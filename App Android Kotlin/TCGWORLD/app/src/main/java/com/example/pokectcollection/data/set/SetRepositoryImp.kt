package com.example.pokectcollection.data.set

import com.example.pokectcollection.data.LocalDataSource
import com.example.pokectcollection.domain.setlist.SetRepository
import com.example.pokectcollection.domain.setlist.model.Set
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * The implementation of [SetRepository].
 *
 * @param localDataSource the [LocalDataSource] that provides [Set] data locally loaded.
 * @param firebaseSetDataSource the [FirebaseSetDataSource] that provides [Set] data from Firebase.
 */
class SetRepositoryImp @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val firebaseSetDataSource: FirebaseSetDataSource
) : SetRepository {

    /**
     * Retrieves the list of sets from Firebase.
     *
     * @return A list of sets retrieved from Firebase.
     */
    override suspend fun getFirebaseSets(): List<Set> =
        firebaseSetDataSource.getSetList()
            .await()
            .documents
            .map { it.toObject<FirebaseSet>()?.toDomainModel() as Set }

    /**
     * Retrieves the list of sets from the local data source.
     *
     * @return A list of sets retrieved from the local data source.
     */
    override fun getSets(): List<Set> = localDataSource.getSetList()

    /**
     * Retrieves a set by its ID from the local data source.
     *
     * @param id The ID of the set to retrieve.
     * @return The set with the specified ID.
     */
    override fun getSetById(id: String): Set = localDataSource.getSetById(id)
}
