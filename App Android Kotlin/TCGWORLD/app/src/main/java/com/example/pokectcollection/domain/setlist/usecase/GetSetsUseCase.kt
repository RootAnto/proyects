package com.example.pokectcollection.domain.setlist.usecase

import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.cards.CardRepository
import com.example.pokectcollection.domain.setlist.SetRepository
import com.example.pokectcollection.domain.setlist.model.Set
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Use case for retrieving a list of [Set]s.
 *
 * @property authRepository The repository for authentication-related operations.
 * @property setRepository The repository for set-related operations.
 * @property cardRepository The repository for card-related operations.
 */
class GetSetsUseCase(
    private val authRepository: AuthRepository,
    private val setRepository: SetRepository,
    private val cardRepository: CardRepository
) {

    /**
     * Retrieves a list of [Set]s.
     *
     * @return A [Flow] emitting a list of sets.
     */
    suspend operator fun invoke(): Flow<List<Set>> {
        val setList = setRepository.getSets()
        return if (setList.isNotEmpty()) {
            flowOf(setList)
        } else {
            val userId = authRepository.getCurrentUserId() as String
            cardRepository.updateLoadedOwnedCards(userId)
            return flowOf(setRepository.getSets())
        }
    }
}
