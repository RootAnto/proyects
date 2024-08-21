package com.example.pokectcollection.ui.userdetail.model

import com.example.pokectcollection.domain.user.model.ProfileUserInfo
import com.example.pokectcollection.domain.user.model.User
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Extension function to convert a [User] object to a [UserProfileUiState].
 *
 * @return A [UserProfileUiState] containing the user's name, email, and profile picture URL.
 */
fun ProfileUserInfo.toUiState() = UserProfileUiState(
    user = user,
    joinDate = getConvertedDate(joinDate),
    completedSets = completedSets,
    cardsObtained = cardsOwned
)

fun getConvertedDate(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return format.format(date)
}
