package com.example.pokectcollection.domain.cards.usecase

import com.example.pokectcollection.data.card.toDomainModel
import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.cards.CardRepository
import com.example.pokectcollection.domain.cards.model.CustomCardList
import com.example.pokectcollection.domain.user.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

/**
 * Use case [GetCustomCardListsUseCase] to get custom card lists for a user.
 *
 * @param authRepository The repository to manage authentication.
 * @param userRepository The repository to manage user data.
 * @param cardRepository The repository to manage card data.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class GetCustomCardListsUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val cardRepository: CardRepository
) {

    /**
     * Invokes the use case to get custom card lists for the current user.
     *
     * @return A [Flow] emitting a list of custom card lists, each containing a list of [CustomCardList].
     */
    operator fun invoke(): Flow<List<CustomCardList>> {
        val userId = authRepository.getCurrentUserId() as String
        return userRepository.getUserCustomLists(userId).flatMapLatest { customLists ->
            flowOf(customLists.map {
                it.toDomainModel { setId, cardId ->
                    cardRepository.getCustomCardById(setId, cardId)
                }
            })
        }
    }
}
