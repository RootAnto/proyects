package com.example.pokectcollection.data.user

import com.example.pokectcollection.data.card.FirebaseCustomCardList
import com.example.pokectcollection.domain.user.UserRepository
import com.example.pokectcollection.domain.user.model.User
import com.example.pokectcollection.ui.login.model.UserData
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * The implementation of [UserRepository].
 *
 * @param firebaseUserDataSource the [FirebaseUserDataSource] that provides and manage [User] data from Firebase.
 */
@Suppress("TooManyFunctions")
class UserRepositoryImp @Inject constructor(
    private val firebaseUserDataSource: FirebaseUserDataSource
) : UserRepository {

    /**
     * Creates a user document if it does not already exist.
     *
     * @param user The user data to create the document for.
     */
    override fun createUserDoc(user: UserData) {
        firebaseUserDataSource.getUserById(user.userId).addOnSuccessListener {
            if (!it.exists()) {
                firebaseUserDataSource.createUser(user)
            }
        }
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email The email of the user to retrieve.
     * @return A flow emitting the user data or null if not found.
     */
    override fun getUserByEmail(email: String): Flow<User?> = flow {
        runCatching {
            firebaseUserDataSource.getUserByEmail(email).await()
        }.onSuccess { userDocs ->
            if (userDocs.isEmpty) {
                emit(null) // Emit null if no documents are found
            } else {
                val userDoc = userDocs.first()
                val userData = userDoc.toObject<User>()
                emit(userData)
            }
        }.onFailure { error ->
            println("error message ${error.message}")
            emit(null) // Emit null in case of error
        }
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return A flow emitting the user data or null if not found.
     */
    override fun getUserById(userId: String): Flow<User?> = flow {
        runCatching {
            firebaseUserDataSource.getUserById(userId).await()
        }.onSuccess { userDoc ->
            val user = userDoc.toObject<User>()
            emit(user)
        }.onFailure { error ->
            println("error message ${error.message}")
            emit(null)
        }
    }

    /**
     * Retrieves the user's image resource ID by their email.
     *
     * @param email The email of the user.
     * @return A flow emitting the image resource ID or null if not found.
     */
    override fun getUserImageByEmail(email: String): Flow<Int?> = flow {
        runCatching {
            firebaseUserDataSource.getUserByEmail(email).await()
        }.onSuccess { userDocs ->
            if (userDocs.isEmpty) {
                emit(null)
            } else {
                val userDoc = userDocs.first()
                val userImageResId = userDoc.getString("imageResId")?.toIntOrNull()
                emit(userImageResId)
            }
        }.onFailure {
            emit(null)
        }
    }

    /**
     * Updates the user's image.
     *
     * @param userId The ID of the user.
     * @param userInfoUpdated The updated user information.
     */
    override fun updateUserImage(userId: String, userInfoUpdated: User) {
        firebaseUserDataSource.updateUserInfo(userId, userInfoUpdated)
    }

    /**
     * Updates a Google user's information.
     *
     * @param user The user data to update.
     */
    override fun updateGoogleUser(user: UserData) {
        firebaseUserDataSource.updateUser(user)
    }

    /**
     * Retrieves the profile picture URL of a user by their ID.
     *
     * @param userId The ID of the user.
     * @return A flow emitting the profile picture URL.
     */
    override fun getUserProfilePicture(userId: String): Flow<String?> =
        firebaseUserDataSource.getUserProfilePicture(userId)
            .map { querySnapshot -> querySnapshot.toObject<User>()?.profilePictureUrl }

    /**
     * Retrieves the custom card lists of a user by their ID.
     *
     * @param userId The ID of the user.
     * @return A flow emitting the list of custom card lists.
     */
    override fun getUserCustomLists(userId: String): Flow<List<FirebaseCustomCardList>> =
        firebaseUserDataSource.getUserCustomLists(userId).map { documents ->
            documents.map {
                it.toObject<FirebaseCustomCardList>()
            }
        }

    /**
     * Updates a user's custom card lists.
     *
     * @param userId The ID of the user.
     * @param listName The name of the custom list.
     * @param customList The updated custom list.
     */
    override fun updateUserCustomLists(userId: String, listName: String, customList: List<String>) {
        firebaseUserDataSource.updateUserCustomLists(userId, listName, customList)
    }

    /**
     * Updates a user's information.
     *
     * @param userId The ID of the user.
     * @param userInfo The updated user information.
     */
    override fun updateUserInfo(userId: String, userInfo: User) {
        firebaseUserDataSource.updateUser(userId = userId, user = userInfo)
    }


    /**
     * Retrieves the list of owned cards of a user by their ID.
     *
     * @param userId The ID of the user.
     * @return A list of owned card IDs.
     */
    override suspend fun getUserOwnedCardList(userId: String): List<String> =
        firebaseUserDataSource.getUserById(userId)
            .await()
            .toObject<User>()
            ?.cardList
            .orEmpty()

    /**
     * Removes cards from a custom list.
     *
     * @param userId The ID of the user.
     * @param customListName The name of the custom list.
     * @param customCardIdList The list of card IDs to remove.
     */
    override suspend fun removeCardFromCustomList(
        userId: String,
        customListName: String,
        customCardIdList: List<String>
    ) {
        firebaseUserDataSource.updateUserCustomCardList(
            userId,
            customListName,
            customCardIdList
        )
    }

    /**
     * Deletes a custom card list.
     *
     * @param userId The ID of the user.
     * @param listName The name of the list to delete.
     */
    override suspend fun deleteCustomCardList(userId: String, listName: String) {
        firebaseUserDataSource.deleteCustomCardList(userId, listName)
    }

    /**
     * Retrieves the list of achievements of a user by their ID.
     *
     * @param userId The ID of the user.
     * @return A list of achievement IDs.
     */
    override suspend fun getUserAchievements(userId: String): List<String> =
        firebaseUserDataSource.getUserById(userId).await()
            .toObject<User>()?.achievementList.orEmpty()

    /**
     * Updates a user's achievements.
     *
     * @param userId The ID of the user.
     * @param achievements The updated list of achievements.
     */
    override fun updateUserAchievements(userId: String, achievements: List<String>) {
        firebaseUserDataSource.updateUserAchievements(userId, achievements)
    }

    /**
     * Updates the name of a custom list.
     *
     * @param userId The ID of the user.
     * @param oldName The old name of the custom list.
     * @param newName The new name of the custom list.
     */
    override suspend fun updateCustomListName(userId: String, oldName: String, newName: String) {
        firebaseUserDataSource.updateCustomListName(userId, oldName, newName)
    }

}
