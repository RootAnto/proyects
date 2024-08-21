package com.example.pokectcollection.domain.auth.usecase

import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.auth.model.AuthResponse
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals


/**
 * Test class for [LoginWithEmailAndPasswordUseCase].
 *
 * This class contains unit tests to verify the behavior of the [LoginWithEmailAndPasswordUseCase] class.
 */
class LoginWithEmailAndPasswordUseCaseTest {

    private val authRepository = mockk<AuthRepository>()
    private val useCase = LoginWithEmailAndPasswordUseCase(authRepository)

    /**
     * Test for [LoginWithEmailAndPasswordUseCase.invoke].
     *
     * Verifies that the use case returns [AuthResponse] with isAuthSuccessful true when login is successful.
     */
    @Test
    fun `when login is successful, it should return AuthResponse with isAuthSuccessful true`() = runTest {
        // Given
        val email = "testuser@example.com"
        val password = "password123"
        val authResponse = AuthResponse(isAuthSuccessful = true, errorMessage = null)

        every { authRepository.signInWithEmailAndPassword(email, password) } returns flowOf(authResponse)

        // When
        val result = useCase(email, password).first()

        // Then
        assertEquals(expected = authResponse, actual = result)
    }

    /**
     * Test for [LoginWithEmailAndPasswordUseCase.invoke].
     *
     * Verifies that the use case returns [AuthResponse] with isAuthSuccessful false and error message when login fails.
     */
    @Test
    fun `when login fails, it should return AuthResponse with isAuthSuccessful false and error message`() = runTest {
        // Given
        val email = "testuser@example.com"
        val password = "wrongpassword"
        val authResponse = AuthResponse(isAuthSuccessful = false, errorMessage = "Invalid credentials")

        every { authRepository.signInWithEmailAndPassword(email, password) } returns flowOf(authResponse)

        // When
        val result = useCase(email, password).first()

        // Then
        assertEquals(expected = authResponse, actual = result)
    }
}
