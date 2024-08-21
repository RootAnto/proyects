package com.example.pokectcollection.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokectcollection.R
import com.example.pokectcollection.domain.auth.usecase.SignUpWithEmailAndPasswordUseCase
import com.example.pokectcollection.domain.user.model.User
import com.example.pokectcollection.ui.register.model.RegisterUiState
import com.example.pokectcollection.ui.register.model.toRegisterUiState
import com.example.pokectcollection.ui.register.view.RegisterScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * ViewModel [RegisterViewModel] for [RegisterScreen].
 *
 * @property signUpWithEmailAndPasswordUseCase Use case for signing up a user with email and password.
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val signUpWithEmailAndPasswordUseCase: SignUpWithEmailAndPasswordUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<RegisterUiState> = MutableStateFlow(RegisterUiState.getEmptyState())

    /**
     * StateFlow to be collected in the Register screen to observe the registration state.
     */
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    /**
     * Function to sign up a user with the provided name, password, and email.
     *
     * @param name the name of the user.
     * @param password the password of the user.
     * @param email the email of the user.
     */
    fun signUpUser(name: String, password: String, email: String) {
        signUpWithEmailAndPasswordUseCase(
            user = User(name, email, R.drawable.misty.toString()),
            password = password
        ).onEach { authResponse ->
            _uiState.value = authResponse.toRegisterUiState()
        }.launchIn(viewModelScope)
    }
}
