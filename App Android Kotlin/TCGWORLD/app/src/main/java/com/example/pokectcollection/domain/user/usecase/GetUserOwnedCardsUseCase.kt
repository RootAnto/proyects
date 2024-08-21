package com.example.pokectcollection.domain.user.usecase

import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

/**
 * Use case to get the list of cards owned by the current user.
 *
 * @property authRepository The repository to handle authentication operations.
 * @property userRepository The repository to handle user data operations.
 */
class GetUserOwnedCardsUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {

    /**
     * Executes the use case to retrieve the list of cards owned by the current user.
     *
     * @return A flow emitting the list of card IDs owned by the user.
     */
    operator fun invoke(): Flow<List<String>> {
        val currentUserId = authRepository.getCurrentUserId()
        return if (currentUserId != null) {
            userRepository.getUserById(currentUserId).map { it?.cardList.orEmpty() }
        } else {
            flowOf(emptyList())
        }
    }
}
