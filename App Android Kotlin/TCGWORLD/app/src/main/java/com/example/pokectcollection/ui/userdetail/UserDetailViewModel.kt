package com.example.pokectcollection.ui.userdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokectcollection.data.user.GetProfileUserDataUseCase
import com.example.pokectcollection.domain.auth.usecase.SignOutUseCase
import com.example.pokectcollection.domain.user.model.User
import com.example.pokectcollection.ui.userdetail.model.UpdateUserImageUseCase
import com.example.pokectcollection.ui.userdetail.model.UserProfileUiState
import com.example.pokectcollection.ui.userdetail.model.toUiState
import com.example.pokectcollection.ui.userdetail.view.UserDetailScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The view model for the [UserDetailScreen].
 *
 * @param getProfileUserDataUseCase the [GetProfileUserDataUseCase] for getting the [User] data.
 * @param updateUserImageUseCase the [UpdateUserImageUseCase] for updating the [User] profile picture.
 */
@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val getProfileUserDataUseCase: GetProfileUserDataUseCase,
    private val updateUserImageUseCase: UpdateUserImageUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val _uiState: MutableStateFlow<UserProfileUiState> = MutableStateFlow(UserProfileUiState.getEmptyState())

    /**
     * [StateFlow] to be collected in [UserDetailScreen] for updating the info displayed.
     */
    val uiState: StateFlow<UserProfileUiState> = _uiState.asStateFlow()

    /**
     * Subscribe to the user info in the firebase database.
     */
    init {
        scope.launch {
            getProfileUserDataUseCase().collect { user ->
                if (user != null) {
                    _uiState.update { user.toUiState() }
                }
            }
        }
    }

    /**
     * Function that send the update in the profile picture of the [User] in Firebase.
     *
     * @param profilePicture the new profile picture resource id.
     */
    fun updateUserImage(profilePicture: String) {
        viewModelScope.launch {
            val userInfo = _uiState.value.user.copy(profilePictureUrl = profilePicture)
            updateUserImageUseCase(userInfo)
            _uiState.update { it.copy(user = it.user.copy(profilePictureUrl = profilePicture)) }
        }
    }

    fun logOut() {
        signOutUseCase()
    }
}
