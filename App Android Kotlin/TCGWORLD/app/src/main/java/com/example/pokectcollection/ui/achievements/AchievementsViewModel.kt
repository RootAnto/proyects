package com.example.pokectcollection.ui.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokectcollection.domain.achievements.usecase.GetAchievementListUseCase
import com.example.pokectcollection.domain.auth.usecase.SignOutUseCase
import com.example.pokectcollection.ui.achievements.model.AchievementUiState
import com.example.pokectcollection.ui.achievements.view.ArchievementScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * The view model for the [ArchievementScreen].
 *
 * @param getAchievementListUseCase the [GetAchievementListUseCase] for fetching the list of achievements.
 * @param signOutUseCase the [SignOutUseCase] for signing out the user.
 */
@HiltViewModel
class AchievementsViewModel @Inject constructor(
    private val getAchievementListUseCase: GetAchievementListUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<AchievementUiState> = MutableStateFlow(AchievementUiState.getEmptyState())

    /**
     * [StateFlow] to be collected in [AchievementScreen] for updating the info displayed.
     */
    val uiState: StateFlow<AchievementUiState> = _uiState.asStateFlow()

    /**
     * Initializes the view model by fetching the list of achievements.
     */
    init {
        getAchievementListUseCase()
            .onEach { println("Achievement list: $it") }
            .launchIn(viewModelScope)
    }

    /**
     * Function to sign out the user.
     */
    fun logOut() {
        signOutUseCase()
    }
}
