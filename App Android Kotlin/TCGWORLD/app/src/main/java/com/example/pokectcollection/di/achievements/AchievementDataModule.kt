package com.example.pokectcollection.di.achievements

import com.example.pokectcollection.data.achievement.AchievementRepositoryImp
import com.example.pokectcollection.domain.achievements.AchievementRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * HILT module [AchievementDataModule] that provides implementation for achievement repositories interfaces.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AchievementDataModule {

    /**
     * Function that provides singleton implementation for [AchievementRepository].
     */
    @Binds
    @Singleton
    abstract fun bindAchievementRepository(
        achievementRepositoryImp: AchievementRepositoryImp
    ): AchievementRepository
}
