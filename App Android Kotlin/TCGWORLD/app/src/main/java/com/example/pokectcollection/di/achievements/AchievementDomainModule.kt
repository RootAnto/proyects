package com.example.pokectcollection.di.achievements

import com.example.pokectcollection.domain.achievements.AchievementRepository
import com.example.pokectcollection.domain.achievements.usecase.GetAchievementListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * HILT module [AchievementDomainModule] that provides use cases.
 */
@Module
@InstallIn(SingletonComponent::class)
class AchievementDomainModule {

    /**
     * Factory function, that provides new instance of [GetAchievementListUseCase] on every call.
     *
     * @param achievementRepository the [AchievementRepository].
     */
    @Provides
    fun provideGetAchievementListUseCase(
        achievementRepository: AchievementRepository
    ): GetAchievementListUseCase {
        return GetAchievementListUseCase(achievementRepository)
    }
}
