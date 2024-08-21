package com.example.pokectcollection.ui.createlistcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokectcollection.domain.cards.usecase.GetAllCardsUseCase
import com.example.pokectcollection.domain.user.usecase.SaveCustomListUseCase
import com.example.pokectcollection.ui.carddetail.model.toUiState
import com.example.pokectcollection.ui.createlistcard.model.CreateCustomCardListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel [CreateCustomCardListViewModel] for creating custom card lists.
 *
 * @param getAllCardsUseCase The use case to get all cards from the repository.
 * @param saveCustomListUseCase The use case to save the custom card list.
 */
@HiltViewModel
class CreateCustomCardListViewModel @Inject constructor(
    private val getAllCardsUseCase: GetAllCardsUseCase,
    private val saveCustomListUseCase: SaveCustomListUseCase
) : ViewModel() {


    val scope = CoroutineScope(Dispatchers.IO)
    private val _uiState = MutableStateFlow(CreateCustomCardListUiState.getEmptyState())
    val uiState: StateFlow<CreateCustomCardListUiState> = _uiState.asStateFlow()


    private val selectedCardIds = mutableListOf<String>()

    /**
     * Initializes the ViewModel by fetching all cards and updating the UI state.
     */
    init {
        getAllCardsUseCase().onEach { cardList ->
            _uiState.value = CreateCustomCardListUiState(cardList.map { it.toUiState() })
        }.launchIn(viewModelScope)
    }

    /**
     * Adds a card ID to the list of selected card IDs.
     *
     * @param cardId The ID of the card to add.
     */
    fun addToSelectedCardIds(cardId: String) {
        selectedCardIds.add(cardId)
    }

    /**
     * Removes a card ID from the list of selected card IDs.
     *
     * @param cardId The ID of the card to remove.
     */
    fun removeFromSelectedCardIds(cardId: String) {
        selectedCardIds.remove(cardId)
    }

    /**
     * Saves the custom card list with the given name.
     *
     * @param listName The name of the custom card list.
     */
    fun saveCustomList(listName: String) {
        scope.launch {
            saveCustomListUseCase(listName, selectedCardIds)
            selectedCardIds.clear()
        }
    }

}
