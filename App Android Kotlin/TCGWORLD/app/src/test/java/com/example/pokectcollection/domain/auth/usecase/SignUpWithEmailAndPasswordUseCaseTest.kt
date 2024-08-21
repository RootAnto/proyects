package com.example.pokectcollection.domain.auth.usecase

import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.auth.model.AuthResponse
import com.example.pokectcollection.domain.user.model.User
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals


/**
 * Test class for [SignUpWithEmailAndPasswordUseCase].
 *
 * This class contains unit tests to verify the behavior of the [SignUpWithEmailAndPasswordUseCase] class.
 */
class SignUpWithEmailAndPasswordUseCaseTest {

    private val authRepository = mockk<AuthRepository>()
    private val useCase = SignUpWithEmailAndPasswordUseCase(authRepository)

    private val _user = User(
        name = "Test User",
        email = "testuser@example.com",
        profilePictureUrl = "http://example.com/photo.jpg",
        cardList = listOf("card1", "card2")
    )

    /**
     * Test for [SignUpWithEmailAndPasswordUseCase.invoke].
     *
     * Verifies that the use case returns [AuthResponse] with isAuthSuccessful true when sign-up is successful.
     */
    @Test
    fun `when sign-up is successful, it should return AuthResponse with isAuthSuccessful true`() = runTest {
        // Given
        val password = "password123"
        val authResponse = AuthResponse(isAuthSuccessful = true, errorMessage = null)

        every { authRepository.signUpWithEmailAndPassword(_user, password) } returns flowOf(authResponse)

        // When
        val result = useCase(_user, password).first()

        // Then
        assertEquals(expected = authResponse, actual = result)
    }

    /**
     * Test for [SignUpWithEmailAndPasswordUseCase.invoke].
     *
     * Verifies that the use case returns [AuthResponse] with isAuthSuccessful false and an error message when sign-up fails.
     */
    @Test
    fun `when sign-up fails, it should return AuthResponse with isAuthSuccessful false and error message`() = runTest {
        // Given
        val password = "password123"
        val authResponse = AuthResponse(isAuthSuccessful = false, errorMessage = "Sign-up failed")

        every { authRepository.signUpWithEmailAndPassword(_user, password) } returns flowOf(authResponse)

        // When
        val result = useCase(_user, password).first()

        // Then
        assertEquals(expected = authResponse, actual = result)
    }
}
