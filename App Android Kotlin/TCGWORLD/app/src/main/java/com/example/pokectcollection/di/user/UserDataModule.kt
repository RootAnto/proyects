package com.example.pokectcollection.di.user

import com.example.pokectcollection.data.user.UserRepositoryImp
import com.example.pokectcollection.domain.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * HILT module [UserDataModule] that provides implementation for user repositories interfaces.
 */
@InstallIn(SingletonComponent::class)
@Module
abstract class UserDataModule {

    /**
     * Function that provides singleton implementation for [UserRepository]
     */
    @Singleton
    @Binds
    abstract fun bindUserRepository(userRepository: UserRepositoryImp): UserRepository
}
