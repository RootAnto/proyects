package com.example.pokectcollection.domain.user.usecase

import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.cards.CardRepository
import com.example.pokectcollection.domain.cards.model.Card
import com.example.pokectcollection.domain.user.UserRepository
import com.example.pokectcollection.domain.user.model.User

/**
 * Use case to create a custom card list for the current [User].
 *
 * @property authRepository The repository to handle authentication operations.
 * @property userRepository The repository to handle user data operations.
 */
class CreateCustomCardListUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {

    /**
     * Executes the use case to create a custom card list.
     *
     * @param cardList The list of cards to be included in the custom card list.
     */
    suspend operator fun invoke(cardList: List<Card>) {
        val currentUserId = authRepository.getCurrentUserId() as String
        val customList: List<String> = cardList.map { it.id }
        userRepository.updateUserCustomLists(
            userId = currentUserId,
            listName = (customList[0].split("-")[0] + " not owned"),
            customList = customList
        )
        val achievementList = userRepository.getUserAchievements(currentUserId)
        if ("First custom list" !in achievementList)
            userRepository.updateUserAchievements(
                currentUserId,
                achievementList + "First custom list"
            )

    }

}
