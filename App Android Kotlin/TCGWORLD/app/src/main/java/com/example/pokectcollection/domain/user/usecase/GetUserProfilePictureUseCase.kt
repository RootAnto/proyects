package com.example.pokectcollection.domain.user.usecase

import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Use case to get the profile picture URL of the current user.
 *
 * @property authRepository The repository to handle authentication operations.
 * @property userRepository The repository to handle user data operations.
 */
class GetUserProfilePictureUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {

    /**
     * Executes the use case to retrieve the profile picture URL of the current user.
     *
     * @return A flow emitting the profile picture URL of the user, or null if the user is not authenticated.
     */
    operator fun invoke(): Flow<String?> {
        val userId = authRepository.getCurrentUserId()
        return if (userId != null) {
            userRepository.getUserProfilePicture(userId)
        } else {
            flowOf(null)
        }
    }
}
