package com.example.pokectcollection.domain.user

import com.example.pokectcollection.data.card.FirebaseCustomCardList
import com.example.pokectcollection.domain.cards.model.Card
import com.example.pokectcollection.domain.user.model.User
import com.example.pokectcollection.ui.login.model.UserData
import kotlinx.coroutines.flow.Flow

//TODO: update doc :D
/**
 * The repository [UserRepository] to manipulate the [User] data.
 */
@Suppress("TooManyFunctions")
interface UserRepository {

    /**
     * Register a new google user in FireStore.
     *
     * @param user the [UserData] to be registered in FireStore.
     */
    fun createUserDoc(user: UserData)

    /**
     * Get the [User] data from Firebase filtered by email.
     *
     * @return the [Flow] with the [User] data.
     */
    fun getUserByEmail(email: String): Flow<User?>

    /**
     * Get the [User] data from Firebase filtered by the Id.
     *
     * @return the [Flow] with the [User] data.
     */
    fun getUserById(userId: String): Flow<User?>

    /**
     * Update the user image in Firebase.
     *
     * @param userId the id of the [User] to update the profile picture.
     * @param userInfoUpdated the [User] info updated.
     */
    fun updateUserImage(userId: String, userInfoUpdated: User)

    /**
     * Update the google user data in Firebase.
     *
     * @param user the [UserData] to be updated in Firebase.
     */
    fun updateGoogleUser(user: UserData)

    /**
     * Get the [User] image by email.
     *
     * @param email the email of the [User] to get the profile picture.
     *
     * @return the [Flow] with the resource id of the profile picture.
     */
    fun getUserImageByEmail(email: String): Flow<Int?>

    /**
     * Get the [User] profile picture.
     *
     * @param userId the Id of the [User] to get the profile picture.
     *
     * @return the [Flow] with the string uri of the profile picture.
     */
    fun getUserProfilePicture(userId: String): Flow<String?>

    /**
     * Get the [User] custom lists of [Card].
     *
     * @param userId the Id of the [User] to get the custom lists.
     *
     * @return the [Flow] with the custom lists of [Card].
     */
    fun getUserCustomLists(userId: String): Flow<List<FirebaseCustomCardList>>

    /**
     * Update the [User] custom list.
     *
     * @param userId the Id of the [User] to update the custom list.
     * @param customList the custom list to be updated.
     */
    fun updateUserCustomLists(userId: String, listName: String, customList: List<String>)

    /**
     * Update the [User] information.
     *
     * @param userId the Id of the [User] to be updated.
     * @param userInfo the [User] info updated.
     */
    fun updateUserInfo(userId: String, userInfo: User)

    /**
     * Get the [User] owned cards.
     *
     * @param userId the Id of the [User] to get the owned cards.
     *
     * @return the [List] of [String] of the [User] owned cards.
     */
    suspend fun getUserOwnedCardList(userId: String): List<String>

    /**
     * Removes cards from a custom list.
     *
     * @param userId The ID of the user.
     * @param customListName The name of the custom list.
     * @param customCardIdList The list of card IDs to remove from the custom list.
     */
    suspend fun removeCardFromCustomList(
        userId: String,
        customListName: String,
        customCardIdList: List<String>
    )

    /**
     * Deletes a custom card list.
     *
     * @param userId The ID of the user.
     * @param listName The name of the custom list to delete.
     */
    suspend fun deleteCustomCardList(userId: String, listName: String)

    /**
     * Retrieves the list of achievements of a user.
     *
     * @param userId The ID of the user.
     * @return A list of achievement IDs.
     */
    suspend fun getUserAchievements(userId: String): List<String>

    /**
     * Updates a user's achievements.
     *
     * @param userId The ID of the user.
     * @param achievements The updated list of achievement IDs.
     */
    fun updateUserAchievements(userId: String, achievements: List<String>)

    /**
     * Updates the name of a custom list.
     *
     * @param userId The ID of the user.
     * @param oldName The old name of the custom list.
     * @param newName The new name of the custom list.
     */
    suspend fun updateCustomListName(userId: String, oldName: String, newName: String)

}
