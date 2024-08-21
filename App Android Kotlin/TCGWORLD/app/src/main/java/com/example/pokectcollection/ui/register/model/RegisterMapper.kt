package com.example.pokectcollection.ui.register.model

import com.example.pokectcollection.domain.auth.model.AuthResponse

/**
 * Extension function to convert [AuthResponse] to [RegisterUiState].
 *
 * This function maps the properties of an [AuthResponse] instance to a [RegisterUiState] instance.
 *
 * @return an instance of [RegisterUiState] with the mapped properties.
 */
fun AuthResponse.toRegisterUiState() = RegisterUiState(
    isSignUpSuccessful = isAuthSuccessful,
    errorMessage = errorMessage
)
