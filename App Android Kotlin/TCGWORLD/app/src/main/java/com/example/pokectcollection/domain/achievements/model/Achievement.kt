package com.example.pokectcollection.domain.achievements.model

/**
 * Data class [Achievement] representing an achievement in the PocketCollection application.
 *
 * @property name The name of the achievement.
 * @property description A brief description of the achievement.
 * @property url The URL to an image or resource related to the achievement.
 */
data class Achievement(
    val name: String = "",
    val description: String = "",
    val url: String = ""
)
