package com.example.pokectcollection.data.user

import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.cards.CardRepository
import com.example.pokectcollection.domain.cards.model.Card
import com.example.pokectcollection.domain.user.UserRepository
import com.example.pokectcollection.domain.user.model.ProfileUserInfo
import com.example.pokectcollection.domain.user.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * The class [GetProfileUserDataUseCase] is use case to get the [User] data from Firebase.
 *
 * @param authRepository the [AuthRepository] to get the current session [User] id.
 * @param userRepository the [UserRepository] to get the [User] data.
 * @param cardRepository the [CardRepository] to get the [User] [Card] and [Set] info
 */
class GetProfileUserDataUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val cardRepository: CardRepository
) {

    /**
     * Executes the use case.
     */
    suspend operator fun invoke(): Flow<ProfileUserInfo?> {
        val userId = authRepository.getCurrentUserId() as String
        val userCards = userRepository.getUserOwnedCardList(userId).size
        return combine(
            authRepository.getCurrentUserJoinDate(),
            userRepository.getUserById(userId),
            cardRepository.getCompletedSets(),
        ) { joinDate, user, completedSets ->
            ProfileUserInfo(
                user = user as User,
                joinDate = joinDate,
                completedSets = completedSets,
                cardsOwned = userCards
            )
        }
    }

}
