package com.example.pokectcollection.ui.createlistcard.model

import com.example.pokectcollection.ui.carddetail.model.CardUiState

// TODO actualizalo :D
/**
 * Data class [CreateCustomCardListUiState] representing detailed information about a Pokémon.
 *
 * @property number the number of the Pokémon.
 * @property name the name of the Pokémon.
 * @property rarityImage the resource ID of the image representing the Pokémon's rarity.
 * @property checkImage the resource ID of the image representing the checkmark status.
 * @property crossImage the resource ID of the image representing the cross status.
 * @property isChecked the status indicating whether the Pokémon is checked. Default is false.
 */
data class CreateCustomCardListUiState(
    val list: List<CardUiState>
) {
    companion object {

        fun getEmptyState() = CreateCustomCardListUiState(emptyList())
    }
}
