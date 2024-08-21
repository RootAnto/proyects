package com.example.pokectcollection.domain.user.usecase

import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.cards.model.Card
import com.example.pokectcollection.domain.user.UserRepository

class RemoveCardFromCustomListUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(customListName: String, cardList: List<Card>) {
        val userId = authRepository.getCurrentUserId() as String
        val customCardIdList = cardList.map { it.id }
        userRepository.removeCardFromCustomList(userId, customListName, customCardIdList)
    }
}