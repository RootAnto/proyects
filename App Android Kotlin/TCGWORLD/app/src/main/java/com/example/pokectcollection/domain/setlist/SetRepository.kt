package com.example.pokectcollection.domain.setlist

import com.example.pokectcollection.domain.setlist.model.Set

/**
 * Interface [SetRepository] representing the repository for managing sets.
 */
interface SetRepository {

    /**
     * Retrieves a list of sets from Firebase.
     *
     * @return A list of [Set] objects.
     */
    suspend fun getFirebaseSets(): List<Set>

    /**
     * Retrieves a list of sets from the local data source.
     *
     * @return A list of [Set] objects.
     */
    fun getSets(): List<Set>

    /**
     * Retrieves a set by its identifier.
     *
     * @param id The identifier of the set.
     * @return A [Set] object corresponding to the given identifier.
     */
    fun getSetById(id: String): Set
}
