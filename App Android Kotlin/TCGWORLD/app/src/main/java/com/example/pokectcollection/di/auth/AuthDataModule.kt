package com.example.pokectcollection.di.auth

import com.example.pokectcollection.data.auth.AuthRepositoryImp
import com.example.pokectcollection.domain.auth.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * HILT module [AuthDataModule] that provides implementation for auth repositories interfaces.
 */
@InstallIn(SingletonComponent::class)
@Module
abstract class AuthDataModule {

    /**
     * Function that provides singleton implementation for [AuthRepository]
     */
    @Singleton
    @Binds
    abstract fun bindAuthRepository(authRepository: AuthRepositoryImp): AuthRepository
}
