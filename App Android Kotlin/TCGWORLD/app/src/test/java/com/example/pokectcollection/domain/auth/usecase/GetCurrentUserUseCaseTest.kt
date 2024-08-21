package com.example.pokectcollection.domain.auth.usecase

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
 * Test class for [GetCurrentUserUseCase].
 *
 * This class contains unit tests to verify the behavior of the [GetCurrentUserUseCase] class.
 */
class GetCurrentUserUseCaseTest {

    private val authRepository = mockk<AuthRepository>()
    private val userRepository = mockk<UserRepository>()
    private val useCase = GetCurrentUserUseCase(authRepository, userRepository)

    private val _user = User(
        name = "Test User",
        email = "testuser@example.com",
        profilePictureUrl = "http://example.com/photo.jpg",
        cardList = listOf("card1", "card2")
    )

    /**
     * Test for [GetCurrentUserUseCase.invoke].
     *
     * Verifies that the use case returns the current user when the user is authenticated.
     */
    @Test
    fun `when user is authenticated, it should return the current user`() = runTest {
        // Given
        val userId = "TestUserId"

        every { authRepository.getCurrentUserId() } returns userId
        every { userRepository.getUserById(userId) } returns flowOf(_user)

        // When
        val result = useCase().first()

        // Then
        assertEquals(expected = _user, actual = result)
    }

    /**
     * Test for [GetCurrentUserUseCase.invoke].
     *
     * Verifies that the use case returns null when the user is not authenticated.
     */
    @Test
    fun `when user is not authenticated, it should return null`() = runTest {
        // Given
        every { authRepository.getCurrentUserId() } returns null

        // When
        val result = useCase().first()

        // Then
        assertEquals(expected = null, actual = result)
    }
}