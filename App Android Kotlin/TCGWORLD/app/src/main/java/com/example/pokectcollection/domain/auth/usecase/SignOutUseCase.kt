package com.example.pokectcollection.domain.auth.usecase

import com.example.pokectcollection.domain.auth.AuthRepository

/**
 * Use case [SignOutUseCase] to handle user sign-out.
 *
 * @property authRepository Repository to handle authentication-related data.
 */
class SignOutUseCase(
    private val authRepository: AuthRepository
) {

    /**
     * Invoke the use case to sign out the current user.
     */
    operator fun invoke() {
        authRepository.signOut()
    }
}
