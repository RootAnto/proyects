package com.example.pokectcollection.data.achievement

import com.example.pokectcollection.domain.achievements.model.Achievement
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The class [FirebaseAchievementDataSource]
 * The data source for authentication in Firebase.
 */
@Singleton
class FirebaseAchievementDataSource @Inject constructor() {

    private val db = Firebase.firestore.collection(DB_SET_COLLECTION)

    /**
     * Function to get the list of [Achievement] from Firebase.
     *
     */
    fun getAchievementList(): Task<QuerySnapshot> = db.get()

    fun getAchievementByName(name: String) = db.whereEqualTo("name", name).get()

    companion object {

        /**
         * Key for achievements in database.
         */
        const val DB_SET_COLLECTION = "logros"
    }
}
