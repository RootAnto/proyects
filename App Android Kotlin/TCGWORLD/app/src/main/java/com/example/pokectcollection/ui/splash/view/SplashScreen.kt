package com.example.pokectcollection.ui.splash.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pokectcollection.R
import com.example.pokectcollection.ui.splash.SplashViewModel
import kotlinx.coroutines.delay

/**
 * Displays a splash screen with a logo and a progress indicator.
 *
 * @param viewModel The [SplashViewModel] that provides the splash screen logic.
 * @param onSplashFinished A lambda function to be called when the splash screen finishes.
 */
@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onSplashFinished: () -> Unit,
) {
    var text by remember { mutableStateOf("Cargando recursos...") }

    LaunchedEffect(key1 = true) {
        delay(4000)
        text = "Verificando datos del usuario..."
        delay(3000)
        onSplashFinished()
    }

    // Content of the Splash Screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier
                .size(350.dp)
                .weight(1f),
            painter = painterResource(id = R.drawable.dise_o_sin_t_tulo),
            contentDescription = "Logo on the SplashScreen"
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.1f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        IndeterminateCircularIndicator(modifier = Modifier.weight(0.2f))
    }
}

/**
 * Displays an indeterminate circular progress indicator.
 *
 * @param modifier The modifier to be applied to the [CircularProgressIndicator].
 */
@Composable
fun IndeterminateCircularIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier.width(64.dp),
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
}

/**
 * Preview function to display the [SplashScreen] in Android Studio.
 */
@Composable
@Preview(showBackground = true)
fun SplashScreenPreView() {
    SplashScreen(onSplashFinished = {})
}
