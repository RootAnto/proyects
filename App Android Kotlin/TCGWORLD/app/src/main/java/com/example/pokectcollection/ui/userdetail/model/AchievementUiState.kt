package com.example.pokectcollection.ui.userdetail.model


/**
 * [AchievementUiState] Represents the UI state for an achievement.
 *
 * @property name The name of the achievement.
 * @property description The description of the achievement.
 * @property imageUrl The URL of the image associated with the achievement.
 * @property obtained Boolean flag indicating if the achievement has been obtained.
 */
class AchievementUiState(
    val name: String,
    val description: String,
    val imageUrl: String,
    val obtained: Boolean
)