package com.example.pokectcollection.domain.cards.usecase

import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.cards.CardRepository
import com.example.pokectcollection.domain.cards.model.Card
import com.example.pokectcollection.domain.user.UserRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class GetCustomCardListsUseCaseTest {

    private val authRepository = mockk<AuthRepository>()
    private val userRepository = mockk<UserRepository>()
    private val cardRepository = mockk<CardRepository>()
    private val useCase = GetCustomCardListsUseCase(authRepository, userRepository, cardRepository)

    private val _card = Card.getEmptyCard()

    /*@Test
    fun `when getting custom card lists, given repositories, then it should emit the custom card lists obtained`() = runTest {
        // Given
        val userId = "TestUserId"
        val customLists = listOf(
            listOf("setId1", "cardId1", "cardId2"),
            listOf("setId2", "cardId3", "cardId4")
        )
        val cards = listOf(
            _card.copy(id = "setId1-cardId1", name = "Test name 1", rarity = "Test rarity 1", artist = "Test artist 1", evolvesFrom = "Test evolvesFrom 1"),
            _card.copy(id = "setId1-cardId2", name = "Test name 2", rarity = "Test rarity 2", artist = "Test artist 2", evolvesFrom = "Test evolvesFrom 2"),
            _card.copy(id = "setId2-cardId3", name = "Test name 3", rarity = "Test rarity 3", artist = "Test artist 3", evolvesFrom = "Test evolvesFrom 3"),
            _card.copy(id = "setId2-cardId4", name = "Test name 4", rarity = "Test rarity 4", artist = "Test artist 4", evolvesFrom = "Test evolvesFrom 4")
        )

        every { authRepository.getCurrentUserId() } returns userId
        every { userRepository.getUserCustomLists(userId) } returns flowOf(customLists)
        every { cardRepository.getCustomCardById(any(), any()) } answers {
            val setId = firstArg<String>()
            val cardId = secondArg<String>()
            val cardIdFull = "$setId-$cardId"
            cards.first { it.id == cardIdFull }
        }

        // When
        val result = useCase().first()

        // Then
        val expected = listOf(
            listOf(
                _card.copy(id = "setId1-cardId1", name = "Test name 1", rarity = "Test rarity 1", artist = "Test artist 1", evolvesFrom = "Test evolvesFrom 1"),
                _card.copy(id = "setId1-cardId2", name = "Test name 2", rarity = "Test rarity 2", artist = "Test artist 2", evolvesFrom = "Test evolvesFrom 2")
            ),
            listOf(
                _card.copy(id = "setId2-cardId3", name = "Test name 3", rarity = "Test rarity 3", artist = "Test artist 3", evolvesFrom = "Test evolvesFrom 3"),
                _card.copy(id = "setId2-cardId4", name = "Test name 4", rarity = "Test rarity 4", artist = "Test artist 4", evolvesFrom = "Test evolvesFrom 4")
            )
        )
        assertEquals(expected = expected, actual = result)
    }*/
}

