package com.example.pokectcollection.data.card

/**
 * Data class [FirebaseCustomCardList] representing a custom card list in Firebase.
 * @param name The name of the custom card list.
 * @param customList The list of custom card IDs.
 */
data class FirebaseCustomCardList(
    val name: String = "",
    val customList: List<String> = emptyList()
)
