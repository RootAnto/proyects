package com.example.pokectcollection.domain.user.usecase

import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.user.UserRepository

class UpdateCustomListNameUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(oldName: String, newName: String) {
        val userId = authRepository.getCurrentUserId() as String
        userRepository.updateCustomListName(userId, oldName, newName)
    }
}