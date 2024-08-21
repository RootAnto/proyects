package com.example.pokectcollection.domain.cards.model

import androidx.compose.runtime.Immutable

/**
 * Data class [CardAttack] representing an attack of [Card].
 *
 * @param convertedEnergyCost The converted energy cost of the attack.
 * @param cost The energy types required for the attack.
 * @param damage The damage value of the attack.
 * @param name The name of the attack.
 * @param text The description of the attack.
 */
@Immutable
data class CardAttack(
    val convertedEnergyCost: String,
    val cost: List<String>,
    val damage: String,
    val name: String,
    val text: String
) {
    companion object {
        fun getEmptyCardAttack() = CardAttack(
            convertedEnergyCost = "",
            cost = listOf(),
            damage = "",
            name = "",
            text = ""
        )
    }
}
