package com.example.pokectcollection.ui.achievements.model

/**
 * UI state [AchievementUiState] for an achievement.
 *
 * @property date the date when the achievement was obtained.
 * @property name the name of the achievement.
 * @property description the description of the achievement.
 * @property url the URL associated with the achievement.
 */
data class AchievementUiState(
    val date: String,
    val name: String,
    val description: String,
    val url: String
) {
    companion object {

        /**
         * Function to get an empty state of [AchievementUiState].
         *
         * @return an instance of [AchievementUiState] with all properties set to empty strings.
         */
        fun getEmptyState() = AchievementUiState(
            date = "",
            name = "",
            description = "",
            url = ""
        )
    }
}
