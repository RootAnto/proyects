package com.example.pokectcollection.ui.userdetail.model

import androidx.compose.runtime.Immutable
import com.example.pokectcollection.domain.user.model.User

/**
 * [UserProfileUiState] Represents the UI state for a user profile.
 *
 * @property user The [User] object containing user information.
 * @property joinDate The date the user joined, represented as a [String].
 * @property completedSets The number of sets completed by the user.
 * @property cardsObtained The number of cards obtained by the user.
 */
@Immutable
data class UserProfileUiState(
    val user: User,
    val joinDate: String,
    val completedSets: Int,
    val cardsObtained: Int
) {

    /**
     * Provides an instance of [UserProfileUiState] with default empty values.
     *
     * @return An instance of [UserProfileUiState] with all properties initialized to default values.
     */
    companion object {

        fun getEmptyState() = UserProfileUiState(
            user = User(),
            joinDate = "",
            completedSets = 0,
            cardsObtained = 0
        )
    }

}
