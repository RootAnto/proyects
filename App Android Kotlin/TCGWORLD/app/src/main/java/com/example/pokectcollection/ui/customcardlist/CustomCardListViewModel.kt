package com.example.pokectcollection.ui.customcardlist


import androidx.lifecycle.ViewModel
import com.example.pokectcollection.domain.cards.model.CustomCardList
import com.example.pokectcollection.domain.cards.usecase.GetCustomCardListsUseCase
import com.example.pokectcollection.domain.user.usecase.DeleteCustomCardListUseCase
import com.example.pokectcollection.domain.user.usecase.RemoveCardFromCustomListUseCase
import com.example.pokectcollection.domain.user.usecase.UpdateCustomListNameUseCase
import com.example.pokectcollection.ui.customcardlist.model.CustomCardListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import javax.inject.Inject

/**
 * ViewModel [CustomCardListViewModel] for managing the state of [CustomCardList].
 *
 * @param getCustomCardListsUseCase the use case for fetching custom card lists.
 */
@HiltViewModel
class CustomCardListViewModel @Inject constructor(
    private val getCustomCardListsUseCase: GetCustomCardListsUseCase,
    private val removeCardFromCustomListUseCase: RemoveCardFromCustomListUseCase,
    private val deleteCustomCardListUseCase: DeleteCustomCardListUseCase,
    private val updateCustomListNameUseCase: UpdateCustomListNameUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomCardListUiState.getEmptyState())

    /**
     * StateFlow to be collected in the UI for updating the custom card lists.
     */
    val uiState: StateFlow<CustomCardListUiState> = _uiState.asStateFlow()

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    /**
     * Initializes the ViewModel and fetches the custom card lists.
     */
    init {
        scope.launch {
            getCustomCardListsUseCase().collect { cardLists ->
                _uiState.update { CustomCardListUiState(cardLists) }
            }
        }
    }

    fun removeCardFromList(customListName: String, cardId: String) {
        val selectedList = uiState.value.lists
            .first { it.name == customListName }
            .customList
            .filterNot { it.id == cardId }

        scope.launch {
            removeCardFromCustomListUseCase(customListName, selectedList)
        }

        val updatedLists = mutableListOf<CustomCardList>()
        uiState.value.lists.forEach { customList ->
            if (customList.name != customListName)
                updatedLists.add(customList)
            else {
                val modifiedList = customList.customList.filterNot { it.id == cardId }
                updatedLists.add(CustomCardList(customList.name, modifiedList))
            }
        }
        _uiState.value = CustomCardListUiState(updatedLists.toImmutableList())
    }

    fun deleteUserList(listName: String) {
        scope.launch {
            deleteCustomCardListUseCase(listName)
        }
    }

    fun changeListName(oldName: String, newName: String) {
        scope.launch {
            updateCustomListNameUseCase(oldName, newName)
        }
    }

}
