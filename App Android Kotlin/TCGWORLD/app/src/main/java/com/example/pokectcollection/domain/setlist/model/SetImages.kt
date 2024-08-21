package com.example.pokectcollection.domain.setlist.model

/**
 * Data class [SetImages] representing the images of a card [Set].
 *
 * @param symbol URL of the symbol image.
 * @param logo URL of the logo image.
 */
data class SetImages(
    val symbol: String,
    val logo: String
) {
    companion object {
        /**
         * Provides an empty instance of [SetImages].
         *
         * @return An instance of [SetImages] with empty values.
         */
        fun getEmptySetImages() = SetImages(
            symbol = "",
            logo = ""
        )
    }
}
