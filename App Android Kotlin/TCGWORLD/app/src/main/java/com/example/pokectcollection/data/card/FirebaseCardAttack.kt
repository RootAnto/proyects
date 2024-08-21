package com.example.pokectcollection.data.card

import com.example.pokectcollection.domain.cards.model.Card
/**
 * Class that represents the attack info of a [Card] for Firebase format.
 *
 * @param convertedEnergyCost the energy cost of the [FirebaseCardAttack].
 * @param cost the [ArrayList] of the cost of the [FirebaseCardAttack].
 * @param damage the damage of the [FirebaseCardAttack].
 * @param name the name of the [FirebaseCardAttack].
 * @param text the description of the [FirebaseCardAttack].
 */
data class FirebaseCardAttack(
    val convertedEnergyCost: Int = 0,
    val cost: ArrayList<String> = arrayListOf(),
    val damage: String = "",
    val name: String = "",
    val text: String = ""
)
