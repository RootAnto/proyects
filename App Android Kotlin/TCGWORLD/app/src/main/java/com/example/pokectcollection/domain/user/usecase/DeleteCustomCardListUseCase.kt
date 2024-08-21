package com.example.pokectcollection.domain.user.usecase

import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.user.UserRepository

class DeleteCustomCardListUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(listName: String) {
        val userId = authRepository.getCurrentUserId() as String
        userRepository.deleteCustomCardList(userId, listName)
    }
}