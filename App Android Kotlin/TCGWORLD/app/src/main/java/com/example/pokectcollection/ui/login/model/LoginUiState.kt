package com.example.pokectcollection.ui.login.model

/**
 * Data class [LoginUiState] representing the UI state during the login process.
 *
 * @property isSignInSuccessful indicates whether the sign-in process was successful.
 * @property signInError contains the error message if the sign-in process failed.
 */
data class LoginUiState(
    val isSignInSuccessful: Boolean?,
    val signInError: String?
) {
    companion object {

        /**
         * Returns an empty state of [LoginUiState].
         *
         * @return an instance of [LoginUiState] with null properties.
         */
        fun getEmptyState(): LoginUiState = LoginUiState(
            isSignInSuccessful = null,
            signInError = null
        )
    }
}
