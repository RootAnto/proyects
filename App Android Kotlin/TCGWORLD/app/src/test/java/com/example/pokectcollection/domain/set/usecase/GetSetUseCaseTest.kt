package com.example.pokectcollection.domain.setlist.usecase


import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.cards.CardRepository
import com.example.pokectcollection.domain.setlist.SetRepository
import com.example.pokectcollection.domain.setlist.model.Set
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Test class for [GetSetsUseCase].
 *
 * This class contains unit tests to verify the behavior of the [GetSetsUseCase] class.
 */
class GetSetsUseCaseTest {

    private val authRepository = mockk<AuthRepository>()
    private val setRepository = mockk<SetRepository>()
    private val cardRepository = mockk<CardRepository>()
    private val useCase = GetSetsUseCase(authRepository, setRepository, cardRepository)


    /**
     * Test for [GetSetsUseCase.invoke] when the set list is not empty.
     *
     * Verifies that the use case returns the correct list of sets from the repository.
     */
    @Test
    fun `when set list is not empty, it should return the list of sets`() = runTest {
        // Given
        val sets = listOf(
            _set.copy(id = "set1", name = "Set 1", releaseDate = "2021-01-01"),
            _set.copy(id = "set2", name = "Set 2", releaseDate = "2021-02-01")
        )
        coEvery { setRepository.getSets() } returns sets

        // When
        val result = useCase().first()

        // Then
        assertEquals(expected = sets, actual = result)
    }

    /**
     * Test for [GetSetsUseCase.invoke] when the set list is empty.
     *
     * Verifies that the use case updates loaded owned cards and returns the updated list of sets.
     */
    @Test
    fun `when set list is empty, it should update loaded owned cards and return the list of sets`() = runTest {
        // Given
        val emptySets = emptyList<Set>()
        val updatedSets = listOf(
            _set.copy(id = "set1", name = "Set 1", releaseDate = "2021-01-01"),
            _set.copy(id = "set2", name = "Set 2", releaseDate = "2021-02-01")
        )
        val userId = "TestUserId"

        coEvery { setRepository.getSets() } returns emptySets andThen updatedSets
        coEvery { authRepository.getCurrentUserId() } returns userId
        coEvery { cardRepository.updateLoadedOwnedCards(userId) } returns emptyList()

        // When
        val result = useCase().first()

        // Then
        assertEquals(expected = updatedSets, actual = result)
        coVerify { cardRepository.updateLoadedOwnedCards(userId) }
    }

    private val _set = Set.getEmptySet()

}