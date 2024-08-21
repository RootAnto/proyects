package com.example.pokectcollection.ui.customcardlist.model

import com.example.pokectcollection.domain.cards.model.CustomCardList
import com.example.pokectcollection.ui.carddetail.model.CardUiState

/**
 * Data class [CustomCardListUiState] representing the UI state for a custom list of cards.
 *
 * @property lists a list of lists containing [CardUiState] instances, representing different custom card lists.
 */
data class CustomCardListUiState(val lists: List<CustomCardList>) {

    companion object {

        /**
         * Returns an empty state of [CustomCardListUiState].
         *
         * @return an instance of [CustomCardListUiState] with an empty list.
         */
        fun getEmptyState() = CustomCardListUiState(emptyList())
    }
}
