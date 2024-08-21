package com.example.pokectcollection.ui.setlist.model

import com.example.pokectcollection.domain.setlist.model.Set

/**
 * Extension function to convert a [Set] to [SetUiState].
 *
 * This function maps the properties of a [Set] instance to a [SetUiState] instance.
 *
 * @return an instance of [SetUiState] with the mapped properties.
 */
fun Set.toUiState() = SetUiState(
    id = id,
    name = name,
    series = series,
    total = total,
    legalities = legalities,
    releaseDate = releaseDate,
    images = images,
    ownedCard = ownedCards
)
