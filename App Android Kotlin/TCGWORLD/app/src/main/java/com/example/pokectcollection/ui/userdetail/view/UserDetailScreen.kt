package com.example.pokectcollection.ui.userdetail.view

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.pokectcollection.R
import com.example.pokectcollection.ui.userdetail.UserDetailViewModel


/**
 * Composable function [UserDetailScreen] that displays the user detail screen.
 *
 * @param viewModel The [UserDetailViewModel] that provides the user data.
 * @param onProfileClick Lambda function to handle profile click.
 * @param onSetListClick Lambda function to handle set list click.
 * @param onLogOut Lambda function to handle logout.
 * @param onCreateListClick Lambda function to handle create list click.
 * @param onClickBack Lambda function to handle back click.
 * @param onCheckListCardClick Lambda function to handle check list card click.
 */
@Composable
fun UserDetailScreen(
    viewModel: UserDetailViewModel = hiltViewModel(),
    onProfileClick: () -> Unit,
    onSetListClick: () -> Unit,
    onLogOut: () -> Unit,
    onCreateListClick: () -> Unit,
    onClickBack: () -> Unit,
    onCheckListCardClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    var menuExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.captura_de_pantalla_2024_04_30_134600),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.3f),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .verticalScroll(scrollState), // Añadir scroll vertical
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Fila con flecha hacia atrás e icono de menú
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.flecha_hacia_atras),
                    contentDescription = stringResource(R.string.flecha_para_retroceder_volver_a_la_lista_de_cartas),
                    modifier = Modifier
                        .size(35.dp)
                        .clickable { onClickBack() }
                )

                Box {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menú"
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
                                        painter = painterResource(id = R.drawable.evaluacion),
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    (Text(text = stringResource(id = R.string.lista_de_sets)))
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
                                    Text(stringResource(id = R.string.ver_listas))
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
                                viewModel.logOut()
                                onLogOut()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Spacer(modifier = Modifier.height(8.dp))

            // Imagen de usuario
            UserProfileImagePicker(
                userEmail = uiState.user.email,
                viewModel = viewModel,
                profilePicture = uiState.user.profilePictureUrl,
                selectedImageResId = R.drawable.ash // Valor por defecto si es null
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre de usuario
            Text(
                text = uiState.user.name,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = uiState.user.email,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(30.dp))

            Column {
                // Tarjetas de información
                InfoCard(
                    icon = R.drawable.fecha_de_entrega,
                    title =  stringResource(id = R.string.fecha_de_ingreso),
                    value = uiState.joinDate
                )

                Spacer(modifier = Modifier.height(4.dp))

                InfoCard(
                    icon = R.drawable._carta,
                    title = stringResource(id = R.string.sets_completados),
                    value = uiState.completedSets.toString()
                )

                Spacer(modifier = Modifier.height(4.dp))

                InfoCard(
                    icon = R.drawable.reverse_card,
                    title = stringResource(id = R.string.carta_conseguida),
                    value = uiState.cardsObtained.toString()
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Tarjeta de medallas
                MedalsCard(
                    medal = listOf(
                        R.drawable.logro1,
                        R.drawable.logro2jhoto,
                        R.drawable.logro3,
                    ),
                    uiState.user.achievementList
                )
            }
        }
    }
}

/**
 * Composable function [MedalsCard] that displays a card with medals.
 *
 * @param medal List of medal resource IDs.
 * @param logros List of achievements obtained by the user.
 */
@Composable
fun MedalsCard(medal: List<Int>, logros: List<String>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.medallas),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                medal.forEachIndexed { index, medal ->
                    val mensaje = when (index) {
                        0 -> stringResource(R.string.registra_tu_primera_carta)
                        1 -> stringResource(R.string.crea_tu_primera_lista_de_cartas)
                        else -> stringResource(R.string.completa_tu_primer_set)
                    }
                    val conseguido = when (index) {
                        0 -> "First card" in logros
                        1 -> "First card" !in logros && "First custom list" in logros
                        else -> "First set completed" in logros
                    }
                    logroImagen(image = medal, conseguido = conseguido, mensaje = mensaje)
                }
            }
        }
    }
}

/**
 * Composable function [logroImagen] that displays an image of an achievement.
 *
 * @param image The resource ID of the achievement image.
 * @param conseguido Boolean flag indicating if the achievement is obtained.
 * @param mensaje Custom message to display when the image is clicked.
 */
@Composable
fun logroImagen(image: Int, conseguido: Boolean, mensaje: String) {
    val context = LocalContext.current
    Image(
        painter = painterResource(id = image),
        contentDescription = null,
        modifier = Modifier
            .size(60.dp)
            .padding(4.dp)
            .alpha(if (conseguido) 1f else 0.2f)
            .clickable {
                Toast
                    .makeText(context, mensaje, Toast.LENGTH_SHORT)
                    .show()
            }
    )
}

/**
 * Composable function [UserProfileImagePicker] that allows the user to pick a profile image.
 *
 * @param userEmail The email of the user.
 * @param viewModel The [UserDetailViewModel] that provides the user data.
 * @param profilePicture The URL of the current profile picture.
 * @param selectedImageResId The resource ID of the selected image.
 */
@Composable
fun UserProfileImagePicker(
    userEmail: String,
    viewModel: UserDetailViewModel,
    profilePicture: String,
    selectedImageResId: Int
) {
    var selectedImage by remember { mutableIntStateOf(selectedImageResId) }
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .size(90.dp)
            .clip(CircleShape)
            .background(Color.LightGray)
            .clickable { showDialog = true },
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = selectedImage),
                contentDescription = null,
                modifier = Modifier.size(90.dp),
                contentScale = ContentScale.Crop
            )
        }
        AsyncImage(
            model = when(profilePicture) {
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
                else -> profilePicture
            },
            contentDescription = null,
            modifier = Modifier.size(90.dp),
            contentScale = ContentScale.Crop
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(id = R.string.selecciona_foto_de_perfil)) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    ImagePickerItem(R.drawable.ash, "Ash") { newImage ->
                        selectedImage = newImage
                        viewModel.updateUserImage("Ash")
                        showDialog = false
                    }
                    ImagePickerItem(R.drawable.misty, "Misty") { newImage ->
                        selectedImage = newImage
                        viewModel.updateUserImage("Misty")
                        showDialog = false
                    }

                    ImagePickerItem(R.drawable.brock, "Brock") { newImage ->
                        selectedImage = newImage
                        viewModel.updateUserImage("Brock")
                        showDialog = false
                    }
                    ImagePickerItem(R.drawable.pikachu, "Pikachu") { newImage ->
                        selectedImage = newImage
                        viewModel.updateUserImage("Pikachu")
                        showDialog = false
                    }
                    ImagePickerItem(R.drawable.gary, "Gary") { newImage ->
                        selectedImage = newImage
                        viewModel.updateUserImage("Gary")
                        showDialog = false
                    }
                    ImagePickerItem(R.drawable.profeok, "Profesor ok") { newImage ->
                        selectedImage = newImage
                        viewModel.updateUserImage("Profeok")
                        showDialog = false
                    }
                    ImagePickerItem(R.drawable.bulba, "Bulbasur") { newImage ->
                        selectedImage = newImage
                        viewModel.updateUserImage("Bulbasur")
                        showDialog = false
                    }
                    ImagePickerItem(R.drawable.char1, "Charmander") { newImage ->
                        selectedImage = newImage
                        viewModel.updateUserImage("Charmander")
                        showDialog = false
                    }
                    ImagePickerItem(R.drawable.squirtle, "Squirtle") { newImage ->
                        selectedImage = newImage
                        viewModel.updateUserImage("Squirtle")
                        showDialog = false
                    }
                    ImagePickerItem(R.drawable.jesy, "Jessie") { newImage ->
                        selectedImage = newImage
                        viewModel.updateUserImage("Jessie")
                        showDialog = false
                    }
                    ImagePickerItem(R.drawable.james, "James") { newImage ->
                        selectedImage = newImage
                        viewModel.updateUserImage("James")
                        showDialog = false
                    }
                    ImagePickerItem(R.drawable.selena, "Selena") { newImage ->
                        selectedImage = newImage
                        viewModel.updateUserImage("Selena")
                        showDialog = false
                    }
                    ImagePickerItem(R.drawable.aura, "Aura") { newImage ->
                        selectedImage = newImage
                        viewModel.updateUserImage("Aura")
                        showDialog = false
                    }
                    ImagePickerItem(R.drawable.megablazi, "Mega Blaziken") { newImage ->
                        selectedImage = newImage
                        viewModel.updateUserImage("Mega Blaziken")
                        showDialog = false
                    }
                    ImagePickerItem(R.drawable.megagengar, "Mega Gengar") { newImage ->
                        selectedImage = newImage
                        viewModel.updateUserImage("Mega Gengar")
                        showDialog = false
                    }
                    ImagePickerItem(R.drawable.megagengo, "Mega Gengar Shiny") { newImage ->
                        selectedImage = newImage
                        viewModel.updateUserImage("Mega Gengar Shiny")
                        showDialog = false
                    }
                    ImagePickerItem(R.drawable.mew1, "Mew") { newImage ->
                        selectedImage = newImage
                        viewModel.updateUserImage("Mew")
                        showDialog = false
                    }
                    ImagePickerItem(R.drawable.groudon, "Groudon") { newImage ->
                        selectedImage = newImage
                        viewModel.updateUserImage("Groudon")
                        showDialog = false
                    }
                    ImagePickerItem(R.drawable.slowpoke, "Slowpoke") { newImage ->
                        selectedImage = newImage
                        viewModel.updateUserImage("Slowpoke")
                        showDialog = false
                    }
                    ImagePickerItem(R.drawable.kyogre, "Kyogre") { newImage ->
                        selectedImage = newImage
                        viewModel.updateUserImage("Kyogre")
                        showDialog = false
                    }
                    ImagePickerItem(R.drawable.bruxish, "Bruxish") { newImage ->
                        selectedImage = newImage
                        viewModel.updateUserImage("Bruxish")
                        showDialog = false
                    }
                    ImagePickerItem(R.drawable.zapdos, "Zapdos") { newImage ->
                        selectedImage = newImage
                        viewModel.updateUserImage("Zapdos")
                        showDialog = false
                    }
                    ImagePickerItem(R.drawable.articuno, "Articuno") { newImage ->
                        selectedImage = newImage
                        viewModel.updateUserImage("Articuno")
                        showDialog = false
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(id = R.string.cancelar))
                }
            }
        )
    }
}


/**
 * Composable function [ImagePickerItem] that displays an image picker item.
 *
 * @param imageRes The resource ID of the image.
 * @param imageName The name of the image.
 * @param onImageSelected Lambda function to handle image selection.
 */
@Composable
fun ImagePickerItem(@DrawableRes imageRes: Int, imageName: String, onImageSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onImageSelected(imageRes) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = imageName)
    }
}

@Composable
fun InfoCard(
    @DrawableRes icon: Int,
    title: String,
    value: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(100.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.fillMaxWidth() // Asegura que el texto ocupe todo el ancho disponible
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        color = Color.Gray
                    ),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.fillMaxWidth() // Asegura que el texto ocupe todo el ancho disponible
                )
            }
        }
    }
}
