package com.example.pokectcollection.domain.cards.model

import androidx.compose.runtime.Immutable

/**
 * Data class [CardWeakness] representing the weakness of a [Card].
 *
 * @param type Type of the weakness.
 * @param value Value of the weakness.
 */
@Immutable
data class CardWeakness(
    val type: String,
    val value: String
) {
    companion object {
        fun getEmpyCardWeakness() = CardWeakness(
            type = "",
            value = ""
        )
    }
}
