package com.example.pokectcollection.data.card

import com.example.pokectcollection.data.set.FirebaseSet
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The data source for [FirebaseCard] data.
 */
@Singleton
class FirebaseCardDataSource @Inject constructor() {

    private val db = Firebase.firestore.collection(DB_SET_COLLECTION)

    /**
     *  Function to make a query for getting a Card filtering by id.
     *
     *  @param cardId the id of the [FirebaseCard].
     *  @param setId the id of the [FirebaseSet]
     *
     *  @return the [Task] of the [QuerySnapshot].
     */
    fun getCardById(cardId: String, setId: String): Task<QuerySnapshot> = db
        .document(setId)
        .collection(DB_CARD_COLLECTION)
        .whereEqualTo("id", cardId).get()

    /**
     * Function that make a query to firebase for getting the list of cards of a specific set.
     *
     * @param setId the id of the [FirebaseSet].
     *
     * @return the [Task] of the [QuerySnapshot] for getting the list of cards of the set.
     */
    fun getCardList(setId: String): Task<QuerySnapshot> =
        db.document(setId).collection(DB_CARD_COLLECTION).get()

    companion object {
        /**
         * Firebase database collection id for sets.
         */
        const val DB_SET_COLLECTION = "sets"

        /**
         * Firebase database collection id for cards.
         */
        const val DB_CARD_COLLECTION = "cartas"
    }
}
