package com.example.pokectcollection.domain.cards.model

import androidx.compose.runtime.Immutable

/**
 * Data class [CardResistance] representing the resistance of [Card].
 *
 * @param type Type of the resistance.
 * @param value Value of the resistance.
 */
@Immutable
data class CardResistance(
    val type: String,
    val value: String
) {
    companion object {
        fun getEmpyCardReistance() = CardResistance(
            type = "",
            value = ""
        )
    }
}
