package com.example.pokectcollection.ui.cardlist.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.pokectcollection.R
import com.example.pokectcollection.ui.cardlist.CardListViewModel
import com.example.pokectcollection.ui.topbar.view.TopMenuBar

/**
 * Composable function for displaying the card list screen.
 *
 * @param viewModel the [CardListViewModel] instance.
 * @param onProfileClick callback for navigating to the profile screen.
 * @param onSetListClick callback for navigating to the set list screen.
 * @param onLogOut callback for logging out.
 * @param onCreateListClick callback for navigating to the create list screen.
 * @param onClickBack callback for navigating back to the previous screen.
 * @param onCheckListCardClick callback for navigating to the check list card screen.
 * @param onClickAchievement callback for navigating to the achievements screen.
 * @param onCardClicked callback for navigating to the card detail screen.
 */
@Composable
fun CardListScreen(
    viewModel: CardListViewModel = hiltViewModel(),
    onProfileClick: () -> Unit,
    onSetListClick: () -> Unit,
    onLogOut: () -> Unit,
    onCreateListClick: () -> Unit,
    onClickBack: () -> Unit,
    onCheckListCardClick: () -> Unit,
    onCardClicked: (String) -> Unit
) {
    // Obtener el estado de la UI desde el ViewModel de la lista de cartas
    val cardListUiState by viewModel.uiState.collectAsState()

    // Estado para el filtro de texto
    var filterText by remember { mutableStateOf("") }

    // Estado para el modo de vista (una o dos columnas)
    var isSingleColumn by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.f3cardlist),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.3f,
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            // Barra de navegación superior
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

            Spacer(modifier = Modifier.height(8.dp))

            // Barra de búsqueda
            SearchBar(
                texto = stringResource(R.string.buscar_carta),
                filterText = filterText,
                onFilterChange = { filterText = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Selector de modo de vista (una o dos columnas)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.modo_de_vista),
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = isSingleColumn,
                        onCheckedChange = { isSingleColumn = it },
                        modifier = Modifier.scale(0.75f)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(onClick = {
                    viewModel.createCustomListWithNotObtainedCard()
                    Toast.makeText(
                        context,
                        context.getString(R.string.lista_creada),
                        Toast.LENGTH_LONG
                    ).show()
                }) {
                    Text(text = stringResource(R.string.crear_lista))
                }
            }

            // Cuadrícula de cartas
            LazyVerticalGrid(
                columns = GridCells.Fixed(if (isSingleColumn) 1 else 2),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filteredCards = cardListUiState.cardList.filter {
                    it.name.contains(filterText, ignoreCase = true) ||
                        it.number.contains(filterText, ignoreCase = true) ||
                        it.rarity.contains(filterText, ignoreCase = true) ||
                        it.artist.contains(filterText, ignoreCase = true)
                }.sortedBy { it.number.toIntOrNull() ?: Int.MAX_VALUE }

                items(filteredCards, key = { it.id }) { card ->
                    if (isSingleColumn) {
                        // Componente de carta individual
                        SingleCardComponent(
                            imageUrl = card.images.small,
                            cardName = card.name,
                            symbolPainter = card.set.images.logo,
                            collectionNumber = card.number,
                            setIconPainter = card.set.images.symbol,
                            quantityOwned = "10",
                            rarityPainter = card.rarity,
                            onClick = {
                                onCardClicked(card.id)
                            }
                        )
                    } else {
                        // Componente de contenido de la carta
                        CardContent(
                            imageUrl = card.images.small,
                            ownedIcon = if (card.owned) {
                                painterResource(id = R.drawable.che2)
                            } else {
                                painterResource(id = R.drawable.xroja)
                            },
                            symbolPainter = painterResource(id = R.drawable.symbol_paldea_fades),
                            collectionNumber = card.number,
                            cardName = card.name,
                            isSingleColumn = isSingleColumn,
                            onCardClick = { onCardClicked(card.id) },
                            onIconClick = { viewModel.updateCardOwnership(card.id) },
                            isOwned = card.owned
                        )
                    }
                }
            }
        }
    }
}

/**
 * Composable function for displaying the search bar.
 *
 * @param texto the placeholder text.
 * @param filterText the current filter text.
 * @param onFilterChange callback for when the filter text changes.
 */
@Composable
fun SearchBar(
    texto: String,
    filterText: String,
    onFilterChange: (String) -> Unit
) {
    TextField(
        value = filterText,
        onValueChange = onFilterChange,
        placeholder = { Text(texto) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

/**
 * Composable function for displaying card content.
 *
 * @param imageUrl the URL of the card image.
 * @param ownedIcon the painter for the owned icon.
 * @param symbolPainter the painter for the symbol icon.
 * @param collectionNumber the collection number of the card.
 * @param cardName the name of the card.
 * @param isSingleColumn whether the view is in single column mode.
 * @param onCardClick callback for when the card is clicked.
 * @param onIconClick callback for when the icon is clicked.
 * @param isOwned whether the card is owned.
 */
@Composable
fun CardContent(
    imageUrl: String,
    ownedIcon: Painter,
    symbolPainter: Painter,
    collectionNumber: String,
    cardName: String,
    isSingleColumn: Boolean,
    onCardClick: () -> Unit,
    onIconClick: () -> Unit,
    isOwned: Boolean
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onCardClick() }
            .fillMaxWidth()
            .alpha(
                if (!isSingleColumn && !isOwned) 0.5f else 1f
            ) // Modificar opacidad solo si no es de una columna y no es owned
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            placeholder = painterResource(R.drawable.reverse_card),
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isSingleColumn) 240.dp else 200.dp)
        )

        Text(
            text = cardName,
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopStart)
                .background(Color.White, shape = RoundedCornerShape(4.dp))
                .padding(4.dp)
        )

        Image(
            painter = ownedIcon,
            contentDescription = stringResource(R.string.icono_clicable),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(35.dp)
                .clickable { onIconClick() }
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .background(Color.White, shape = RoundedCornerShape(4.dp))
                .height(25.dp)
                .width(60.dp)
                .padding(4.dp)
        ) {
            Text(
                text = collectionNumber,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Image(
            painter = symbolPainter,
            contentDescription = "Collection symbol",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .background(Color.White, shape = RoundedCornerShape(4.dp))
                .size(48.dp)
                .padding(4.dp)
        )
    }
}

/**
 * Composable function for displaying a single card component.
 *
 * @param imageUrl the URL of the card image.
 * @param cardName the name of the card.
 * @param symbolPainter the painter for the symbol icon.
 * @param collectionNumber the collection number of the card.
 * @param setIconPainter the painter for the set icon.
 * @param rarityPainter the rarity of the card.
 * @param quantityOwned the quantity of the card owned.
 * @param onClick callback for when the card is clicked.
 */
@Composable
fun SingleCardComponent(
    imageUrl: String,
    cardName: String,
    symbolPainter: String,
    collectionNumber: String,
    setIconPainter: String,
    rarityPainter: String,
    quantityOwned: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            placeholder = painterResource(R.drawable.reverse_card),
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = cardName,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.numero, collectionNumber),
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(4.dp))

            Row {
                Text(
                    text = stringResource(R.string.set),
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                
                Spacer(modifier = Modifier.width(2.dp))

                AsyncImage(
                    model = setIconPainter,
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.simbolo_temporal_forces),
                    modifier = Modifier
                        .size(24.dp)
                )

            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                

                Row {
                    Text(
                        text = stringResource(R.string.coleccion),
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )



                    AsyncImage(
                        model = symbolPainter,
                        contentDescription = null,
                        placeholder = painterResource(R.drawable.logo_temporal_forces),
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            }

            Text(
                text = "Rarity: $rarityPainter",
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.End)
                    .wrapContentWidth(Alignment.End),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
