package com.example.pokectcollection.domain.cards.usecase

import com.example.pokectcollection.domain.cards.CardRepository
import com.example.pokectcollection.domain.cards.model.Card
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Test class for [GetCardListUseCase].
 *
 * This class contains unit tests to verify the behavior of the [GetCardListUseCase] class.
 */
class GetCardListUseCaseTest {

    private val cardRepository = mockk<CardRepository>()
    private val useCase = GetCardListUseCase(cardRepository)


    /**
     * Test for [GetCardListUseCase.invoke].
     *
     * Verifies that the use case returns the correct list of cards from the repository when invoked.
     */
    @Test
    fun `when getting card list, given repository card list, then it should emit the card list obtained`() = runTest {
        // Given
        val setId = "Test set id"
        val cards = listOf(
            _card.copy(
                id = "Test id 1",
                name = "Test name 1",
                rarity = "Test rarity 1",
                artist = "Test artist 1",
                evolvesFrom = "Test evolvesFrom 1"
            ),
            _card.copy(
                id = "Test id 2",
                name = "Test name 2",
                rarity = "Test rarity 2",
                artist = "Test artist 2",
                evolvesFrom = "Test evolvesFrom 2"
            )
        )
        coEvery { cardRepository.getCardList(any()) } returns flowOf(cards)

        // When
        val result = useCase(setId).first()

        // Then
        assertEquals(expected = cards, actual = result)
    }

    private val _card = Card.getEmptyCard()
}