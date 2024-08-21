package com.example.pokectcollection.domain.auth.usecase

import com.example.pokectcollection.domain.auth.AuthRepository
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Test

/**
 * Test class for [SignOutUseCase].
 *
 * This class contains unit tests to verify the behavior of the [SignOutUseCase] class.
 */
class SignOutUseCaseTest {

    private val authRepository = mockk<AuthRepository>()
    private val useCase = SignOutUseCase(authRepository)

    /**
     * Test for [SignOutUseCase.invoke].
     *
     * Verifies that the use case calls the signOut method of the [AuthRepository] to sign out the current user.
     */
    @Test
    fun `when invoke is called, it should sign out the current user`() {
        // Given
        every { authRepository.signOut() } just runs

        // When
        useCase()

        // Then
        verify { authRepository.signOut() }
    }
}
