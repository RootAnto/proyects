package com.example.pokectcollection.domain.auth.model

/**
 * [AuthResponse] Represents the response of an authentication attempt.
 *
 * @property isAuthSuccessful Indicates whether the authentication was successful.
 * @property errorMessage The error message if the authentication failed, null otherwise.
 */
data class AuthResponse(
    val isAuthSuccessful: Boolean,
    val errorMessage: String?
) {
    companion object {
        /**
         * Provides an empty [AuthResponse] indicating a failed authentication with no error message.
         *
         * @return An [AuthResponse] with `isAuthSuccessful` set to false and `errorMessage` set to null.
         */
        fun getEmptyResponse(): AuthResponse = AuthResponse(
            isAuthSuccessful = false,
            errorMessage = null
        )
    }
}
