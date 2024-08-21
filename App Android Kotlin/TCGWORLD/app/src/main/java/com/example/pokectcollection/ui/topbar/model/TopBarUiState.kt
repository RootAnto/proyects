package com.example.pokectcollection.ui.topbar.model

/**
 * Represents [TopBarUiState] the UI state for the top bar.
 *
 * @property profilePictureUrl The URL of the profile picture to be displayed in the top bar.
 */
data class TopBarUiState(
    val profilePictureUrl: String
) {
    companion object {
        /**
         * Provides an empty state for the top bar.
         *
         * @return An instance of [TopBarUiState] with default values.
         */
        fun getEmptyState() = TopBarUiState(
            profilePictureUrl = ""
        )
    }
}
