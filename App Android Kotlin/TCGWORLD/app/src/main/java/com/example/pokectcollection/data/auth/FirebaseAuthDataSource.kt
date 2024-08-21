package com.example.pokectcollection.data.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * The data source for the authentication with Firebase.
 */
class FirebaseAuthDataSource @Inject constructor() {

    private val auth: FirebaseAuth = Firebase.auth
    private val authFlow = MutableStateFlow(Firebase.auth)

    /**
     * Function to register the user with email and password.
     *
     * @param email the email to use for the sign up.
     * @param password the password to use for the sign up.
     *
     * @return the [Task] with the result of the register ([AuthResult]).
     */
    fun signUpWithEmailAndPassword(email: String, password: String): Task<AuthResult> =
        auth.createUserWithEmailAndPassword(email, password)

    /**
     * Function to send an email wth a confirmation.
     */
    fun sendEmailVerification() = auth.currentUser?.sendEmailVerification()

    /**
     * Function to login into the app using email and password.
     *
     * @param email the email to use for the login.
     * @param password the password to use for the login.
     *
     * @return the [Task] with the result of the login [AuthResult].
     */
    fun signInWithEmailAndPassword(email: String, password: String): Task<AuthResult> =
        auth.signInWithEmailAndPassword(email, password)

    /**
     * Function that logout the user from the firebase session.
     */
    fun signOut() {
        auth.signOut()
    }

    /**
     * Function to get the current user id of the Firebase session.
     *
     * @return the string id of the [FirebaseUser] of the current session.
     */
    fun getCurrentSignedUserId(): String? = auth.currentUser?.uid

    /**
     * Retrieves the join date of the currently signed-in user.
     *
     * @return A flow emitting the join date (creation timestamp) of the current user.
     */
    fun getCurrentUserJoinDate() =
        flowOf(auth.currentUser?.metadata?.creationTimestamp as Long)

    /**
     * Subscribes to the authentication state of the current user.
     *
     * @return A flow emitting the UID of the current user.
     */
    fun subscribeToAuthState() = flowOf(Firebase.auth.currentUser?.uid)


}
