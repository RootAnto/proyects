import androidx.lifecycle.SavedStateHandle
import com.example.pokectcollection.domain.achievements.model.Achievement
import com.example.pokectcollection.domain.achievements.usecase.GetAchievementListUseCase
import com.example.pokectcollection.domain.auth.usecase.SignOutUseCase
import com.example.pokectcollection.rule.MainCoroutineRule
import com.example.pokectcollection.ui.achievements.AchievementsViewModel
import com.example.pokectcollection.ui.achievements.model.AchievementUiState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AchievementsViewModelTest {

    @get: Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val getAchievementListUseCase = mockk<GetAchievementListUseCase>()
    private val signOutUseCase = mockk<SignOutUseCase>()

    private fun createViewModel() = AchievementsViewModel(
        getAchievementListUseCase = getAchievementListUseCase,
        signOutUseCase = signOutUseCase
    )

    private val _achievement = Achievement(
        name = "",
        description = "",
        url = ""
    )

    // Test para verificar que el estado inicial es vacío cuando no se proporcionan logros
    @Test
    fun `when getting state, given no achievements, then state must be empty`() = runTest {
        // Given
        every { getAchievementListUseCase() } returns flowOf(emptyList())

        // When
        val viewModel = createViewModel()
        advanceUntilIdle()
        val result = viewModel.uiState.value

        // Then
        val expected = AchievementUiState.getEmptyState()
        assertEquals(expected, result)
    }

    /**
    // Test para verificar que el estado se mapea correctamente cuando se proporcionan logros
    //todo

    @Test
    fun `when getting state, given achievements, then state must be properly mapped`() = runTest {
        // Given
        val achievements = listOf(
            _achievement.copy(date = "2023-01-01", name = "Achievement 1", description = "Description 1", url = "http://example.com/1"),
            _achievement.copy(date = "2023-02-01", name = "Achievement 2", description = "Description 2", url = "http://example.com/2")
        )
        every { getAchievementListUseCase() } returns flowOf(achievements)

        // When
        val viewModel = createViewModel()
        advanceUntilIdle()
        val result = viewModel.uiState.value

        // Then
        assertEquals(expected = "2023-01-01", actual = result.date)
        assertEquals(expected = "Achievement 1", actual = result.name)
        assertEquals(expected = "Description 1", actual = result.description)
        assertEquals(expected = "http://example.com/1", actual = result.url)
    }
*/

}
