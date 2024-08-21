package com.example.pokectcollection.ui.topbar.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import coil.compose.AsyncImage
import com.example.pokectcollection.R
import com.example.pokectcollection.ui.topbar.TopBarViewModel

/**
 * Composable function that displays a top menu bar with navigation options.
 *
 * @param topBarViewModel The ViewModel associated with the top bar.
 * @param title The title to be displayed in the top bar.
 * @param onUserImageClick Lambda function to handle user image click.
 * @param onSetListClick Lambda function to handle set list click.
 * @param onLogOut Lambda function to handle logout click.
 * @param onCreateListClick Lambda function to handle create list click.
 * @param onCheckListCardClick Lambda function to handle check list card click.
 * @param isBackEnabled Flag to indicate if the back button is enabled.
 * @param onClickBack Lambda function to handle back button click.
 * @param onClickAchievement Lambda function to handle achievements click.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopMenuBar(
    topBarViewModel: TopBarViewModel = hiltViewModel(),
    title: String,
    onUserImageClick: () -> Unit,
    onSetListClick: () -> Unit,
    onLogOut: () -> Unit,
    onCreateListClick: () -> Unit,
    onCheckListCardClick: () -> Unit,
    isBackEnabled: Boolean = true,
    onClickBack: () -> Unit,
) {
    val uiState by topBarViewModel.uiState.collectAsState()
    var menuExpanded by remember { mutableStateOf(false) }

    LifecycleResumeEffect(topBarViewModel) {
        topBarViewModel.getProfilePicture()
        onPauseOrDispose { }
    }

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        navigationIcon = {
            if (isBackEnabled) {
                Image(
                    painter = painterResource(id = R.drawable.flecha_hacia_atras),
                    contentDescription = stringResource(R.string.flecha_para_retroceder_volver_a_la_lista_de_cartas),
                    modifier = Modifier
                        .size(35.dp)
                        .clickable { onClickBack() }
                )
            }
        },
        title = {
            Text(
                title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        actions = {
            IconButton(onClick = { menuExpanded = !menuExpanded }) {
                AsyncImage(
                    model = when(uiState.profilePictureUrl) {
                        "Ash" -> R.drawable.ash
                        "Misty" -> R.drawable.misty
                        "Brock" -> R.drawable.brock
                        "Pikachu" -> R.drawable.pikachu
                        "Gary" -> R.drawable.gary
                        "Profeok" -> R.drawable.profeok
                        "Bulbasur" -> R.drawable.bulba
                        "Charmander" -> R.drawable.char1
                        "Squirtle" -> R.drawable.squirtle
                        "Jessie" -> R.drawable.jesy
                        "James" -> R.drawable.james
                        "Selena" -> R.drawable.selena
                        "Aura" -> R.drawable.aura
                        "Mega Blaziken" -> R.drawable.megablazi
                        "Mega Gengar" -> R.drawable.megagengar
                        "Mega Gengar Shiny" -> R.drawable.megagengo
                        "Mew" -> R.drawable.mew1
                        "Groudon" -> R.drawable.groudon
                        "Slowpoke" -> R.drawable.slowpoke
                        "Kyogre" -> R.drawable.kyogre
                        "Bruxish" -> R.drawable.bruxish
                        "Zapdos" -> R.drawable.zapdos
                        "Articuno" -> R.drawable.articuno
                        else -> uiState.profilePictureUrl
                    },
                    contentDescription = "Foto del usuario",
                    modifier = Modifier
                        .size(32.dp)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                )
            }


            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = {
                        Row {
                            Image(
                                painter = painterResource(id = R.drawable.perfil),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = stringResource(id = R.string.perfil))
                        }
                    },
                    onClick = {
                        menuExpanded = false
                        onUserImageClick()
                    }
                )
                DropdownMenuItem(
                    text = {
                        Row {
                            Image(
                                painter = painterResource(id = R.drawable.evaluacion),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = stringResource(id = R.string.lista_de_sets))
                        }
                    },
                    onClick = {
                        menuExpanded = false
                        onSetListClick()
                    }
                )
                DropdownMenuItem(
                    text = {
                        Row {
                            Image(
                                painter = painterResource(id = R.drawable.lista_de_quehaceres),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = stringResource(id = R.string.crear_lista))
                        }
                    },
                    onClick = {
                        menuExpanded = false
                        onCreateListClick()
                    }
                )
                DropdownMenuItem(
                    text = {
                        Row {
                            Image(
                                painter = painterResource(id = R.drawable.lista),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = stringResource(id = R.string.ver_listas))
                        }
                    },
                    onClick = {
                        menuExpanded = false
                        onCheckListCardClick()
                    }
                )
                DropdownMenuItem(
                    text = {
                        Row {
                            Image(
                                painter = painterResource(id = R.drawable.salida),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = stringResource(id = R.string.cerrar_sesion))
                        }
                    },
                    onClick = {
                        menuExpanded = false
                        topBarViewModel.signOut()
                        onLogOut()
                    }
                )
            }
        }
    )
}

/**
 * Preview function to display the top menu bar in Android Studio.
 */
@Preview(showBackground = true)
@Composable
fun PreviewTopMenuBar() {
    TopMenuBar(
        title = "Example",
        onUserImageClick = {},
        onSetListClick = {},
        onCreateListClick = {},
        onLogOut = {},
        onCheckListCardClick = {},
        onClickBack = {},
    )
}
