package com.example.pokectcollection.domain.auth

import com.example.pokectcollection.domain.auth.model.AuthResponse
import com.example.pokectcollection.domain.user.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Interface [AuthRepository] for authentication repository.
 * This interface defines methods for user authentication operations.
 */
interface AuthRepository {

    /**
     * Signs up a user with email and password.
     *
     * @param user The user details.
     * @param password The user's password.
     * @return A flow of the sign-up response.
     */
    fun signUpWithEmailAndPassword(user: User, password: String): Flow<AuthResponse>

    /**
     * Signs in a user with email and password.
     *
     * @param email The user's email.
     * @param password The user's password.
     * @return A flow of the sign-in response.
     */
    fun signInWithEmailAndPassword(email: String, password: String): Flow<AuthResponse>

    /**
     * Signs out the currently logged-in user.
     */
    fun signOut()

    /**
     * Gets the current user's ID.
     *
     * @return The current user's ID, or null if no user is logged in.
     */
    fun getCurrentUserId(): String?

    /**
     * Get the current user join date.
     *
     * @return The current user date when joined the app.
     */
    fun getCurrentUserJoinDate(): Flow<Long>


    /**
     * Subscribes to the authentication state of the current user.
     *
     * @return A flow emitting the UID of the current user or null if no user is signed in.
     */
    fun getCurrentUserState(): Flow<String?>

}
