package com.example.pokectcollection.domain.user.usecase

import com.example.pokectcollection.domain.user.UserRepository
import com.example.pokectcollection.ui.login.model.UserData
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Test


/**
 * Test class for [CreateUserDocUseCase].
 *
 * This class contains unit tests to verify the behavior of the [CreateUserDocUseCase] class.
 */
class CreateUserDocUseCaseTest {

    private val userRepository = mockk<UserRepository>()
    private val useCase = CreateUserDocUseCase(userRepository)

    /**
     * Test for [CreateUserDocUseCase.invoke] to ensure that it calls [UserRepository.createUserDoc].
     *
     * This test verifies that the [createUserDoc] method in the [UserRepository] is called with the correct [UserData] when the use case is invoked.
     */
    @Test
    fun `when creating user doc, it should call createUserDoc on userRepository`() {
        // Given
        val googleUser = UserData(
            userId = "TestUserId",
            email = "testuser@example.com",
            username = "Test User",
            profilePictureUrl = "http://example.com/photo.jpg",
            cardList = listOf("card1", "card2")
        )

        every { userRepository.createUserDoc(any()) } just runs

        // When
        useCase(googleUser)

        // Then
        verify { userRepository.createUserDoc(googleUser) }
    }
}