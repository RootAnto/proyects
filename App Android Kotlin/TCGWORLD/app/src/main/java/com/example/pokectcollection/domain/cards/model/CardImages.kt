package com.example.pokectcollection.domain.cards.model

import androidx.compose.runtime.Immutable

/**
 * Data class [CardImages] representing the images of [Card].
 *
 * @param large URL of the large image of the card.
 * @param small URL of the small image of the card.
 */
@Immutable
class CardImages(
    val large: String,
    val small: String
) {
    companion object {
        fun getEmptyCardImages() = CardImages(
            large = "",
            small = ""
        )
    }
}
