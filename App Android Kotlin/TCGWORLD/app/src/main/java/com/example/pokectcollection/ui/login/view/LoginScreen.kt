package com.example.pokectcollection.ui.login.view

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pokectcollection.R
import com.example.pokectcollection.ui.login.GoogleAuthUiClient
import com.example.pokectcollection.ui.login.LoginViewModel
import com.google.android.gms.auth.api.identity.Identity

/**
 * Composable function [LoginScreen] for displaying the login screen.
 *
 * @param viewModel the [LoginViewModel] instance.
 * @param onLoginSuccessful callback for when the login is successful.
 * @param onRegisterClick callback for navigating to the register screen.
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccessful: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val googleState by viewModel.googleState.collectAsState()

    LaunchedEffect(uiState.isSignInSuccessful) {
        if (uiState.isSignInSuccessful != null && uiState.isSignInSuccessful == true) {
            onLoginSuccessful()
            viewModel.resetLoginStatus()
        }
    }

    LaunchedEffect(googleState.isSignInSuccessful) {
        if (googleState.isSignInSuccessful) {
            Toast.makeText(
                context,
                "Sign in successful",
                Toast.LENGTH_LONG
            ).show()
            onLoginSuccessful()
            viewModel.resetState()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background image for the screen
        Image(
            painter = painterResource(id = R.drawable.f3),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize() // Image fills the entire Box
                .alpha(0.4f),
            contentScale = ContentScale.FillBounds // Scale the image to fill the screen
        )

        // Centered content of the screen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center,
        ) {
            // Calls the Login function to display the login components
            Login(
                uiState.isSignInSuccessful ?: true,
                viewModel,
                context,
                onRegisterClick
            )
        }
    }
}

/**
 * Composable function for displaying the login form.
 *
 * @param isLoginSuccessful indicates whether the login attempt was successful.
 * @param viewModel the [LoginViewModel] instance.
 * @param context the context used for displaying toast messages.
 * @param onRegisterClick callback for navigating to the register screen.
 */
@Composable
fun Login(
    isLoginSuccessful: Boolean,
    viewModel: LoginViewModel,
    context: Context,
    onRegisterClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    var pass by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    // Main column containing all the login components
    Column(
        modifier = Modifier
            .verticalScroll(scrollState) // Allows vertical scrolling if the content is large
            .padding(16.dp) // Padding around the column
    ) {
        // Centered header image
        HeaderImagen(Modifier.align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.height(16.dp)) // Space between the image and the next component

        // Email text field
        OutlinedTextField(
            value = email,
            label = { Text(text = stringResource(R.string.user)) },
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(), // Field occupies the entire available width
            placeholder = { Text(text = stringResource(R.string.user)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            maxLines = 1,
            isError = !isLoginSuccessful // Show error if login is not successful
        )

        Spacer(modifier = Modifier.height(8.dp)) // Space between text fields

        // Password text field
        OutlinedTextField(
            value = pass,
            onValueChange = { pass = it },
            label = { Text(text = stringResource(R.string.password)) },
            placeholder = { Text(text = stringResource(R.string.password)) },
            singleLine = true,
            maxLines = 1,
            isError = !isLoginSuccessful,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(), // Field occupies the entire available width
            supportingText = {
                if (!isLoginSuccessful) {
                    // Supporting text showing the error
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.password_incorrecta),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp)) // Space between text fields and the next component

        // Clickable "Forgot Password" text
        ForgotPassword(Modifier.align(Alignment.End))

        Spacer(modifier = Modifier.height(16.dp)) // Space before social login buttons

        // Social login buttons
        LoginSocial(viewModel = viewModel, context = context)

        Spacer(modifier = Modifier.height(16.dp)) // Space before the login button

        // Login button
        Button(
            onClick = { viewModel.loginUser(email, pass) },
            modifier = Modifier
                .fillMaxWidth() // Button occupies the entire available width
                .height(48.dp) // Button height
        ) {
            Text(text = stringResource(R.string.iniciar_sesion))
        }

        Spacer(modifier = Modifier.height(8.dp)) // Space before the register button

        // Register button
        Button(
            onClick = { onRegisterClick() },
            modifier = Modifier
                .fillMaxWidth() // Button occupies the entire available width
                .height(48.dp) // Button height
        ) {
            Text(text = stringResource(R.string.registro))
        }
    }
}

/**
 * Composable function for displaying the header image.
 *
 * @param modifier the modifier to be applied to the image.
 */
@Composable
fun HeaderImagen(modifier: Modifier) {
    // Header image
    Image(
        painter = painterResource(id = R.drawable.dise_o_sin_t_tulo),
        contentDescription = "Logo app",
        modifier = modifier.size(200.dp) // Image size
    )
}

/**
 * Composable function for displaying the "Forgot Password" text.
 *
 * @param modifier the modifier to be applied to the text.
 */
@Composable
fun ForgotPassword(modifier: Modifier) {
    val context = LocalContext.current

    // Clickable "Forgot Password" text
    Text(
        text = stringResource(R.string.olvidaste_la_password),
        modifier = modifier.clickable {
            Toast.makeText(
                context,
                "En desarrollo",
                Toast.LENGTH_LONG
            ).show()
        },
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,

    )
}

/**
 * Composable function for displaying social login buttons.
 *
 * @param viewModel the [LoginViewModel] instance.
 * @param context the context used for displaying toast messages.
 */
@Composable
fun LoginSocial(
    viewModel: LoginViewModel,
    context: Context
) {
    val googleClient = GoogleAuthUiClient(
        context = context,
        oneTapClient = Identity.getSignInClient(context)
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.googleLaunch(googleClient, result)
            }
        }
    )

    // Row for social login icons
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center // Centers the icons horizontally
    ) {
        // Google button
        Image(
            painter = painterResource(id = R.drawable.google),
            contentDescription = stringResource(R.string.inicio_con_red_social_google),
            modifier = Modifier
                .size(60.dp) // Icon size
                .padding(5.dp) // Padding around the icon
                .clickable {
                    viewModel.googleLogin(googleClient, launcher)
                }
        )

        // Facebook button
        Image(
            painter = painterResource(id = R.drawable.facebook),
            contentDescription = stringResource(R.string.inicio_con_red_social_facebook),
            modifier = Modifier
                .size(60.dp) // Icon size
                .padding(5.dp) // Padding around the icon
                .clickable {
                    Toast.makeText(
                        context,
                        "En desarrollo",
                        Toast.LENGTH_LONG
                    ).show()
                }

        )

        // Instagram button
        Image(
            painter = painterResource(id = R.drawable.instagram),
            contentDescription = stringResource(R.string.inicio_con_red_social_instagram),
            modifier = Modifier
                .size(60.dp) // Icon size
                .padding(5.dp) // Padding around the icon
                .clickable {
                    Toast.makeText(
                        context,
                        "En desarrollo",
                        Toast.LENGTH_LONG
                    ).show()
                }
        )
    }
}
