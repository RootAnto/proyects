package com.example.pokectcollection.ui.setlist.model

/**
 * Data class [SetListUiState] representing the UI state for the set list screen.
 *
 * @property setList the list of [SetUiState] representing the sets.
 */
data class SetListUiState(
    val setList: List<SetUiState>,
) {
    companion object {

        /**
         * Returns an empty state of [SetListUiState].
         *
         * @return an instance of [SetListUiState] with an empty list.
         */
        fun getEmptyState() = SetListUiState(emptyList())
    }
}
