package com.example.pokectcollection.data.user

import android.media.Image
import com.example.pokectcollection.domain.user.model.User
import com.example.pokectcollection.ui.login.model.UserData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The class [FirebaseUserDataSource] fun's of users
 * The data source for [User] in Firebase.
 */
@Singleton
class FirebaseUserDataSource @Inject constructor() {

    private val db = Firebase.firestore.collection(DB_USER_COLLECTION)

    /**
     * Function that get the [User] filtering by email.
     *
     * @param email the email to use for the query.
     *
     * @return the [Task] with the [QuerySnapshot] for getting the [User].
     */
    fun getUserByEmail(email: String): Task<QuerySnapshot> = db.whereEqualTo("email", email).get()

    /**
     * Function that get the [User] filtering by id.
     *
     * @param userId the id to use for the query.
     *
     * @return the [Task] with the [QuerySnapshot] for getting the [User].
     */
    fun getUserById(userId: String): Task<DocumentSnapshot> = db.document(userId).get()

    /**
     * Function to create data for the new [User] in Firebase.
     *
     * @param user the [UserData] to be created.
     */
    fun createUser(user: UserData) {
        db.document(user.userId).set(
            mapOf(
                "email" to user.email,
                "name" to user.username,
                "profilePictureUrl" to user.profilePictureUrl
            )
        )
    }

    /**
     * Function to create data for the new [User] in Firebase.
     *
     * @param user the [User] to be created.
     */
    fun createUser(user: User, userId: String) {
        val newUser = user.copy(profilePictureUrl = "Ash")
        db.document(userId).set(newUser)
    }

    /**
     * Function to update data for the [User] in Firebase.
     *
     * @param user the [UserData] to be updated.
     */
    fun updateUser(user: UserData) {
        db.document(user.userId).set(user)
            .addOnSuccessListener {
                println("DocumentSnapshot successfully updated!")
            }
            .addOnFailureListener { e ->
                println("Error updating document $e")
            }
    }

    /**
     * Function to update data for the [User] in Firebase.
     *
     * @param userId the id of the [User] to be updated.
     * @param user the [User] info to be updated.
     */
    fun updateUser(userId: String, user: User) {
        db.document(userId).set(user)
    }

    /**
     * Function to get the profile picture of the [User].
     *
     * @param userId the id of the [User] to be updated.
     *
     * @return the [Flow] with the [DocumentSnapshot] of the [User].
     */
    fun getUserProfilePicture(userId: String): Flow<DocumentSnapshot> =
        db.document(userId).snapshots()

    /**
     * Function to update the custom lists of the [User].
     *
     * @param userId the id of the [User] to be updated.
     * @param listName the name of the custom list to be updated.
     * @param customList the [List] to updated-
     */
    fun updateUserCustomLists(userId: String, listName: String, customList: List<String>) {
        db.document(userId).collection(DB_USER_CUSTOM_LIST_COLLECTION).document().set(
            mapOf(
                "name" to listName,
                "customList" to customList
            )
        )
    }

    /**
     * Function to get the custom lists of the [User].
     *
     * @param userId the id of the [User].
     *
     * @return the [Flow] with the [QuerySnapshot] of the [User] custom lists.
     */
    fun getUserCustomLists(userId: String): Flow<QuerySnapshot> =
        db.document(userId).collection(DB_USER_CUSTOM_LIST_COLLECTION).snapshots()

    // TODO: Update doc :D
    fun updateUserInfo(userId: String, userInfoUpdated: User) {
        db.document(userId).set(userInfoUpdated)
    }

    suspend fun updateUserCustomCardList(
        userId: String,
        customListName: String,
        customCardIdList: List<String>
    ) {
        val docs = db.document(userId).collection(DB_USER_CUSTOM_LIST_COLLECTION)
            .whereEqualTo("name", customListName).get().await()
        if (!docs.isEmpty)
            db.document(userId).collection(DB_USER_CUSTOM_LIST_COLLECTION)
                .document(docs.documents[0].id).set(
                    mapOf(
                        "name" to customListName,
                        "customList" to customCardIdList
                    )
                )
    }

    suspend fun deleteCustomCardList(userId: String, listName: String) {
        val docs = db.document(userId).collection(DB_USER_CUSTOM_LIST_COLLECTION)
            .whereEqualTo("name", listName).get().await()
        if (!docs.isEmpty)
            db.document(userId).collection(DB_USER_CUSTOM_LIST_COLLECTION)
                .document(docs.documents[0].id).delete()
    }

    fun updateUserAchievements(userId: String, achievements: List<String>) {
        db.document(userId).update(
            mapOf(
                "achievementList" to achievements
            )
        )
    }

    suspend fun updateCustomListName(userId: String, oldName: String, newName: String) {
        val docId = db.document(userId).collection(DB_USER_CUSTOM_LIST_COLLECTION)
            .whereEqualTo("name",oldName).get().await().documents[0].id
        db.document(userId).collection(DB_USER_CUSTOM_LIST_COLLECTION).document(docId).update(
            mapOf(
                "name" to newName
            )
        )
    }

    companion object {

        /**
         * Key to users in the database.
         */
        const val DB_USER_COLLECTION = "usuario"

        /**
         * key for sub collections of card list of the user
         */
        const val DB_USER_CUSTOM_LIST_COLLECTION = "customCardList"
    }
}
