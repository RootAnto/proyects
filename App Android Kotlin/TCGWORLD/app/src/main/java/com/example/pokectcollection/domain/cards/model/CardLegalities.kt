package com.example.pokectcollection.domain.cards.model

import androidx.compose.runtime.Immutable

/**
 * Data class [CardLegalities] representing the legalities of [Card].
 *
 * @param expanded Legal status for the expanded format.
 * @param standard Legal status for the standard format.
 * @param unlimited Legal status for the unlimited format.
 */
@Immutable
data class CardLegalities(
    val expanded: String,
    val standard: String,
    val unlimited: String
) {
    companion object {
        fun getEmptyCardLegalities() = CardLegalities(
            expanded = "",
            standard = "",
            unlimited = ""
        )
    }
}
