@file:Suppress("TooManyFunctions")

package com.example.pokectcollection.ui.register.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pokectcollection.R
import com.example.pokectcollection.ui.register.RegisterViewModel

/**
 * Composable function to display the registration screen.
 *
 * @param viewModel The [RegisterViewModel] instance used for handling registration logic.
 * @param onClickBack Lambda function to handle back button click.
 * @param onRegisterSuccessful Lambda function to handle successful registration.
 */
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onClickBack: () -> Unit,
    onRegisterSuccessful: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSignUpSuccessful) {
        if (uiState.isSignUpSuccessful != null && uiState.isSignUpSuccessful == true) {
            onRegisterSuccessful()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background image for the screen
        Image(
            painter = painterResource(id = R.drawable.fondoregistro),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.4f),
            contentScale = ContentScale.FillBounds
        )

        // Centered content of the screen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            RegistrationScreenDesign(viewModel, onClickBack)
        }
    }
}

/**
 * Composable function to display the design of the registration screen.
 *
 * @param viewModel The [RegisterViewModel] instance used for handling registration logic.
 * @param onClickBack Lambda function to handle back button click.
 */
@Suppress("CyclomaticComplexMethod")
@Composable
fun RegistrationScreenDesign(
    viewModel: RegisterViewModel,
    onClickBack: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var usernameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    // Validation functions
    fun validateUsername(username: String) {
        usernameError = when {
            username.isEmpty() -> null
            else -> null
        }
    }

    fun validateEmail(email: String) {
        emailError = when {
            email.isEmpty() -> null
            android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> null
            else -> "Formato de correo inválido"
        }
    }

    fun validatePassword(password: String) {
        passwordError = when {
            password.isEmpty() -> null
            password.length >= 8 && password.any { it.isDigit() } -> null
            else -> "La contraseña debe tener al menos 8 caracteres y un número"
        }
    }

    fun validateConfirmPassword(confirmPassword: String) {
        confirmPasswordError = when {
            confirmPassword.isEmpty() -> null
            confirmPassword == password -> null
            else -> "Las contraseñas no coinciden"
        }
    }

    // Real-time validation calls
    LaunchedEffect(username) { validateUsername(username) }
    LaunchedEffect(email) { validateEmail(email) }
    LaunchedEffect(password) { validatePassword(password) }
    LaunchedEffect(confirmPassword) { validateConfirmPassword(confirmPassword) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Username field
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                validateUsername(it)
            },
            isError = usernameError != null,
            label = { Text(stringResource(R.string.nombre_publico)) },
            placeholder = { Text(stringResource(R.string.escribe_tu_nombre_publico)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        usernameError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        Spacer(Modifier.height(8.dp))

        // Email field
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                validateEmail(it)
            },
            isError = emailError != null,
            label = { Text(stringResource(R.string.email_label)) },
            placeholder = { Text(stringResource(R.string.escribe_tu_email)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        emailError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        Spacer(Modifier.height(8.dp))

        // Password field
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                validatePassword(it)
            },
            isError = passwordError != null,
            label = { Text(stringResource(R.string.contraseña)) },
            placeholder = { Text(stringResource(R.string.crea_una_contraseña)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        passwordError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        Spacer(Modifier.height(8.dp))

        // Confirm Password field
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                validateConfirmPassword(it)
            },
            isError = confirmPasswordError != null,
            label = { Text(stringResource(R.string.repetir_contraseña)) },
            placeholder = { Text(stringResource(R.string.repite_tu_contraseña)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        confirmPasswordError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        Spacer(Modifier.height(16.dp))

        // Register button
        Button(
            onClick = {
                viewModel.signUpUser(username, password, email)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = username.isNotEmpty() &&
                    usernameError == null && emailError == null &&
                    passwordError == null && confirmPasswordError == null
        ) {
            Text(stringResource(R.string.registrar))
        }

        // Back button
        Button(
            onClick = {
                onClickBack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.volver))
        }
    }
}
