
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.pokectcollection.R
import com.example.pokectcollection.domain.cards.model.Card
import com.example.pokectcollection.ui.cardlist.view.SearchBar
import com.example.pokectcollection.ui.customcardlist.CustomCardListViewModel
import com.example.pokectcollection.ui.topbar.view.TopMenuBar

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CustomCardList(
    viewModel: CustomCardListViewModel = hiltViewModel(),
    onProfileClick: () -> Unit,
    onSetListClick: () -> Unit,
    onCreateListClick: () -> Unit,
    onCheckListCardClick: () -> Unit,
    onSelectedCardClicked: (String) -> Unit,
    onLogOut: () -> Unit,
    onClickBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    var filterText by remember { mutableStateOf("") }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var showNameChangeDialog by rememberSaveable { mutableStateOf(false) }
    var selectedListName by rememberSaveable { mutableStateOf(0) }
    val listState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            TopMenuBar(
                title = stringResource(id = R.string.crear_lista),
                onUserImageClick = onProfileClick,
                onSetListClick = onSetListClick,
                onCreateListClick = onCreateListClick,
                onCheckListCardClick = onCheckListCardClick,
                onLogOut = onLogOut,
                onClickBack = onClickBack,
            )

            Spacer(modifier = Modifier.height(8.dp))

            SearchBar(
                texto = stringResource(R.string.buscar_carta),
                filterText = filterText,
                onFilterChange = { filterText = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(state = listState) {
                items(uiState.lists.size) { index ->
                    CustomListLabel(
                        cardName = uiState.lists[index].name,
                        onClick = {
                            showDialog = true
                            selectedListName = index
                        },
                        imagePainter = painterResource(id = R.drawable.borrar),
                        onButtonClick = {
                            showNameChangeDialog = true
                            selectedListName = index
                        },
                        onDeleteClick = {
                            viewModel.deleteUserList(uiState.lists[index].name)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        AnimatedVisibility(
            visible = showDialog,
            enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
        ) {
            CustomDialog(
                selectedList = uiState.lists[selectedListName].customList,
                onDismissRequest = { showDialog = false },
                listName = uiState.lists[selectedListName].name,
                onRemoveCard = viewModel::removeCardFromList,
                onCardClicked = { cardId ->
                    onSelectedCardClicked(cardId)
                    // Ensure the dialog remains open when returning
                    showDialog = true
                }
            )
        }

        if (showNameChangeDialog) {
            ChangeNameDialog(
                currentName = uiState.lists[selectedListName].name,
                onDismissRequest = { showNameChangeDialog = false },
                onConfirm = { newName ->
                    viewModel.changeListName(uiState.lists[selectedListName].name, newName)
                    showNameChangeDialog = false
                }
            )
        }
    }
}

@Composable
fun ChangeNameDialog(
    currentName: String,
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var newName by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(stringResource(id = R.string.crear_lista))
        },
        text = {
            Column {
                Text(stringResource(id = R.string.por_favor_ingrese_nombre_de_la_lista))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text(stringResource(id = R.string.nombre_de_la_lista)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(newName) }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancelar")
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun CustomDialog(
    selectedList: List<Card>,
    onDismissRequest: () -> Unit,
    listName: String,
    onRemoveCard: (String, String) -> Unit,
    onCardClicked: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickableWithoutRipple { onDismissRequest() }
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .fillMaxWidth(0.80f) // Ocupa 4/5 del ancho de la pantalla
                .background(
                    MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
                )
                .padding(16.dp)
                .clickableWithoutRipple { }
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = listName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(1f) // Add weight to ensure the text takes up equal space
                            .padding(end = 16.dp)
                    )
                    ElevatedButton(
                        onClick = onDismissRequest,
                        modifier = Modifier
                            .weight(1f) // Add weight to ensure the button takes up equal space
                            .size(height = 36.dp, width = 80.dp)
                    ) {
                        Text(text = stringResource(id = R.string.cerrar), style = MaterialTheme.typography.bodySmall)
                    }
                }

                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(selectedList.sortedBy { it.number.toIntOrNull() ?: Int.MAX_VALUE }) { card ->
                        CardElementLabel(
                            card = card,
                            setIconUrl = card.set.images.symbol,
                            listName = listName,
                            image2 = painterResource(id = R.drawable.borrar),
                            onClickDelete = onRemoveCard,
                            onClickCard = onCardClicked
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun CustomListLabel(
    cardName: String,
    onClick: () -> Unit,
    imagePainter: Painter,
    onButtonClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color = Color(0xFF6200EE), shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = cardName.take(1), // First letter of the card name
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cardName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterEnd
            ) {
                ElevatedButton(
                    onClick = onButtonClick,
                    modifier = Modifier
                        .defaultMinSize(minWidth = 100.dp) // Set a minimum width for the button
                ) {
                    Text(
                        text = stringResource(R.string.nombre),
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Image(
                painter = imagePainter,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onDeleteClick() }
            )
        }
    }
}

@Composable
fun CardElementLabel(
    card: Card,
    setIconUrl: String,
    listName: String,
    image2: Painter,
    onClickDelete: (String, String) -> Unit,
    onClickCard: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
            .clickable { onClickCard(card.id) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = card.number,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(40.dp) // Fija el ancho para el número
        )

        Spacer(modifier = Modifier.width(8.dp))

        AsyncImage(
            model = setIconUrl,
            contentDescription = "Image logo",
            modifier = Modifier
                .size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = card.name,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f) // Ocupa el espacio disponible
        )

        Spacer(modifier = Modifier.width(8.dp))

        Image(
            painter = image2,
            contentDescription = "Imagen para borrar de la lista",
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    onClickDelete(listName, card.id)
                }
        )
    }
}

/**
 * Composable function that makes a clickable element without the ripple when it is clicked.
 */
@Composable
fun Modifier.clickableWithoutRipple(onClick: () -> Unit) = composed {
    val interactionSource = remember { MutableInteractionSource() }
    this.then(
        Modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        )
    )
}
