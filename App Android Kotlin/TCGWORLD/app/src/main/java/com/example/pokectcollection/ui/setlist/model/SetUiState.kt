package com.example.pokectcollection.ui.setlist.model

import com.example.pokectcollection.domain.setlist.model.SetImages
import com.example.pokectcollection.domain.setlist.model.SetLegalities

/**
 * Data class [SetUiState] representing the UI state for a single set.
 *
 * @property id the unique identifier of the set.
 * @property name the name of the set.
 * @property series the series to which the set belongs.
 * @property total the total number of cards in the set.
 * @property legalities the legalities of the set.
 * @property releaseDate the release date of the set.
 * @property images the images associated with the set.
 * @property ownedCard the number of cards owned in the user's collection.
 */
data class SetUiState(
    val id: String,
    val name: String,
    val series: String,
    val total: Int,
    val legalities: SetLegalities,
    val releaseDate: String,
    val images: SetImages,
    val ownedCard: Int

) {

    companion object {

        /**
         * Returns an empty state of [SetUiState].
         *
         * @return an instance of [SetUiState] with default empty values.
         */
        fun getEmptyState() = SetUiState(
            id = "",
            name = "",
            series = "",
            total = 0,
            legalities = SetLegalities.getEmptySetLegalities(),
            releaseDate = "",
            images = SetImages.getEmptySetImages(),
            ownedCard = 0
        )
    }
}
