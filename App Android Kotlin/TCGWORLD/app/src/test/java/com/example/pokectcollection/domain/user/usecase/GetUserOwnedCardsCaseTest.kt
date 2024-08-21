package com.example.pokectcollection.domain.user.usecase

import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.user.UserRepository
import com.example.pokectcollection.domain.user.model.User
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Test class for [GetUserOwnedCardsUseCase].
 *
 * This class contains unit tests to verify the behavior of the [GetUserOwnedCardsUseCase] class.
 */
class GetUserOwnedCardsUseCaseTest {

    private val authRepository = mockk<AuthRepository>()
    private val userRepository = mockk<UserRepository>()
    private val useCase = GetUserOwnedCardsUseCase(authRepository, userRepository)

    private val _user = User(
        name = "Test User",
        email = "testuser@example.com",
        profilePictureUrl = "http://example.com/photo.jpg",
        cardList = listOf("card1", "card2")
    )

    /**
     * Test for [GetUserOwnedCardsUseCase.invoke] to ensure that it returns the list of owned cards when the user is authenticated.
     *
     * This test verifies that the [GetUserOwnedCardsUseCase] returns the correct list of owned cards when the user is authenticated.
     */
    @Test
    fun `when user is authenticated, it should return the list of owned cards`() = runTest {
        // Given
        val userId = "TestUserId"
        val ownedCards = listOf("card1", "card2")

        every { authRepository.getCurrentUserId() } returns userId
        every { userRepository.getUserById(userId) } returns flowOf(_user)

        // When
        val result = useCase().first()

        // Then
        assertEquals(expected = ownedCards, actual = result)
    }

    /**
     * Test for [GetUserOwnedCardsUseCase.invoke] to ensure that it returns an empty list when the user is not authenticated.
     *
     * This test verifies that the [GetUserOwnedCardsUseCase] returns an empty list when the user is not authenticated.
     */
    @Test
    fun `when user is not authenticated, it should return an empty list`() = runTest {
        // Given
        every { authRepository.getCurrentUserId() } returns null

        // When
        val result = useCase().first()

        // Then
        assertEquals(expected = emptyList<String>(), actual = result)
    }
}
