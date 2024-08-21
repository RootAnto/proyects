package com.example.pokectcollection.domain.cards.usecase

import com.example.pokectcollection.domain.cards.CardRepository
import com.example.pokectcollection.domain.cards.model.Card
import kotlinx.coroutines.flow.Flow

/**
 * Use case [GetCardListUseCase] to get a list of cards for a specific set.
 *
 * @param cardRepository The repository to fetch card data.
 */
class GetCardListUseCase(
    private val cardRepository: CardRepository
) {

    /**
     * Invokes the use case to get a list of cards for the given set ID.
     *
     * @param setId The ID of the set to get the card list for.
     * @return A [Flow] emitting the list of cards.
     */
    suspend operator fun invoke(setId: String): Flow<List<Card>> =
        cardRepository.getCardList(setId)
}
