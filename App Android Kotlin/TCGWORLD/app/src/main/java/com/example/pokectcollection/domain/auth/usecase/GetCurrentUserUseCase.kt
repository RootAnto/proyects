package com.example.pokectcollection.domain.auth.usecase

import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.user.UserRepository
import com.example.pokectcollection.domain.user.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Use case [GetCurrentUserUseCase] to get the currently authenticated user.
 *
 * @property authRepository Repository to handle authentication-related data.
 * @property userRepository Repository to handle user-related data.
 */
class GetCurrentUserUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {

    /**
     * Invoke the use case to get the current user.
     *
     * @return A [Flow] emitting the current [User] if authenticated, or null if no user is authenticated.
     */
    operator fun invoke(): Flow<User?> {
        val currentUser = authRepository.getCurrentUserId()
        return if (currentUser != null) {
            userRepository.getUserById(currentUser)
        } else {
            flowOf(null)
        }
    }
}
