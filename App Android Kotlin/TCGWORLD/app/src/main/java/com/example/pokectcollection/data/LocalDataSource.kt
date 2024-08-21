package com.example.pokectcollection.data

import com.example.pokectcollection.domain.cards.model.Card
import com.example.pokectcollection.domain.setlist.model.Set
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The class [LocalDataSource]
 * In memory cache for the [Set] and [Card] for the current user.
 */
@Suppress("TooManyFunctions")
@Singleton
class LocalDataSource @Inject constructor() {

    private val _setsInfo: MutableList<Set> = mutableListOf()

    private val _cardsInfo: MutableMap<String, MutableList<Card>> = mutableMapOf()
    private val selectedCardList: MutableList<Card> = mutableListOf()
    private val achievements: MutableList<String> = mutableListOf()
    private var areOwnedCardLoaded: Boolean = false


    /**
     * Save the [Set] info from Firebase to memory.
     *
     * @param set the [Set] to be saved.
     * @param areOwnedIncluded the boolean to indicate if we got the card that the user own.
     */
    fun loadFirebaseSet(set: Set, areOwnedIncluded: Boolean) {
        areOwnedCardLoaded = areOwnedIncluded
        _setsInfo.add(set)
    }

    /**
     * Save the [List] of [Card] of the specific [Set] from Firebase to memory.
     *
     * @param setId the id of the [Set] that the [Card] belongs to.
     * @param cardList the [List] of [Card] to be saved.
     */
    fun loadFirebaseSetCardList(setId: String, cardList: MutableList<Card>) {
        _cardsInfo[setId] = cardList
    }

    /**
     * Function to retrieve the [List] of [Set] from memory.
     *
     * @return [List] of [Set]
     */
    fun getSetList(): List<Set> = if (areOwnedCardLoaded) _setsInfo else emptyList()

    /**
     * Function to retrieve the [Set] by its id from memory.
     *
     * @param setId the id of the [Set] to be retrieved.
     *
     * @return the specific [Set] filtered by its id.
     */
    fun getSetById(setId: String): Set = _setsInfo.first { it.id == setId }

    /**
     * Function to retrieve the [List] of [Card] of a specific [Set] from memory.
     *
     * @param setId the id of the [Set] that the [Card] belongs to.
     *
     * @return the [Flow] with the [List] of [Card] of the said [Set].
     */
    fun getSetCardList(setId: String): Flow<List<Card>> = flow {
        selectedCardList.clear()
        selectedCardList.addAll(_cardsInfo[setId] as List<Card>)
        emit(selectedCardList)
    }

    /**
     * Function to retrieve the [Card] by its id from memory.
     *
     * @param setId the id of the [Set] that the [Card] belongs to.
     * @param cardId the id of the [Card] to be retrieved.
     *
     * @return the [Flow] with the specific [Card] filtered by its id.
     */
    fun getCardById(setId: String, cardId: String): Flow<Card?> = flowOf(
        _cardsInfo[setId]?.first { it.id == cardId }
    )

    /**
     * Function to retrieve the [Card] of a custom list by its id from memory.
     *
     * @param setId the id of the [Set] that the [Card] belongs to.
     * @param cardId the id of the [Card] to be retrieved.
     *
     * @return the [Card] filtered by its id.
     */
    fun getCustomCardById(setId: String, cardId: String): Card =
        _cardsInfo[setId]?.first { it.id == cardId } as Card

    /**
     * Function to update the in memory [Card] ownership from user interaction.
     *
     * @param setId the id of the [Set] that the [Card] belongs to.
     * @param cardId the id of the [Card] to update the ownership.
     *
     * @return the [Flow] with the updated [List] after modifying the [Card] ownership.
     */
    fun updateCardOwnership(setId: String, cardId: String): Flow<List<Card>> = flow {
        val cardSelected = selectedCardList.first { it.id == cardId }
        val updatedCard = cardSelected.copy(owned = !cardSelected.owned)
        _cardsInfo[setId]?.remove(cardSelected)
        _cardsInfo[setId]?.add(updatedCard)
        selectedCardList.clear()
        selectedCardList.addAll(_cardsInfo[setId] as List<Card>)
        updateSetOwnershipCount(setId)
        emit(selectedCardList)
    }

    /**
     * Function to update the in memory [Card] ownership from Firebase.
     *
     * @param ownedCards the [List] of [Card] that the user owns.
     */
    fun updateLoadedOwnershipData(ownedCards: List<String>) {
        ownedCards.forEach { cardId ->
            updateCardOwned(cardId.split("-")[0], cardId)
            updateSetOwnershipCount(cardId.split("-")[0])
        }
        areOwnedCardLoaded = true
    }

    /**
     * Retrieves the count of completed sets from the local data source.
     *
     * @return A flow emitting the count of completed sets.
     */
    fun getCompletedSets(): Flow<Int> {
        val completedSets = _setsInfo.filter { it.ownedCards == it.total }
        return flowOf(completedSets.size)
    }

    /**
     * Retrieves all cards from the local data source.
     *
     * @return A flow emitting the list of all cards.
     */
    fun getCallCards(): Flow<List<Card>> = flow {
        val allCardList = mutableListOf<Card>()
        _cardsInfo.forEach { (_, cardList) ->
            allCardList.addAll(cardList)
        }
        emit(allCardList)
    }

    /**
     * Loads the user's achievements into the local data source.
     *
     * @param achievementList The list of achievements to load.
     */
    fun loadUserAchievements(achievementList: List<String>) {
        achievements.clear()
        achievements.addAll(achievementList)
    }

    /**
     * Checks if local resources are loaded.
     *
     * @return True if local resources are loaded, false otherwise.
     */
    fun areLocalResourcesLoaded(): Boolean = areOwnedCardLoaded

    /**
     * Retrieves the list of obtained achievements from the local data source.
     *
     * @return A list of obtained achievements.
     */
    fun getUserAchievements(): List<String> = achievements

    /**
     * Adds a new obtained achievement to the local data source.
     *
     * @param newAchievement The new achievement to add.
     */
    fun addObtainedAchievement(newAchievement: String) {
        achievements.add(newAchievement)
    }

    /**
     * Checks if the set is completed.
     *
     * @return True if the set is completed, false otherwise.
     */
    fun isSetCompleted(): Boolean {
        _setsInfo.forEach {
            if (it.ownedCards == it.total) return true
        }
        return false
    }

    /**
     * Updates the ownership status of a card to true in the local data source.
     *
     * @param setId The ID of the set the card belongs to.
     * @param cardId The ID of the card to update.
     */
    private fun updateCardOwned(setId: String, cardId: String) {
        val cardSelected = _cardsInfo[setId]?.first { it.id == cardId } as Card
        val updatedCard = cardSelected.copy(owned = true)
        _cardsInfo[setId]?.remove(cardSelected)
        _cardsInfo[setId]?.add(updatedCard)
    }

    /**
     * Updates the ownership count of a set in the local data source.
     *
     * @param setId The ID of the set to update the ownership count for.
     */
    private fun updateSetOwnershipCount(setId: String) {
        val selectedSet = _setsInfo.first { it.id == setId }
        val updatedSet = selectedSet.copy(ownedCards = _cardsInfo[setId]?.filter { it.owned }?.size ?: 0)
        _setsInfo.remove(selectedSet)
        _setsInfo.add(updatedSet)
    }

}
