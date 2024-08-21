package com.example.pokectcollection.ui.login.model

import com.example.pokectcollection.domain.auth.model.AuthResponse

/**
 * Converts [AuthResponse] to [LoginUiState].
 *
 * Extension function that maps an [AuthResponse] domain model to a [LoginUiState] which is used
 * for representing the UI state during the login process.
 *
 * @return an instance of [LoginUiState] with properties mapped from the [AuthResponse] instance.
 */
fun AuthResponse.toLoginUiState() = LoginUiState(
    isSignInSuccessful = isAuthSuccessful,
    signInError = errorMessage
)
