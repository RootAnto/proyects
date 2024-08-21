package com.example.pokectcollection.domain.cards.model

import com.example.pokectcollection.domain.setlist.model.Set

/**
 * Data class [Card] representing a card.
 *
 * @property id The unique identifier of the card.
 * @property abilities The abilities of the card.
 * @property artist The artist of the card.
 * @property attacks The attacks of the card.
 * @property evolvesFrom The card from which this card evolves.
 * @property evolvesTo The list of cards to which this card evolves.
 * @property flavorText The flavor text of the card.
 * @property images The images of the card.
 * @property hp The HP (health points) of the card.
 * @property legalities The legalities of the card.
 * @property name The name of the card.
 * @property number The number of the card.
 * @property rarity The rarity of the card.
 * @property resistances The resistances of the card.
 * @property retreatCost The retreat cost of the card.
 * @property rules The rules of the card.
 * @property set The set to which the card belongs.
 * @property supertype The supertype of the card.
 * @property subtypes The subtypes of the card.
 * @property types The types of the card.
 * @property weaknesses The weaknesses of the card.
 * @property owned Whether the card is owned by the user.
 */
data class Card(
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
        /**
         * Returns an empty [Card] instance.
         */
        fun getEmptyCard() = Card(
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
