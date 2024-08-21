package com.example.pokectcollection.ui.cardlist.model

import androidx.compose.runtime.Immutable
import com.example.pokectcollection.ui.carddetail.model.CardUiState

/**
 * [CardListUiState] Represents the UI state for a list of cards.
 *
 * @property cardList the list of cards in their UI state representation.
 */
@Immutable
data class CardListUiState(
    val cardList: List<CardUiState>
) {
    companion object {

        /**
         * Returns an empty state of [CardListUiState].
         *
         * @return an instance of [CardListUiState] with an empty card list.
         */
        fun getEmptyState() = CardListUiState(emptyList())
    }
}
