package com.example.pokectcollection.ui.carddetail.model

import com.example.pokectcollection.domain.cards.model.Card

/**
 * Converts [Card] to [CardUiState].
 *
 * Extension function that maps a [Card] domain model to a [CardUiState] which is used
 * for representing the UI state of a card in the card detail screen.
 *
 * @return an instance of [CardUiState] with properties mapped from the [Card] instance.
 */
fun Card.toUiState() = CardUiState(
    id = id,
    abilities = abilities,
    artist = artist,
    attacks = attacks,
    evolvesFrom = evolvesFrom,
    evolvesTo = evolvesTo,
    flavorText = flavorText,
    images = images,
    hp = hp,
    legalities = legalities,
    name = name,
    number = number,
    rarity = rarity,
    resistances = resistances,
    retreatCost = retreatCost,
    rules = rules,
    set = set,
    supertype = supertype,
    subtypes = subtypes,
    types = types,
    weaknesses = weaknesses,
    owned = owned
)
