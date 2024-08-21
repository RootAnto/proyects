package com.example.pokectcollection.data.card

import com.example.pokectcollection.domain.cards.model.Card
import com.example.pokectcollection.domain.setlist.model.Set

/**
 * The data class that represents the JSON format of [Card] in firebase.
 *
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
data class FirebaseCard(
    val abilities: ArrayList<Map<String, String>> = arrayListOf(emptyMap()),
    val artist: String = "",
    val attacks: ArrayList<FirebaseCardAttack> = arrayListOf(),
    val evolvesFrom: String = "",
    val evolvesTo: List<String> = listOf(),
    val flavorText: String = "",
    val images: Map<String, String> = emptyMap(),
    val hp: String = "",
    val legalities: Map<String, String> = emptyMap(),
    val name: String = "",
    val number: String = "",
    val rarity: String = "",
    val resistances: ArrayList<Map<String, String>> = arrayListOf(emptyMap()),
    val retreatCost: ArrayList<String> = arrayListOf(),
    val rules: ArrayList<String> = arrayListOf(),
    val set: Map<String, String> = emptyMap(),
    val supertype: String = "",
    val subtypes: ArrayList<String> = arrayListOf(),
    val types: ArrayList<String> = arrayListOf(),
    val weaknesses: ArrayList<Map<String, String>> = arrayListOf(emptyMap())
)
