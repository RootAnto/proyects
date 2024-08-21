package com.example.pokectcollection.domain.user.usecase

import com.example.pokectcollection.domain.achievements.AchievementRepository
import com.example.pokectcollection.domain.achievements.model.Achievement
import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.cards.CardRepository
import com.example.pokectcollection.domain.user.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlin.coroutines.coroutineContext

class CheckAchievementsUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val cardRepository: CardRepository,
    private val achievementRepository: AchievementRepository
) {

    suspend operator fun invoke(): Flow<Achievement> = flow {
        while (coroutineContext.isActive) {
            delay(1500L)
            val userId = authRepository.getCurrentUserId()
            val areLocalResourcesLoaded = cardRepository.areLocalResourcesLoaded()
            if (userId != null && areLocalResourcesLoaded) {
                val firebaseAchievements = userRepository.getUserAchievements(userId)
                val userObtainedAchievements = cardRepository.getObtainedAchievements()
                val newAchievement = compareAchievementLists(
                    firebaseAchievements,
                    userObtainedAchievements
                )
                if (newAchievement != null) {
                    cardRepository.addObtainedAchievement(newAchievement)
                    emit(achievementRepository.getAchievementByName(newAchievement))
                }
            }
        }
    }

    private fun compareAchievementLists(
        firebaseAchievements: List<String>,
        userObtainedAchievements: List<String>
    ): String? {
        firebaseAchievements.forEach {
            if (it !in userObtainedAchievements)
                return it
        }
        return null
    }

}
