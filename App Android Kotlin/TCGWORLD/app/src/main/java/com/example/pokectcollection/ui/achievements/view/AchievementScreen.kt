package com.example.pokectcollection.ui.achievements.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pokectcollection.R
import com.example.pokectcollection.ui.achievements.AchievementsViewModel
import com.example.pokectcollection.ui.topbar.view.TopMenuBar
import kotlinx.coroutines.delay

/**
 * Composable function for the Achievement screen.
 *
 * @param viewModel the [AchievementsViewModel] instance.
 * @param onProfileClick callback for navigating to the profile screen.
 * @param onSetListClick callback for navigating to the set list screen.
 * @param onCreateListClick callback for navigating to the create list screen.
 * @param onCheckListCardClick callback for navigating to the check list card screen.
 * @param onClickBack callback for navigating back to the previous screen.
 * @param onClickAchievements callback for navigating to the achievements screen.
 * @param onLogOut callback for logging out.
 */
@Composable
fun ArchievementScreen(
    viewModel: AchievementsViewModel = hiltViewModel(),
    onProfileClick: () -> Unit,
    onSetListClick: () -> Unit,
    onCreateListClick: () -> Unit,
    onCheckListCardClick: () -> Unit,
    onClickBack: () -> Unit,
    onLogOut: () -> Unit

) {
    TopMenuBar(
        title = stringResource(R.string.lista_de_cartas),
        onUserImageClick = {
            // Acción para la imagen de usuario
            onProfileClick()
        },
        onSetListClick = {
            // Acción para la lista de sets
            onSetListClick()
        },
        onCreateListClick = {
            // Acción para crear una lista
            onCreateListClick()
        },
        onCheckListCardClick = {
            onCheckListCardClick()
        },
        onLogOut = {
            viewModel.logOut()
            onLogOut()
        },
        onClickBack = {
            onClickBack()
        },
    )
}



@Composable
fun LogroList(
    viewModel: AchievementsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Asegúrate de llamar a getLogros para obtener los datos
    LaunchedEffect(Unit) {
        Log.d("LogroList", "Llamando a getLogros")
    }

    Column {
        Log.d("LogroList", "Estado actual: ${uiState.name}")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.energy_fighting), // Usa un recurso de imagen apropiado
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row {
                    Text(
                        text = "Nombre: ${uiState.name}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = uiState.name,
                        fontSize = 14.sp
                    )
                }
                Spacer(Modifier.height(4.dp))
                Row {
                    Text(
                        text = "Fecha: ${uiState.date}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = uiState.date, // Assuming `uiState.id` represents the date
                        fontSize = 14.sp
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Descripción: ${uiState.description}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = uiState.description,
                    fontSize = 14.sp
                )
            }
        }
    }
}

