package com.example.pokectcollection.ui.login.model

/**
 * Data class [SignInResult] representing the result of a sign-in attempt.
 *
 * @property data the user data if the sign-in was successful.
 * @property errorMessage the error message if the sign-in failed.
 */
data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

/**
 * Data class representing detailed information about a user.
 *
 * @property userId the unique identifier of the user.
 * @property email the email of the user.
 * @property username the username of the user.
 * @property profilePictureUrl the URL of the user's profile picture.
 * @property cardList the list of card IDs owned by the user.
 */
data class UserData(
    val userId: String,
    val email: String?,
    val username: String?,
    val profilePictureUrl: String?,
    val cardList: List<String> = emptyList(),
    val achievementList: List<String> = emptyList()
)
