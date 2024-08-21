package com.example.pokectcollection.domain.user.model

/**
 * Data class representing a [User].
 *
 * @property name The name of the user.
 * @property email The email address of the user.
 * @property profilePictureUrl The URL of the user's profile picture.
 * @property cardList The list of card IDs owned by the user.
 * @property achievementList The list of achievement IDs achieved by the user.
 */
data class User(
    val name: String = "",
    val email: String = "",
    val profilePictureUrl: String = "",
    val cardList: List<String> = emptyList(),
    val achievementList: List<String> = emptyList()
)
