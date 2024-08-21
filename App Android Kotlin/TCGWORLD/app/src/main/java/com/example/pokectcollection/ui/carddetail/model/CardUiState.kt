package com.example.pokectcollection.ui.carddetail.model

import androidx.compose.runtime.Immutable
import com.example.pokectcollection.domain.cards.model.Card
import com.example.pokectcollection.domain.cards.model.CardAbilities
import com.example.pokectcollection.domain.cards.model.CardAttack
import com.example.pokectcollection.domain.cards.model.CardImages
import com.example.pokectcollection.domain.cards.model.CardLegalities
import com.example.pokectcollection.domain.cards.model.CardResistance
import com.example.pokectcollection.domain.cards.model.CardWeakness
import com.example.pokectcollection.domain.setlist.model.Set

/**
 * The data [CardUiState] class that represents the UiState of a [Card] to be displayed.
 *
 * @param id the unique identifier of the [Card].
 * @param abilities the [ArrayList] of abilities of the [Card].
 * @param artist the artist of the [Card].
 * @param attacks the [ArrayList] of the attacks of the [Card].
 * @param evolvesFrom the name of the pokemon evolved from.
 * @param evolvesTo the name of the pokemons that can evolve to.
 * @param flavorText flavored text of the [Card].
 * @param images the list of images of the [Card].
 * @param hp the health points of the pokemon of the [Card].
 * @param legalities the legalities of the card.
 * @param name the name of the [Card].
 * @param number the number of the [Card] in the [Set].
 * @param rarity the rarity of the [Card].
 * @param resistances the [ArrayList] of resistances of the [Card].
 * @param retreatCost the cost for retreating the [Card].
 * @param rules the rules applied to the [Card].
 * @param set the info of the [Set] that the [Card] belongs to.
 * @param supertype the supertype of the [Card].
 * @param subtypes the [ArrayList] of subtypes of the [Card].
 * @param types the [ArrayList] of the types of the [Card].
 * @param weaknesses the [ArrayList] of the weaknesses of the [Card].
 */
@Immutable
data class CardUiState(
    val id: String,
    val abilities: CardAbilities,
    val artist: String,
    val attacks: List<CardAttack>,
    val evolvesFrom: String,
    val evolvesTo: List<String>,
    val flavorText: String,
    val images: CardImages,
    val hp: String,
    val legalities: CardLegalities,
    val name: String,
    val number: String,
    val rarity: String,
    val resistances: List<CardResistance>,
    val retreatCost: List<String>,
    val rules: List<String>,
    val set: Set,
    val supertype: String,
    val subtypes: List<String>,
    val types: List<String>,
    val weaknesses: List<CardWeakness>,
    val owned: Boolean
) {
    companion object {
        fun getEmptyState() = CardUiState(
            id = "",
            abilities = CardAbilities.getEmptyCardAbilities(),
            artist = "",
            attacks = listOf(CardAttack.getEmptyCardAttack()),
            evolvesFrom = "",
            evolvesTo = listOf(),
            flavorText = "",
            images = CardImages.getEmptyCardImages(),
            hp = "",
            legalities = CardLegalities.getEmptyCardLegalities(),
            name = "",
            number = "",
            rarity = "",
            resistances = listOf(CardResistance.getEmpyCardReistance()),
            retreatCost = listOf(),
            rules = emptyList(),
            set = Set.getEmptySet(),
            supertype = "",
            subtypes = listOf(),
            types = listOf(),
            weaknesses = listOf(CardWeakness.getEmpyCardWeakness()),
            owned = false
        )
    }
}
