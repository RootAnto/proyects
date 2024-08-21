package com.example.pokectcollection.data.card

import com.example.pokectcollection.data.LocalDataSource
import com.example.pokectcollection.data.user.FirebaseUserDataSource
import com.example.pokectcollection.domain.cards.CardRepository
import com.example.pokectcollection.domain.cards.model.Card
import com.example.pokectcollection.domain.setlist.model.Set
import com.example.pokectcollection.domain.user.model.User
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * The implementation of [CardRepository].
 *
 * @param firebaseCardDataSource tge [FirebaseCardDataSource] that provides [Card] data from Firebase.
 */
class CardRepositoryImp @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val firebaseCardDataSource: FirebaseCardDataSource,
    private val firebaseUserDataSource: FirebaseUserDataSource
) : CardRepository {


    /**
     * Loads the list of cards from Firebase for a given set and updates the local data source.
     *
     * @param set The set of cards to load.
     * @param ownedCardList The list of owned card IDs.
     * @param areOwnedCardsIncluded A flag indicating if owned cards should be included.
     */
    override suspend fun loadFirebaseSetCards(
        set: Set,
        ownedCardList: List<String>,
        areOwnedCardsIncluded: Boolean
    ) {
        val cardListDocs = firebaseCardDataSource.getCardList(set.id).await().documents
        val cardList = cardListDocs.map { doc ->
            doc.toObject<FirebaseCard>()?.toDomainModel(
                cardId = doc.id,
                set = set,
                ownedCardList = ownedCardList
            ) as Card
        }
        localDataSource.loadFirebaseSet(set = set, areOwnedIncluded = areOwnedCardsIncluded)
        localDataSource.loadFirebaseSetCardList(setId = set.id, cardList = cardList as MutableList)
    }

    /**
     * Loads the user's achievements into the local data source.
     *
     * @param achievementList The list of achievements to load.
     */
    override fun loadUserAchievements(achievementList: List<String>) {
        localDataSource.loadUserAchievements(achievementList)
    }

    /**
     * Retrieves the list of cards for a given set ID from the local data source.
     *
     * @param setId The ID of the set to retrieve cards for.
     * @return A flow emitting the list of cards for the specified set.
     */
    override suspend fun getCardList(setId: String): Flow<List<Card>> =
        localDataSource.getSetCardList(setId)


    /**
     * Retrieves a card by its set ID and card ID from the local data source.
     *
     * @param setId The ID of the set the card belongs to.
     * @param cardId The ID of the card to retrieve.
     * @return A flow emitting the card with the specified set ID and card ID, or null if not found.
     */
    override fun getCardById(setId: String, cardId: String): Flow<Card?> =
        localDataSource.getCardById(setId, cardId)



    /**
     * Retrieves a custom card by its set ID and card ID from the local data source.
     *
     * @param setId The ID of the set the custom card belongs to.
     * @param cardId The ID of the custom card to retrieve.
     * @return The custom card with the specified set ID and card ID.
     */
    override fun getCustomCardById(setId: String, cardId: String): Card =
        localDataSource.getCustomCardById(setId = setId, cardId = cardId)


    /**
     * Updates the ownership status of a card in the local data source.
     *
     * @param setId The ID of the set the card belongs to.
     * @param cardId The ID of the card to update ownership for.
     */
    override fun updateLocalCardOwnership(setId: String, cardId: String) =
        localDataSource.updateCardOwnership(setId = setId, cardId = cardId)

    /**
     * Updates the loaded owned cards for a user by retrieving them from Firebase and updating the local data source.
     *
     * @param userId The ID of the user whose owned cards are being updated.
     * @return A list of owned card IDs.
     */
    override suspend fun updateLoadedOwnedCards(userId: String): List<String> {
        val ownedCards: List<String> = firebaseUserDataSource.getUserById(userId).await()
            .toObject<User>()?.cardList.orEmpty()
        localDataSource.updateLoadedOwnershipData(ownedCards = ownedCards)
        return ownedCards
    }

    /**
     * Retrieves the count of completed sets from the local data source.
     *
     * @return A flow emitting the count of completed sets.
     */
    override fun getCompletedSets(): Flow<Int> = localDataSource.getCompletedSets()

    /**
     * Retrieves all cards from the local data source.
     *
     * @return A flow emitting the list of all cards.
     */
    override fun getAllCards(): Flow<List<Card>> = localDataSource.getCallCards()

    /**
     * Checks if local resources are loaded.
     *
     * @return True if local resources are loaded, false otherwise.
     */
    override fun areLocalResourcesLoaded(): Boolean = localDataSource.areLocalResourcesLoaded()

    /**
     * Retrieves the list of obtained achievements from the local data source.
     *
     * @return A list of obtained achievements.
     */
    override fun getObtainedAchievements(): List<String> = localDataSource.getUserAchievements()

    /**
     * Adds a new obtained achievement to the local data source.
     *
     * @param newAchievement The new achievement to add.
     */
    override fun addObtainedAchievement(newAchievement: String) {
        localDataSource.addObtainedAchievement(newAchievement)
    }

    /**
     * Checks if the set is completed.
     *
     * @return True if the set is completed, false otherwise.
     */
    override fun isSetCompleted(): Boolean = localDataSource.isSetCompleted()

}
