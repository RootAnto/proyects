package com.example.pokectcollection.domain.user.usecase

import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.cards.CardRepository
import com.example.pokectcollection.domain.cards.model.Card
import com.example.pokectcollection.domain.user.UserRepository
import com.example.pokectcollection.domain.user.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

/**
 * Use case to update the ownership status of a card.
 *
 * @property authRepository The repository to handle authentication operations.
 * @property userRepository The repository to handle user data operations.
 * @property cardRepository The repository to handle card data operations.
 */
class UpdateCardOwnershipUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val cardRepository: CardRepository
) {

    /**
     * Executes the use case to update the ownership status of a card.
     *
     * @param setId The ID of the set containing the card.
     * @param cardId The ID of the card to update.
     * @return A flow emitting the updated list of cards.
     */
    suspend operator fun invoke(setId: String, cardId: String): Flow<List<Card>> {
        val currentUserId = authRepository.getCurrentUserId()
        currentUserId?.let { userId ->
            userRepository.getUserById(userId).collect { user ->
                changeCardOwnership(cardId, user as User, userId)
                if ("First card" !in user.achievementList)
                    userRepository.updateUserAchievements(userId, user.achievementList + "First card")
            }
        }
        return cardRepository.updateLocalCardOwnership(setId, cardId)
    }

    /**
     * Changes the ownership status of a card for a user.
     *
     * @param cardId The ID of the card to update.
     * @param user The user whose card ownership status is being updated.
     * @param userId The ID of the user.
     */
    private fun changeCardOwnership(cardId: String, user: User, userId: String) {
        if (cardId in user.cardList) {
            val newList = (user.cardList as MutableList).also { it.remove(cardId) }
            val newUserInfo = user.copy(cardList = newList)
            userRepository.updateUserInfo(userId, newUserInfo)
        } else {
            val newList = if (user.cardList.isEmpty()) {
                mutableListOf(cardId)
            } else {
                (user.cardList as MutableList).also { it.add(cardId) }
            }
            val newUserInfo = user.copy(cardList = newList)
            userRepository.updateUserInfo(userId, newUserInfo)
        }
    }
}
