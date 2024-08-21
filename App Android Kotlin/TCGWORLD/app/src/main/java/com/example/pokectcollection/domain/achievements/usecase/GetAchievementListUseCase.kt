package com.example.pokectcollection.domain.achievements.usecase

import com.example.pokectcollection.domain.achievements.AchievementRepository
import com.example.pokectcollection.domain.achievements.model.Achievement
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case [GetAchievementListUseCase] for retrieving a list of achievements.
 *
 * @property achievementRepository The repository to manage achievements data.
 */
class GetAchievementListUseCase @Inject constructor(
    private val achievementRepository: AchievementRepository
) {
    /**
     * Invokes the use case to get the list of achievements.
     *
     * @return A [Flow] emitting the list of [Achievement]s.
     */
    operator fun invoke(): Flow<List<Achievement>> = achievementRepository.getAchievementList()
}
