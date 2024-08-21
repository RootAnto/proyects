package com.example.pokectcollection.domain.setlist.model

/**
 * Data class representing a [Set] of cards.
 *
 * @param id Identifier of the set.
 * @param name Name of the set.
 * @param series Series to which the set belongs.
 * @param total Total number of cards in the set.
 * @param legalities Legalities of the set.
 * @param releaseDate Release date of the set.
 * @param images Images related to the set.
 * @param ownedCards Number of owned cards in the set.
 */
data class Set(
    val id: String,
    val name: String,
    val series: String,
    val total: Int,
    val legalities: SetLegalities,
    val releaseDate: String,
    val images: SetImages,
    val ownedCards: Int
) {

    companion object {
        /**
         * Provides an empty instance of [Set].
         *
         * @return An instance of [Set] with all properties set to their default values.
         */
        fun getEmptySet() = Set(
            id = "",
            name = "",
            series = "",
            total = 0,
            legalities = SetLegalities.getEmptySetLegalities(),
            releaseDate = "",
            images = SetImages.getEmptySetImages(),
            ownedCards = 0
        )
    }
}
