package com.example.pokectcollection.ui.createlistcard.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.pokectcollection.R
import com.example.pokectcollection.ui.SearchBar
import com.example.pokectcollection.ui.createlistcard.CreateCustomCardListViewModel
import com.example.pokectcollection.ui.topbar.view.TopMenuBar

@Composable
fun CreateListCard(
    viewModel: CreateCustomCardListViewModel = hiltViewModel(),
    onProfileClick: () -> Unit,
    onSetListClick: () -> Unit,
    onLogOut: () -> Unit,
    onCreateListClick: () -> Unit,
    onClickBack: () -> Unit,
    onCheckListCardClick: () -> Unit,
) {
    // Obtener el estado de la UI desde el ViewModel de la lista de cartas
    val cardListUiState by viewModel.uiState.collectAsState()

    // Estado para el filtro de texto
    var filterText by remember { mutableStateOf("") }

    // Estado para mostrar el modal
    var showDialog by remember { mutableStateOf(false) }

    // Estado para el nombre de la lista
    var listName by remember { mutableStateOf("") }

    // Estado para las cartas seleccionadas
    val selectedCards = remember { mutableStateListOf<String>() }

    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        // Background Image
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
                .padding(16.dp) // Added padding to the entire screen
        ) {
            // Content Box with padding
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                // Barra de navegación superior
                TopMenuBar(
                    title = stringResource(id = R.string.crear_lista),
                    onUserImageClick = onProfileClick,
                    onSetListClick = onSetListClick,
                    onCreateListClick = onCreateListClick,
                    onCheckListCardClick = onCheckListCardClick,
                    onLogOut = {
                        //viewModel.logOut()
                        onLogOut()
                    },
                    onClickBack = onClickBack,
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Barra de búsqueda
                SearchBar(
                    texto = stringResource(id = R.string.buscar_carta),
                    filterText = filterText,
                    onFilterChange = { filterText = it }
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Cuadrícula de cartas
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                    horizontalArrangement = Arrangement.spacedBy(1.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    val filteredCards = cardListUiState.list.filter {
                        it.name.contains(filterText, ignoreCase = true)
                                || it.number.contains(filterText, ignoreCase = true)
                                || it.rarity.contains(filterText, ignoreCase = true)
                                || it.artist.contains(filterText, ignoreCase = true)
                    }.sortedBy { it.number.toIntOrNull() ?: Int.MAX_VALUE }

                    items(filteredCards, key = { it.id }) { card ->
                        val isSelected = selectedCards.contains(card.id)
                        CardContentCustomList(
                            imageUrl = card.images.small,
                            isSelected = isSelected,
                            onCardClick = {
                                if (isSelected) {
                                    selectedCards.remove(card.id)
                                    viewModel.removeFromSelectedCardIds(card.id)
                                } else {
                                    selectedCards.add(card.id)
                                    viewModel.addToSelectedCardIds(card.id)
                                }
                            }
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                ElevatedButton(
                    onClick = { showDialog = true },
                    enabled = selectedCards.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 64.dp)
                ) {
                    Text(text = stringResource(id = R.string.guardar_lista))
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(stringResource(id = R.string.crear_lista)) },
                text = {
                    Column {
                        Text(text = stringResource(id = R.string.por_favor_ingrese_nombre_de_la_lista),)
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = listName,
                            onValueChange = { listName = it },
                            placeholder = { Text(stringResource(id = R.string.nombre_de_la_lista),) }
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            viewModel.saveCustomList(listName)
                            selectedCards.clear()
                            listName = ""
                        }
                    ) {
                        Text(stringResource(id = R.string.guardar))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialog = false }
                    ) {
                        Text(stringResource(id = R.string.cancelar))
                    }
                }
            )
        }
    }
}

@Composable
fun CardContentCustomList(
    imageUrl: String,
    isSelected: Boolean,
    onCardClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(1.dp)
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = if (isSelected) Color.Red else Color.Transparent,
                shape = RectangleShape
            )
            .clickable { onCardClick() }
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            placeholder = painterResource(R.drawable.reverse_card),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .alpha(if (isSelected) 1f else 0.5f)
        )
    }
}
