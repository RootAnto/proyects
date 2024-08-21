package com.example.pokectcollection.domain.cards.usecase

import com.example.pokectcollection.domain.cards.CardRepository
import com.example.pokectcollection.domain.cards.model.Card
import kotlinx.coroutines.flow.Flow

/**
 * Use case [GetCardUseCase] to get a specific card by its ID.
 *
 * @param cardRepository The repository to fetch card data.
 */
class GetCardUseCase(
    private val cardRepository: CardRepository,
) {
    /**
     * Invokes the use case to get a card by its ID.
     *
     * @param id The ID of the card to fetch.
     * @return A [Flow] emitting the card, or null if not found.
     */
    operator fun invoke(id: String): Flow<Card?> {
        val setId = getSetIdFromCardId(id)
        return cardRepository.getCardById(setId = setId, cardId = id)
    }

    /**
     * Extracts the set ID from the card ID.
     *
     * @param cardId The ID of the card.
     * @return The set ID.
     */
    private fun getSetIdFromCardId(cardId: String): String =
        cardId.split("-")[0]
}
