package com.example.pokectcollection.data.user

import com.example.pokectcollection.domain.cards.model.Card
import com.example.pokectcollection.domain.user.model.User

/**
 * Class [FirebaseOwnedCardList] that represents the [User] [Card] owned list in Firebase.
 *
 * @param cardIdList the [List] of custom lists.
 */
data class FirebaseOwnedCardList(val cardIdList: List<String> = emptyList())
