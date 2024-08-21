package com.example.pokectcollection.domain.auth.usecase

import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.user.model.User

/**
 * Use case [SignOutUseCase] to handle user sign-up with email and password.
 *
 * @property authRepository Repository to handle authentication-related data.
 */
class SignUpWithEmailAndPasswordUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * Invoke the use case to sign up a new user with email and password.
     *
     * @param user The user details.
     * @param password The user's password.
     * @return A flow of the sign-up response.
     */
    operator fun invoke(user: User, password: String) =
        authRepository.signUpWithEmailAndPassword(user = user, password = password)
}
