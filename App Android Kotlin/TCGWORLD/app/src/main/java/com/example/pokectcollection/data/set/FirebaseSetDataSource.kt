package com.example.pokectcollection.data.set

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The data source for [FirebaseSet] data.
 */
@Singleton
class FirebaseSetDataSource @Inject constructor() {

    private val db = Firebase.firestore.collection(DB_SET_COLLECTION)

    /**
     * Function to get the list of set in the database.
     *
     * @return the [Task] with the query to firebase ([QuerySnapshot]).
     */
    fun getSetList(): Task<QuerySnapshot> = db.get()

    /**
     * Function to get a specific [FirebaseSet], filtering by id.
     *
     * @param id the id for filtering in the database.
     */
    fun getSetById(id: String): Task<DocumentSnapshot> = db.document(id).get()

    companion object {

        /**
         * They key for the sets in the database.
         */
        const val DB_SET_COLLECTION = "sets"
    }
}
