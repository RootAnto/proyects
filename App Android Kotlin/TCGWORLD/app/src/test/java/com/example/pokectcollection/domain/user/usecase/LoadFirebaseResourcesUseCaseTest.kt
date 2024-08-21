package com.example.pokectcollection.domain.user.usecase

import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.cards.CardRepository
import com.example.pokectcollection.domain.setlist.SetRepository
import com.example.pokectcollection.domain.setlist.model.Set
import com.example.pokectcollection.domain.user.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

/**
 * Test class for [LoadFirebaseResourcesUseCase].
 *
 * This class contains unit tests to verify the behavior of the [LoadFirebaseResourcesUseCase] class.
 */
class LoadFirebaseResourcesUseCaseTest {

    private val authRepository = mockk<AuthRepository>()
    private val setRepository = mockk<SetRepository>()
    private val cardRepository = mockk<CardRepository>()
    private val userRepository = mockk<UserRepository>()
    private val useCase = LoadFirebaseResourcesUseCase(authRepository, setRepository, cardRepository, userRepository)

    private val _set = Set.getEmptySet()

    /**
     * Test for [LoadFirebaseResourcesUseCase.invoke] when the user is not authenticated.
     *
     * This test verifies that the resources are loaded without the owned card data when the user is not authenticated.
     */
    @Test
    fun `when user is authenticated, it should load resources with owned card data`() = runTest {
        // Given
        val userId = "TestUserId"
        val ownedCardList = listOf("setId1-cardId1", "setId1-cardId2")
        val setList = listOf(_set.copy(id = "setId1", name = "Set 1", ownedCards = 0))

        coEvery { authRepository.getCurrentUserId() } returns userId
        coEvery { setRepository.getFirebaseSets() } returns setList
        coEvery { userRepository.getUserOwnedCardList(userId) } returns ownedCardList
        coEvery { cardRepository.loadFirebaseSetCards(any(), any(), any()) } returns Unit
        coEvery { userRepository.getUserAchievements(any()) } returns emptyList()
        coEvery { cardRepository.loadUserAchievements(any()) } returns Unit

        // When
        useCase()

        // Then
        coVerify {
            cardRepository.loadFirebaseSetCards(
                set = _set.copy(id = "setId1", name = "Set 1", ownedCards = ownedCardList.size),
                ownedCardList = ownedCardList,
                areOwnedCardsIncluded = true
            )
        }
    }

    /**
     * Test for [LoadFirebaseResourcesUseCase.invoke] when the user is not authenticated.
     *
     * This test verifies that the resources are loaded without the owned card data when the user is not authenticated.
     */
    @Test
    fun `when user is not authenticated, it should load resources without owned card data`() = runTest {
        // Given
        val setList = listOf(_set.copy(id = "setId1", name = "Set 1", ownedCards = 0))

        coEvery { authRepository.getCurrentUserId() } returns null
        coEvery { setRepository.getFirebaseSets() } returns setList
        coEvery { cardRepository.loadFirebaseSetCards(any(), any(), any()) } returns Unit

        // When
        useCase()

        // Then
        coVerify {
            cardRepository.loadFirebaseSetCards(
                set = _set.copy(id = "setId1", name = "Set 1", ownedCards = 0),
                ownedCardList = emptyList(),
                areOwnedCardsIncluded = false
            )
        }
    }
}