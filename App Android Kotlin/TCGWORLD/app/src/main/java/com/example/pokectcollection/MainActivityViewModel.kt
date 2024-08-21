package com.example.pokectcollection

import androidx.lifecycle.ViewModel
import com.example.pokectcollection.domain.user.usecase.CheckAchievementsUseCase
import com.example.pokectcollection.ui.achievements.model.AchievementLabelUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * ViewModel class [MainActivityViewModel] that manages the UI state for the MainActivity.
 *
 * @param checkAchievementsUseCase the use case for checking user achievements [CheckAchievementsUseCase].
 */
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val checkAchievementsUseCase: CheckAchievementsUseCase
) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val _uiState = MutableStateFlow(AchievementLabelUiState.getEmptyState())
    val uiState: StateFlow<AchievementLabelUiState> = _uiState.asStateFlow()

    /**
     * Function [checkUserAchievements] that checks the user's achievements and updates the UI state.
     */
    fun checkUserAchievements() {
        scope.launch {
            checkAchievementsUseCase().collect {
                _uiState.value = AchievementLabelUiState(it,true)
            }
        }
    }

    /**
     * Function [updateState] that resets the UI state to its empty state.
     */
    fun updateState() {
        _uiState.value = AchievementLabelUiState.getEmptyState()
    }


}