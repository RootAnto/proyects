package com.example.pokectcollection.domain.cards.model

import androidx.compose.runtime.Immutable

/**
 * Data class [CardAbilities] representing an ability of [Card].
 *
 * @param name Name of the ability.
 * @param text Description of the ability.
 * @param type Type of the ability.
 */
@Immutable
data class CardAbilities(
    val name: String,
    val text: String,
    val type: String,
) {
    companion object {
        /**
         * Provides an instance of [CardAbilities] with empty values.
         *
         * @return An instance of [CardAbilities] with all properties initialized to empty strings.
         */
        fun getEmptyCardAbilities() = CardAbilities(
            name = "",
            text = "",
            type = ""
        )
    }
}
