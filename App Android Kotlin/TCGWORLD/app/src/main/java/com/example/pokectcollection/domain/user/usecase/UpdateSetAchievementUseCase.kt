package com.example.pokectcollection.domain.user.usecase

import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.cards.CardRepository
import com.example.pokectcollection.domain.user.UserRepository

class UpdateSetAchievementUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val cardRepository: CardRepository
) {

    suspend operator fun invoke() {
        val userId = authRepository.getCurrentUserId() as String
        val achievementList = userRepository.getUserAchievements(userId)
        if (cardRepository.isSetCompleted() && "First set completed" !in achievementList)
            userRepository.updateUserAchievements(userId, achievementList + "First set completed")
    }
}