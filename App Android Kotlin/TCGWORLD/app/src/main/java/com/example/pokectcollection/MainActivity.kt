package com.example.pokectcollection

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.pokectcollection.ui.achievements.view.AchievementLabel
import com.example.pokectcollection.ui.navigation.PokectCollectionNavigation
import com.example.pokectcollection.ui.theme.PokectCollectionTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay


/**
 * The main activity for the PokectCollection app.
 *
 * This activity sets up the content view using Jetpack Compose and handles displaying
 * achievements to the user.
 *
 * @property viewModel The [MainActivityViewModel] that provides data and handles logic for the UI.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()


    /**
     * Called when the activity is starting. This is where most initialization should go.
     * It sets the content view using Jetpack Compose and observes the UI state from the [viewModel].
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in [onSaveInstanceState].
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.checkUserAchievements()

        setContent {
            val uiState by viewModel.uiState.collectAsState()
            val context = LocalContext.current

            PokectCollectionTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Superficie principal para el contenido de la aplicación
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        PokectCollectionNavigation()
                    }

                    // Mostrar el AchievementLabel si uiState.display es verdadero
                    if (uiState.display) {


                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            AchievementLabel(
                                achievementImageRes = R.drawable.dise_o_sin_t_tulo,
                                achievementName = uiState.achievement.name
                            )
                        }
                       LaunchedEffect( true ) {
                           delay(5000)
                           viewModel.updateState()
                       }

                    }
                }
            }
        }
    }
}

