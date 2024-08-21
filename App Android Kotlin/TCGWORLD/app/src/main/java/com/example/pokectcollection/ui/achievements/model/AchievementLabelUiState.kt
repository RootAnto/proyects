package com.example.pokectcollection.ui.achievements.model

import com.example.pokectcollection.domain.achievements.model.Achievement

/**
 * [AchievementLabelUiState] Represents the UI state of an achievement label.
 *
 * @param achievement The [Achievement] associated with this UI state.
 * @param display A boolean indicating whether the achievement should be displayed.
 */
data class AchievementLabelUiState(val achievement: Achievement, val display: Boolean){

    /**
     * Provides an empty state of [AchievementLabelUiState].
     *
     * @return An instance of [AchievementLabelUiState] with default values.
     */
    companion object{
        fun getEmptyState() = AchievementLabelUiState(
            achievement = Achievement(),display = false
        )
    }


}