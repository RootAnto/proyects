package com.example.pokectcollection.ui.topbar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokectcollection.domain.auth.usecase.SignOutUseCase
import com.example.pokectcollection.domain.user.usecase.GetUserProfilePictureUseCase
import com.example.pokectcollection.ui.topbar.model.TopBarUiState
import com.example.pokectcollection.ui.topbar.view.TopMenuBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * ViewModel for [TopMenuBar], responsible for managing the state of the user's profile picture
 * and handling sign out actions.
 *
 * @property getUserProfilePictureUseCase UseCase to fetch the user's profile picture URL.
 * @property signOutUseCase UseCase to handle user sign out.
 */
@HiltViewModel
class TopBarViewModel @Inject constructor(
    private val getUserProfilePictureUseCase: GetUserProfilePictureUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    // MutableStateFlow to hold the current UI state of the TopBar
    private val _uiState: MutableStateFlow<TopBarUiState> =
        MutableStateFlow(TopBarUiState.getEmptyState())

    // StateFlow to expose the UI state to the UI layer
    val uiState: StateFlow<TopBarUiState> = _uiState.asStateFlow()

    init {
        // Fetch the user's profile picture URL when the ViewModel is initialized

    }

    fun getProfilePicture() {
        getUserProfilePictureUseCase().onEach { profilePictureUrl ->
            if (profilePictureUrl != null) {
                _uiState.value = TopBarUiState(profilePictureUrl = profilePictureUrl)
                println(_uiState.value)
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Signs out the current user.
     */
    fun signOut() {
        signOutUseCase()
    }
}
