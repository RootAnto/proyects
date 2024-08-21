package com.example.pokectcollection.data.card

import com.example.pokectcollection.domain.cards.model.Card
import com.example.pokectcollection.domain.cards.model.CustomCardList


/**
 * Converts a [FirebaseCustomCardList] to a [CustomCardList].
 *
 * @param getCardById A function that takes a set ID and a card ID and returns a [Card].
 * @return A [CustomCardList] with the name and custom card list populated.
 */
fun FirebaseCustomCardList.toDomainModel(getCardById: (String, String) -> Card): CustomCardList =
    CustomCardList(
        name = name,
        customList = customList.map { getCardById(it.split("-")[0], it) }
    )

