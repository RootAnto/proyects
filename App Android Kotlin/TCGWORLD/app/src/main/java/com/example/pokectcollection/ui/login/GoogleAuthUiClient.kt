package com.example.pokectcollection.ui.login

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.example.pokectcollection.R
import com.example.pokectcollection.ui.login.model.SignInResult
import com.example.pokectcollection.ui.login.model.UserData
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

/**
 * [GoogleAuthUiClient] Client for handling Google authentication using the One Tap sign-in API.
 *
 * @property context the context used to access resources.
 * @property oneTapClient the [SignInClient] used to initiate the Google sign-in flow.
 */
class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    private val auth = Firebase.auth

    /**
     * Initiates the Google sign-in process and returns an [IntentSender] to start the sign-in intent.
     *
     * @return the [IntentSender] to start the sign-in intent, or null if the sign-in initiation failed.
     */
    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    /**
     * Completes the sign-in process using the provided intent.
     *
     * @param intent the intent containing the sign-in credentials.
     * @return a [SignInResult] indicating the outcome of the sign-in attempt.
     */
    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        email = email,
                        username = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    /**
     * Builds the sign-in request for Google authentication.
     *
     * @return a [BeginSignInRequest] configured for Google sign-in.
     */
    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}
