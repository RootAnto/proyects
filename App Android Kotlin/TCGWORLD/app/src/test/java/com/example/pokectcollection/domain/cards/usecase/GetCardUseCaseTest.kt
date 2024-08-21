package com.example.pokectcollection.domain.cards.usecase

import com.example.pokectcollection.domain.cards.CardRepository
import com.example.pokectcollection.domain.cards.model.Card
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals


/**
 * Test class for [GetCardUseCase].
 *
 * This class contains unit tests to verify the behavior of the [GetCardUseCase] class.
 */
class GetCardUseCaseTest {

    private val cardRepository = mockk<CardRepository>()
    private val useCase = GetCardUseCase(cardRepository)

    /**
     * Test for [GetCardUseCase.invoke].
     *
     * Verifies that the use case returns the correct card details from the repository when invoked.
     */
    @Test
    fun `when getting card detail, given repository card, then it should emit the card obtained`() =
        runTest {
            // Given
            val cardId = "Test-id"
            val card = _card.copy(
                id = cardId,
                name = "Test name",
                rarity = "Test rarity",
                artist = "Test artist",
                flavorText = "Test flavor text"
            )
            every { cardRepository.getCardById(any(), any()) } returns flowOf(card)

            // When
            val result = useCase(cardId).first()

            // Then
            assertEquals(expected = card, actual = result)
        }



    private val _card = Card.getEmptyCard()
}
