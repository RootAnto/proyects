package com.example.pokectcollection.data.achievement

import com.example.pokectcollection.domain.achievements.AchievementRepository
import com.example.pokectcollection.domain.achievements.model.Achievement
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
/**
 * The implementation of [AchievementRepository].
 *
 * @param firebaseDataSource the [FirebaseAchievementDataSource] that provides [Achievement] data from Firebase.
 */
class AchievementRepositoryImp @Inject constructor(
    private val firebaseDataSource: FirebaseAchievementDataSource
) : AchievementRepository {

    /**
     * Retrieves the list of achievements from the Firebase data source.
     *
     * @return A flow emitting the list of achievements.
     */
    override fun getAchievementList(): Flow<List<Achievement>> = flow {
        runCatching {
            firebaseDataSource.getAchievementList().await()
        }.onSuccess { result ->
            val achievementList = mutableListOf<Achievement>()
            for (document in result) {
                achievementList.add(document.toObject<Achievement>())
            }
            emit(achievementList)
        }.onFailure {
            println("Error getting achievements")
            emit(emptyList())
        }
    }

    /**
     * Retrieves an achievement by its name from the Firebase data source.
     *
     * @param name The name of the achievement to retrieve.
     * @return The achievement with the specified name.
     */
    override suspend fun getAchievementByName(name: String): Achievement =
        firebaseDataSource.getAchievementByName(name).await().toObjects<Achievement>()[0]

}

