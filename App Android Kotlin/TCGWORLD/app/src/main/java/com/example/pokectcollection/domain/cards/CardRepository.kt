package com.example.pokectcollection.domain.cards

import com.example.pokectcollection.domain.cards.model.Card
import com.example.pokectcollection.domain.setlist.model.Set
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface [CardRepository] for managing card data.
 */
interface CardRepository {

    /**
     * Loads cards from Firebase for the specified set.
     *
     * @param set The set for which to load cards.
     * @param ownedCardList A list of owned card IDs.
     * @param areOwnedCardsIncluded Whether owned cards should be included.
     */
    suspend fun loadFirebaseSetCards(
        set: Set,
        ownedCardList: List<String>,
        areOwnedCardsIncluded: Boolean
    )
    //TODO doc :D
    fun loadUserAchievements(achievementList: List<String>)

    /**
     * Gets the list of cards for the specified set ID.
     *
     * @param setId The ID of the set.
     * @return A [Flow] emitting a list of [Card]s.
     */
    suspend fun getCardList(setId: String): Flow<List<Card>>

    /**
     * Gets a card by its ID within the specified set.
     *
     * @param setId The ID of the set.
     * @param cardId The ID of the card.
     * @return A [Flow] emitting the [Card] if found, or null otherwise.
     */
    fun getCardById(setId: String, cardId: String): Flow<Card?>

    /**
     * Gets a custom card by its ID within the specified set.
     *
     * @param setId The ID of the set.
     * @param cardId The ID of the card.
     * @return The [Card] if found.
     */
    fun getCustomCardById(setId: String, cardId: String): Card

    /**
     * Updates the local card ownership for the specified card within the set.
     *
     * @param setId The ID of the set.
     * @param cardId The ID of the card.
     * @return A [Flow] emitting a list of updated [Card]s.
     */
    fun updateLocalCardOwnership(setId: String, cardId: String): Flow<List<Card>>

    /**
     * Updates the loaded owned cards for the specified user.
     *
     * @param userId The ID of the user.
     * @return A list of updated owned card IDs.
     */
    suspend fun updateLoadedOwnedCards(userId: String): List<String>

    // TODO: Documentar :D
    fun getCompletedSets(): Flow<Int>

    fun getAllCards(): Flow<List<Card>>

    fun areLocalResourcesLoaded(): Boolean

    fun getObtainedAchievements(): List<String>

    fun addObtainedAchievement(newAchievement: String)

    fun isSetCompleted(): Boolean


}
