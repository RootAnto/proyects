package com.example.pokectcollection.data.set

import com.example.pokectcollection.domain.setlist.model.Set
/**
 * Class [FirebaseSet] that represents the [Set] in Firebase.
 * @param id The unique identifier of the [Set].
 * @param name the name of the [Set].
 * @param series the series of the [Set].
 * @param total the total number of cards in the [Set].
 * @param legalities the legalities applied to the [Set].
 * @param releaseDate the date when the [Set] was released.
 * @param images the images associated with the [Set].
 */
data class FirebaseSet(
    val id: String = "",
    val name: String = "",
    val series: String = "",
    val total: Int = 0,
    val legalities: Map<String, String> = emptyMap(),
    val releaseDate: String = "",
    val images: Map<String, String> = emptyMap()
)
