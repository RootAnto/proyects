package com.example.pokectcollection.ui.setlist.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.example.pokectcollection.R
import com.example.pokectcollection.ui.SearchBar
import com.example.pokectcollection.ui.setlist.SetListViewModel
import com.example.pokectcollection.ui.topbar.view.TopMenuBar

/**
 * Composable function that displays the set list screen.
 *
 * @param viewModel the [SetListViewModel] that manages the UI state.
 * @param onProfileClick lambda function to handle the profile click action.
 * @param onSetClicked lambda function to handle the set click action.
 * @param onSetListClick lambda function to handle the set list click action.
 * @param onLogOut lambda function to handle the log out action.
 * @param onCreateListClick lambda function to handle the create list click action.
 * @param onCheckListCardClick lambda function to handle the check list card click action.
 * @param onClickBack lambda function to handle the back click action.
 * @param onClickAchievements lambda function to handle the achievements click action.
 */
@Composable
fun SetListScreen(
    viewModel: SetListViewModel = hiltViewModel(),
    onProfileClick: () -> Unit,
    onSetClicked: (String) -> Unit,
    onSetListClick: () -> Unit,
    onLogOut: () -> Unit,
    onCreateListClick: () -> Unit,
    onCheckListCardClick: () -> Unit,
    onClickBack: () -> Unit,
) {
    // Obtain the UI state from the ViewModel
    val setListUiState by viewModel.uiState.collectAsState()

    // State for the search filter text
    var filterText by remember { mutableStateOf("") }

    LifecycleResumeEffect(viewModel) {
        viewModel.refreshData()
        onPauseOrDispose { }
    }

    BackHandler(true) {
    }

    Column(Modifier.padding(16.dp)) {
        // Top navigation bar
        TopMenuBar(
            title = stringResource(R.string.lista_de_sets),
            onUserImageClick = {
                onProfileClick()
            },
            onSetListClick = {
                onSetListClick()
            },
            onCreateListClick = {
                onCreateListClick()
            },
            onCheckListCardClick = {
                onCheckListCardClick()
            },
            onLogOut = {
                onLogOut()
            },
            isBackEnabled = false,
            onClickBack = {
                onClickBack()
            },
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Search bar
        SearchBar(
            texto = stringResource(R.string.buscar_set),
            filterText = filterText,
            onFilterChange = { filterText = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Card sets grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                setListUiState.setList.filter {
                    it.name.contains(filterText, ignoreCase = true)
                }
            ) { set ->
                CardSetInfo(
                    imagePainter = set.images.logo,
                    symbolPainter = set.images.symbol,
                    setName = set.name,
                    releaseDate = set.releaseDate,
                    legalities = set.legalities,
                    numCardHave = set.ownedCard,
                    numTotalSet = set.total,
                    onSetClicked = { onSetClicked(set.id) }
                )
            }
        }
    }
}
