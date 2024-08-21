package com.example.pokectcollection.domain.cards.usecase

import com.example.pokectcollection.domain.cards.CardRepository

/**
 * Use case [GetAllCardsUseCase] for retrieving all cards from the [CardRepository].
 *
 * @param cardRepository The repository that provides card data.
 */
class GetAllCardsUseCase(
    private val cardRepository: CardRepository
) {

    /**
     * Invokes the use case to retrieve all cards.
     *
     * @return A flow emitting the list of all cards from the [CardRepository].
     */
    operator fun invoke() = cardRepository.getAllCards()
}
