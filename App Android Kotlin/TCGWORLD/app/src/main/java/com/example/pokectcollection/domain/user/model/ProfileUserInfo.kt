package com.example.pokectcollection.domain.user.model

/**
 * Data class [ProfileUserInfo] representing the profile information of a user.
 *
 * @param user The [User] object containing the user's details.
 * @param joinDate The join date of the user as a [Long] timestamp.
 * @param completedSets The number of completed sets by the user.
 * @param cardsOwned The number of cards owned by the user.
 */
data class ProfileUserInfo(
    val user: User,
    val joinDate: Long,
    val completedSets: Int,
    val cardsOwned: Int
)
