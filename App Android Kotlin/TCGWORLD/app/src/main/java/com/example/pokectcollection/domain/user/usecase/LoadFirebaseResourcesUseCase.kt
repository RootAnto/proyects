package com.example.pokectcollection.domain.user.usecase

import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.cards.CardRepository
import com.example.pokectcollection.domain.setlist.SetRepository
import com.example.pokectcollection.domain.user.UserRepository

/**
 * Use case to load resources from Firebase.
 *
 * @property authRepository The repository to handle authentication operations.
 * @property setRepository The repository to handle set data operations.
 * @property cardRepository The repository to handle card data operations.
 * @property userRepository The repository to handle user data operations.
 */
class LoadFirebaseResourcesUseCase(
    private val authRepository: AuthRepository,
    private val setRepository: SetRepository,
    private val cardRepository: CardRepository,
    private val userRepository: UserRepository
) {

    /**
     * Executes the use case to load resources from Firebase.
     *
     * If the user is authenticated, it retrieves the user's owned card list and updates the sets with owned cards.
     * If the user is not authenticated, it loads the set cards without owned card data.
     */
    suspend operator fun invoke() {
        val userId = authRepository.getCurrentUserId()
        val setList = setRepository.getFirebaseSets()
        if (userId != null) {
            val achievementList = userRepository.getUserAchievements(userId)
            val ownedCardList = userRepository.getUserOwnedCardList(userId)
            cardRepository.loadUserAchievements(achievementList)
            setList.forEach { set ->
                val cardOwned = ownedCardList.filter { it.split("-")[0] == set.id }
                cardRepository.loadFirebaseSetCards(
                    set = set.copy(ownedCards = cardOwned.size),
                    ownedCardList = cardOwned,
                    areOwnedCardsIncluded = true
                )
            }
        } else {
            setList.forEach { set ->
                cardRepository.loadFirebaseSetCards(set, emptyList(), false)
            }
        }
    }
}
