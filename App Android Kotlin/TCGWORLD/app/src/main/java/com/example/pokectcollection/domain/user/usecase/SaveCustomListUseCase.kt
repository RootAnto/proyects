package com.example.pokectcollection.domain.user.usecase

import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.user.UserRepository

class SaveCustomListUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(listName: String, customList: List<String>) {
        val userId = authRepository.getCurrentUserId() as String
        userRepository.updateUserCustomLists(
            userId = userId,
            listName = listName,
            customList = customList
            )
        val achievementList = userRepository.getUserAchievements(userId)
        if ("First custom list" !in achievementList)
            userRepository.updateUserAchievements(
                userId,
                achievementList + "First custom list"
            )
    }

}
