package com.example.pokectcollection.ui.login.model

/**
 * Data class [SignInState] representing the state of a sign-in attempt.
 *
 * @property isSignInSuccessful indicates whether the sign-in process was successful.
 * @property signInError contains the error message if the sign-in process failed.
 */
data class SignInState(
    val isSignInSuccessful: Boolean,
    val signInError: String?
) {
    companion object {

        /**
         * Returns an empty state of [SignInState].
         *
         * @return an instance of [SignInState] with default values.
         */
        fun getEmptyState() = SignInState(
            isSignInSuccessful = false,
            signInError = null
        )
    }
}
