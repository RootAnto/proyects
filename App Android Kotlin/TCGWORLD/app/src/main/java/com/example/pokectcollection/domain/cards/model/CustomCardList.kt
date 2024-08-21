package com.example.pokectcollection.domain.cards.model

import com.example.pokectcollection.domain.user.model.User

/**
 * Class [CustomCardList] that represents the [User] custom lists in Firebase.
 *
 * @param name the name of the custom list.
 * @param customList the [List] of custom lists.
 */
class CustomCardList(
    val name: String = "",
    val customList: List<Card> = emptyList()
)
