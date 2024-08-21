package com.example.pokectcollection.data.set

import com.example.pokectcollection.domain.setlist.model.Set
import com.example.pokectcollection.domain.setlist.model.SetImages
import com.example.pokectcollection.domain.setlist.model.SetLegalities

/**
 * Convert [FirebaseSet] to [Set].
 */
fun FirebaseSet.toDomainModel() = Set(
    id = id,
    name = name,
    series = series,
    total = total,
    legalities = getSetLegalities(legalities),
    releaseDate = releaseDate,
    images = getSetImages(images),
    ownedCards = 0
)

/**
 * Converts a map of legalities to a [SetLegalities] object.
 *
 * @param legalities The map of legalities.
 * @return A [SetLegalities] object containing the legality details.
 */
private fun getSetLegalities(legalities: Map<String, String>): SetLegalities = SetLegalities(
    unlimited = legalities["unlimited"].orEmpty(),
    standard = legalities["standard"].orEmpty(),
    expanded = legalities["expanded"].orEmpty()
)


/**
 * Converts a map of image URLs to a [SetImages] object.
 *
 * @param images The map of image URLs.
 * @return A [SetImages] object containing the symbol and logo image URLs.
 */
private fun getSetImages(images: Map<String, String>): SetImages = SetImages(
    symbol = images["symbol"].orEmpty(),
    logo = images["logo"].orEmpty()
)
