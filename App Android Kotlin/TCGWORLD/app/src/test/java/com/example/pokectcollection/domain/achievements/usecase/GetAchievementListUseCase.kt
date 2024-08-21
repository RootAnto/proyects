package com.example.pokectcollection.domain.achievements.usecase

import com.example.pokectcollection.domain.achievements.AchievementRepository
import com.example.pokectcollection.domain.achievements.model.Achievement
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Test class for [GetAchievementListUseCase].
 *
 * This class contains unit tests to verify the behavior of the [GetAchievementListUseCase] class.
 */
class GetAchievementListUseCaseTest {

    private val achievementRepository = mockk<AchievementRepository>()
    private val useCase = GetAchievementListUseCase(achievementRepository)

    /**
     * Test for [GetAchievementListUseCase.invoke].
     *
     * Verifies that the use case returns a list of achievements when invoked.
     */
    @Test
    fun `when invoked, it should return a list of achievements`() = runTest {
        // Given
        val achievements = listOf(
            Achievement(name = "Achievement 1", description = "Description 1",  url = "http://example.com/1"),
            Achievement(name = "Achievement 2", description = "Description 2",  url = "http://example.com/2")
        )

        every { achievementRepository.getAchievementList() } returns flowOf(achievements)

        // When
        val result = useCase().first()

        // Then
        assertEquals(expected = achievements, actual = result)
    }
}