package com.example.pokectcollection.domain.achievements

import com.example.pokectcollection.domain.achievements.model.Achievement
import kotlinx.coroutines.flow.Flow

/**
 * Interface [AchievementRepository] for the Achievement Repository.
 *
 * This interface defines the contract for the methods that the achievement repository must implement.
 */
interface AchievementRepository {

    /**
     * Retrieves the list of achievements.
     *
     * @return A [Flow] emitting the list of [Achievement]s.
     */
    fun getAchievementList(): Flow<List<Achievement>>

    //TODO doc :D
    suspend fun getAchievementByName(name: String): Achievement
}
