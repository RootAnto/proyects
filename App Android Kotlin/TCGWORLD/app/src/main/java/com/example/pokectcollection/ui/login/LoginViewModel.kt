package com.example.pokectcollection.ui.login

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokectcollection.domain.auth.usecase.GetCurrentUserUseCase
import com.example.pokectcollection.domain.auth.usecase.LoginWithEmailAndPasswordUseCase
import com.example.pokectcollection.domain.user.usecase.CreateUserDocUseCase
import com.example.pokectcollection.ui.login.model.LoginUiState
import com.example.pokectcollection.ui.login.model.SignInResult
import com.example.pokectcollection.ui.login.model.SignInState
import com.example.pokectcollection.ui.login.model.UserData
import com.example.pokectcollection.ui.login.model.toLoginUiState
import com.example.pokectcollection.ui.login.view.LoginScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
/**
 * The ViewModel for the [LoginScreen].
 *
 * @param loginWithEmailAndPasswordUseCase the use case for logging in with email and password.
 * @param createUserDocUseCase the use case for creating a user document.
 * @param getCurrentUserUseCase the use case for getting the current user.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginWithEmailAndPasswordUseCase: LoginWithEmailAndPasswordUseCase,
    private val createUserDocUseCase: CreateUserDocUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<LoginUiState> =
        MutableStateFlow(LoginUiState.getEmptyState())

    /**
     * StateFlow to be collected in the UI for updating the login state.
     */
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _googleState = MutableStateFlow(SignInState.getEmptyState())

    /**
     * StateFlow to be collected in the UI for updating the Google sign-in state.
     */
    val googleState = _googleState.asStateFlow()

    /**
     * Initializes the ViewModel and checks for the current user.
     */
    init {
        getCurrentUserUseCase().onEach {
            it?.let {
                _googleState.update {
                    SignInState(
                        isSignInSuccessful = true,
                        signInError = null
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Logs in a user with email and password.
     *
     * @param email the user's email.
     * @param password the user's password.
     */
    fun loginUser(email: String, password: String) {
        loginWithEmailAndPasswordUseCase(email, password)
            .onEach { signInResponse ->
                _uiState.value = signInResponse.toLoginUiState()
            }.launchIn(viewModelScope)
    }

    /**
     * Resets the login status.
     */
    fun resetLoginStatus() {
        _uiState.value = LoginUiState.getEmptyState()
    }

    /**
     * Handles the result of a sign-in attempt.
     *
     * @param result the [SignInResult] of the sign-in attempt.
     */
    private fun onSignInResult(result: SignInResult) {
        if (result.data != null) {
            createUserDocUseCase(
                UserData(
                    result.data.userId,
                    result.data.email,
                    result.data.username,
                    result.data.profilePictureUrl
                )
            )
        }

        _googleState.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    /**
     * Resets the Google sign-in state.
     */
    fun resetState() {
        _googleState.update { SignInState.getEmptyState() }
    }

    /**
     * Launches the Google sign-in process.
     *
     * @param googleAuthUiClient the Google authentication client.
     * @param result the [ActivityResult] containing the sign-in intent.
     */
    fun googleLaunch(googleAuthUiClient: GoogleAuthUiClient, result: ActivityResult) {
        viewModelScope.launch {
            val signInResult = googleAuthUiClient.signInWithIntent(
                intent = result.data ?: return@launch
            )
            onSignInResult(signInResult)
        }
    }

    /**
     * Initiates the Google login process.
     *
     * @param googleAuthUiClient the Google authentication client.
     * @param launcher the [ManagedActivityResultLauncher] for launching the sign-in intent.
     */
    fun googleLogin(
        googleAuthUiClient: GoogleAuthUiClient,
        launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>
    ) {
        viewModelScope.launch {
            val signInIntentSender = googleAuthUiClient.signIn()
            launcher.launch(
                IntentSenderRequest.Builder(
                    signInIntentSender ?: return@launch
                ).build()
            )
        }
    }
}
