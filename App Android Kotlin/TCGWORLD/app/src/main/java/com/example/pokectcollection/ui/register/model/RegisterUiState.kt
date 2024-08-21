package com.example.pokectcollection.ui.register.model

/**
 * Data class [RegisterUiState] representing the UI state for the registration process.
 *
 * @property isSignUpSuccessful indicates whether the sign-up process was successful.
 * @property errorMessage contains the error message if the sign-up process failed.
 */
data class RegisterUiState(
    val isSignUpSuccessful: Boolean?,
    val errorMessage: String?
) {
    companion object {

        /**
         * Returns an empty state of [RegisterUiState].
         *
         * @return an instance of [RegisterUiState] with null properties.
         */
        fun getEmptyState(): RegisterUiState = RegisterUiState(
            isSignUpSuccessful = null,
            errorMessage = null
        )
    }
}
