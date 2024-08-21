package com.example.pokectcollection.data.auth

import com.example.pokectcollection.data.user.FirebaseUserDataSource
import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.auth.model.AuthResponse
import com.example.pokectcollection.domain.user.model.User
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * The implementation of [AuthRepository].
 *
 * @param firebaseAuthDataSource the [FirebaseAuthDataSource] that makes the checking for authentication.
 * @param firebaseUserDataSource the [FirebaseUserDataSource] that provides and manage [User] data in Firebase.
 */
class AuthRepositoryImp @Inject constructor(
    private val firebaseAuthDataSource: FirebaseAuthDataSource,
    private val firebaseUserDataSource: FirebaseUserDataSource
) : AuthRepository {

    /**
     * Signs up a new user with email and password.
     *
     * @param user The user details to sign up.
     * @param password The password for the user.
     * @return A flow emitting an [AuthResponse] indicating the result of the sign-up process.
     */
    override fun signUpWithEmailAndPassword(user: User, password: String): Flow<AuthResponse> = flow {
        runCatching {
            firebaseAuthDataSource.signUpWithEmailAndPassword(user.email, password).await()
        }.onSuccess {
            if (it.user != null) {
                firebaseUserDataSource.createUser(user, (it.user as FirebaseUser).uid)
                emit(AuthResponse(isAuthSuccessful = true, errorMessage = null))
            }
        }.onFailure { error ->
            emit(AuthResponse(isAuthSuccessful = false, errorMessage = error.message))
        }
    }

    /**
     * Signs in a user with email and password.
     *
     * @param email The email of the user.
     * @param password The password for the user.
     * @return A flow emitting an [AuthResponse] indicating the result of the sign-in process.
     */
    override fun signInWithEmailAndPassword(email: String, password: String): Flow<AuthResponse> = flow {
        runCatching {
            firebaseAuthDataSource.signInWithEmailAndPassword(email = email, password = password).await()
        }.onSuccess {
            emit(AuthResponse(isAuthSuccessful = true, errorMessage = null))
        }.onFailure { error ->
            emit(AuthResponse(isAuthSuccessful = false, errorMessage = error.message))
        }
    }

    /**
     * Signs out the currently signed-in user.
     */
    override fun signOut() {
        firebaseAuthDataSource.signOut()
    }

    /**
     * Gets the ID of the currently signed-in user.
     *
     * @return The ID of the currently signed-in user, or null if no user is signed in.
     */
    override fun getCurrentUserId(): String? =
        firebaseAuthDataSource.getCurrentSignedUserId()

    /**
     * Gets the join date of the currently signed-in user.
     *
     * @return A flow emitting the join date of the currently signed-in user.
     */
    override fun getCurrentUserJoinDate(): Flow<Long> =
        firebaseAuthDataSource.getCurrentUserJoinDate()

    /**
     * Subscribes to the authentication state of the current user.
     *
     * @return A flow emitting the authentication state of the current user.
     */
    override fun getCurrentUserState(): Flow<String?> =
        firebaseAuthDataSource.subscribeToAuthState()



}
