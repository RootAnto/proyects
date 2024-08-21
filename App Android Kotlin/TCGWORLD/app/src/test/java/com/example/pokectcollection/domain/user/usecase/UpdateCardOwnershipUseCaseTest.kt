package com.example.pokectcollection.domain.user.usecase

import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.cards.CardRepository
import com.example.pokectcollection.domain.cards.model.Card
import com.example.pokectcollection.domain.user.UserRepository
import com.example.pokectcollection.domain.user.model.User
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Test class for [UpdateCardOwnershipUseCase].
 *
 * This class contains unit tests to verify the behavior of the [UpdateCardOwnershipUseCase] class.
 */
class UpdateCardOwnershipUseCaseTest {

    private val authRepository = mockk<AuthRepository>()
    private val userRepository = mockk<UserRepository>()
    private val cardRepository = mockk<CardRepository>()
    private val useCase = UpdateCardOwnershipUseCase(authRepository, userRepository, cardRepository)

    private val _user = User(
        name = "Test User",
        email = "testuser@example.com",
        profilePictureUrl = "http://example.com/photo.jpg",
        cardList = mutableListOf("card1", "card2")
    )

    private val _card = Card.getEmptyCard()

    /**
     * Test for [UpdateCardOwnershipUseCase.invoke] when the user owns the card.
     *
     * This test verifies that the card is removed from the user's card list and the ownership is updated.
     */
    @Test
    fun `when user owns the card, it should remove the card from the user's card list and update ownership`() = runTest {
        // Given
        val userId = "TestUserId"
        val setId = "TestSetId"
        val cardId = "card1"
        val updatedCards = listOf(_card.copy(id = "card2"))

        every { authRepository.getCurrentUserId() } returns userId
        every { userRepository.getUserById(userId) } returns flowOf(_user)
        every { userRepository.updateUserInfo(any(), any()) } just Runs
        every { cardRepository.updateLocalCardOwnership(setId, cardId) } returns flowOf(updatedCards)
        every { userRepository.updateUserAchievements(any(),any()) } returns Unit
        // When
        val result = useCase(setId, cardId).first()

        // Then
        assertEquals(expected = updatedCards, actual = result)
        verify { userRepository.updateUserInfo(userId, _user.copy(cardList = listOf("card2"))) }
    }

    /**
     * Test for [UpdateCardOwnershipUseCase.invoke] when the user does not own the card.
     *
     * This test verifies that the card is added to the user's card list and the ownership is updated.
     */
    @Test
    fun `when user does not own the card, it should add the card to the user's card list and update ownership`() = runTest {
        // Given
        val userId = "TestUserId"
        val setId = "TestSetId"
        val cardId = "card3"
        val updatedCards = listOf(_card.copy(id = "card3"))

        every { authRepository.getCurrentUserId() } returns userId
        every { userRepository.getUserById(userId) } returns flowOf(_user.copy(cardList = mutableListOf("card1", "card2")))
        every { userRepository.updateUserInfo(any(), any()) } just Runs
        every { cardRepository.updateLocalCardOwnership(setId, cardId) } returns flowOf(updatedCards)
        every { userRepository.updateUserAchievements(any(), any()) } returns Unit

        // When
        val result = useCase(setId, cardId).first()

        // Then
        assertEquals(expected = updatedCards, actual = result)
        verify { userRepository.updateUserInfo(userId, _user.copy(cardList = listOf("card1", "card2", "card3"))) }
    }

}