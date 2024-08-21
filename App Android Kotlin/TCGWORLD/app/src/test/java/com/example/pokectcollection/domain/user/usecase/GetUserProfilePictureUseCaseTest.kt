package com.example.pokectcollection.domain.user.usecase

import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.user.UserRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Test class for [GetUserProfilePictureUseCase].
 *
 * This class contains unit tests to verify the behavior of the [GetUserProfilePictureUseCase] class.
 */
class GetUserProfilePictureUseCaseTest {

    private val authRepository = mockk<AuthRepository>()
    private val userRepository = mockk<UserRepository>()
    private val useCase = GetUserProfilePictureUseCase(authRepository, userRepository)

    /**
     * Test for [GetUserProfilePictureUseCase.invoke] to ensure that it returns the profile picture URL when the user is authenticated.
     *
     * This test verifies that the [GetUserProfilePictureUseCase] returns the correct profile picture URL when the user is authenticated.
     */
    @Test
    fun `when user is authenticated, it should return the profile picture URL`() = runTest {
        // Given
        val userId = "TestUserId"
        val profilePictureUrl = "http://example.com/photo.jpg"

        every { authRepository.getCurrentUserId() } returns userId
        every { userRepository.getUserProfilePicture(userId) } returns flowOf(profilePictureUrl)

        // When
        val result = useCase().first()

        // Then
        assertEquals(expected = profilePictureUrl, actual = result)
    }

    /**
     * Test for [GetUserProfilePictureUseCase.invoke] to ensure that it returns null when the user is not authenticated.
     *
     * This test verifies that the [GetUserProfilePictureUseCase] returns null when the user is not authenticated.
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