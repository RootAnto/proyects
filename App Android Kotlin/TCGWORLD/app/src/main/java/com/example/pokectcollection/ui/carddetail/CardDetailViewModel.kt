package com.example.pokectcollection.ui.carddetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokectcollection.domain.cards.usecase.GetCardUseCase
import com.example.pokectcollection.ui.carddetail.model.CardUiState
import com.example.pokectcollection.ui.carddetail.model.toUiState
import com.example.pokectcollection.ui.carddetail.view.CardDetailScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * The view [CardDetailViewModel] model for the [CardDetailScreen].
 *
 * @param getCardUseCase the [GetCardUseCase] for fetching card details.
 * @param savedStateHandle the [SavedStateHandle] for retrieving card ID.
 */
@HiltViewModel
class CardDetailViewModel @Inject constructor(
    private val getCardUseCase: GetCardUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState: MutableStateFlow<CardUiState> = MutableStateFlow(CardUiState.getEmptyState())

    /**
     * [StateFlow] to be collected in [CardDetailScreen] for updating the info displayed.
     */
    val uiState: StateFlow<CardUiState> = _uiState.asStateFlow()

    /**
     * Initializes the view model by fetching card details if a card ID is provided.
     */
    init {
        val cardId: String? = savedStateHandle["cardId"]
        if (cardId != null) {
            getCardUseCase(cardId).onEach { card ->
                _uiState.value = card?.toUiState() ?: CardUiState.getEmptyState()
            }.launchIn(viewModelScope)
        }
    }
}
