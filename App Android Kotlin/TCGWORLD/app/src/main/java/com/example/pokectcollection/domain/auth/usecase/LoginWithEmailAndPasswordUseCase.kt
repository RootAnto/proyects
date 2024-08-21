package com.example.pokectcollection.domain.auth.usecase

import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.auth.model.AuthResponse
import kotlinx.coroutines.flow.Flow

/**
 * Use case [LoginWithEmailAndPasswordUseCase] to handle user login with email and password.
 *
 * @property authRepository Repository to handle authentication-related data.
 */
class LoginWithEmailAndPasswordUseCase(
    private val authRepository: AuthRepository
) {

    /**
     * Invoke the use case to sign in a user with email and password.
     *
     * @param email The email of the user.
     * @param password The password of the user.
     *
     * @return A [Flow] emitting the [AuthResponse] which indicates the success or failure
     * of the authentication attempt.
     */
    operator fun invoke(email: String, password: String): Flow<AuthResponse> =
        authRepository.signInWithEmailAndPassword(email = email, password = password)
}
