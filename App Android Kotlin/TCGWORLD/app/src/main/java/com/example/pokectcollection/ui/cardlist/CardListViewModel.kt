package com.example.pokectcollection.ui.cardlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.pokectcollection.domain.auth.usecase.SignOutUseCase
import com.example.pokectcollection.domain.cards.model.Card
import com.example.pokectcollection.domain.cards.usecase.GetCardListUseCase
import com.example.pokectcollection.domain.user.usecase.CreateCustomCardListUseCase
import com.example.pokectcollection.domain.user.usecase.UpdateCardOwnershipUseCase
import com.example.pokectcollection.domain.user.usecase.UpdateSetAchievementUseCase
import com.example.pokectcollection.ui.carddetail.model.toUiState
import com.example.pokectcollection.ui.cardlist.model.CardListUiState
import com.example.pokectcollection.ui.cardlist.view.CardListScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The ViewModel for the [CardListScreen].
 *
 * @param getCardListUseCase the use case for fetching the list of cards.
 * @param updateCardOwnershipUseCase the use case for updating card ownership.
 * @param createCustomCardListUseCase the use case for creating a custom card list.
 * @param signOutUseCase the use case for signing out the user.
 * @param savedStateHandle the saved state handle for retrieving state.
 */
@HiltViewModel
class CardListViewModel @Inject constructor(
    private val getCardListUseCase: GetCardListUseCase,
    private val updateCardOwnershipUseCase: UpdateCardOwnershipUseCase,
    private val createCustomCardListUseCase: CreateCustomCardListUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val updateSetAchievementUseCase: UpdateSetAchievementUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CardListUiState.getEmptyState())

    /**
     * StateFlow to be collected in the UI for updating the card list.
     */
    val uiState: StateFlow<CardListUiState> = _uiState.asStateFlow()

    private var _cardListNotOwned: MutableList<Card> = mutableListOf()
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    /**
     * Initializes the ViewModel by fetching the card list.
     */
    init {
        val setId: String? = savedStateHandle["setId"]
        scope.launch {
            getCardListUseCase(setId as String).collect { cardList ->
                _uiState.value = CardListUiState(cardList.map { it.toUiState() })
                _cardListNotOwned.clear()
                _cardListNotOwned.addAll(cardList.filter { !it.owned })
            }
        }
    }

    /**
     * Updates the ownership status of a card.
     *
     * @param cardId the ID of the card to update.
     */
    fun updateCardOwnership(cardId: String) {
        scope.launch {
            val setId: String? = savedStateHandle["setId"]
            updateCardOwnershipUseCase(
                setId = setId as String,
                cardId = cardId
            ).collect { cardList ->
                _uiState.value = CardListUiState(cardList.map { it.toUiState() })
                _cardListNotOwned.clear()
                _cardListNotOwned.addAll(cardList)
            }
            updateSetAchievementUseCase()
        }
    }

    /**
     * Creates a custom list with cards that are not owned.
     */
    fun createCustomListWithNotObtainedCard() {
        if(_cardListNotOwned.isNotEmpty())
            scope.launch {
                createCustomCardListUseCase(_cardListNotOwned)
            }
    }

    /**
     * Signs out the user.
     */
    fun logOut() {
        signOutUseCase()
    }
}
