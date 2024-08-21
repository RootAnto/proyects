package com.example.pokectcollection.data.card

import com.example.pokectcollection.domain.cards.model.Card
import com.example.pokectcollection.domain.cards.model.CardAbilities
import com.example.pokectcollection.domain.cards.model.CardAttack
import com.example.pokectcollection.domain.cards.model.CardImages
import com.example.pokectcollection.domain.cards.model.CardLegalities
import com.example.pokectcollection.domain.cards.model.CardResistance
import com.example.pokectcollection.domain.cards.model.CardWeakness
import com.example.pokectcollection.domain.setlist.model.Set

/**
 * Convert [FirebaseCard] to [Card].
 */
fun FirebaseCard.toDomainModel(
    cardId: String,
    set: Set,
    ownedCardList: List<String>
) = Card(
    id = cardId,
    abilities = getCardAbilities(abilities),
    artist = artist,
    attacks = getCardAttacks(attacks),
    evolvesFrom = evolvesFrom,
    evolvesTo = evolvesTo,
    flavorText = flavorText,
    images = getCardImages(images),
    hp = hp,
    legalities = getCardLegalities(legalities),
    name = name,
    number = number,
    rarity = rarity,
    resistances = getCardResistances(resistances),
    retreatCost = retreatCost,
    rules = rules,
    set = set,
    supertype = supertype,
    subtypes = subtypes,
    types = types,
    weaknesses = getCardWeaknesses(weaknesses),
    owned = cardId in ownedCardList
)

/**
 * Retrieves card abilities from a list of ability maps.
 *
 * @param abilities The list of ability maps.
 * @return A [CardAbilities] object containing the first ability's details or an empty abilities object if none exist.
 */
private fun getCardAbilities(abilities: List<Map<String, String>>?): CardAbilities {
    val ability = abilities?.firstOrNull() ?: return CardAbilities.getEmptyCardAbilities()
    return CardAbilities(
        name = ability["name"].orEmpty(),
        text = ability["text"].orEmpty(),
        type = ability["type"].orEmpty()
    )
}


/**
 * Converts a list of [FirebaseCardAttack] to a list of [CardAttack].
 *
 * @param attacks The list of [FirebaseCardAttack].
 * @return A list of [CardAttack] objects.
 */
private fun getCardAttacks(attacks: List<FirebaseCardAttack>?): List<CardAttack> {
    return attacks?.map { attack ->
        CardAttack(
            convertedEnergyCost = attack.convertedEnergyCost.toString(),
            cost = attack.cost,
            damage = attack.damage,
            name = attack.name,
            text = attack.text
        )
    } ?: listOf(CardAttack.getEmptyCardAttack())
}


/**
 * Converts a map of image URLs to a [CardImages] object.
 *
 * @param images The map of image URLs.
 * @return A [CardImages] object containing the large and small image URLs.
 */
private fun getCardImages(images: Map<String, String>?): CardImages {
    return CardImages(
        large = images?.get("large").orEmpty(),
        small = images?.get("small").orEmpty()
    )
}


/**
 * Converts a map of legalities to a [CardLegalities] object.
 *
 * @param legalities The map of legalities.
 * @return A [CardLegalities] object containing the legalities details.
 */
private fun getCardLegalities(legalities: Map<String, String>?): CardLegalities =
    CardLegalities(
        unlimited = legalities?.get("unlimited").orEmpty(),
        standard = legalities?.get("standard").orEmpty(),
        expanded = legalities?.get("expanded").orEmpty()
    )


/**
 * Converts a list of resistance maps to a list of [CardResistance].
 *
 * @param resistances The list of resistance maps.
 * @return A list of [CardResistance] objects.
 */
private fun getCardResistances(resistances: List<Map<String, String>>?): List<CardResistance> =
    resistances?.map { resistance ->
        CardResistance(
            type = resistance["type"].orEmpty(),
            value = resistance["value"].orEmpty()
        )
    } ?: listOf(CardResistance.getEmpyCardReistance())


/**
 * Converts a list of weakness maps to a list of [CardWeakness].
 *
 * @param weaknesses The list of weakness maps.
 * @return A list of [CardWeakness] objects.
 */
private fun getCardWeaknesses(weaknesses: List<Map<String, String>>?): List<CardWeakness> =
    weaknesses?.map { weakness ->
        CardWeakness(
            type = weakness["type"].orEmpty(),
            value = weakness["value"].orEmpty()
        )
    } ?: listOf(CardWeakness.getEmpyCardWeakness())
