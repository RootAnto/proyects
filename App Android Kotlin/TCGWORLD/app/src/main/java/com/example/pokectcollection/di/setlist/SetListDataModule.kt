package com.example.pokectcollection.di.setlist

import com.example.pokectcollection.data.set.SetRepositoryImp
import com.example.pokectcollection.domain.setlist.SetRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * HILT module [SetListDataModule] that provides implementation for set repositories interfaces.
 */
@InstallIn(SingletonComponent::class)
@Module
abstract class SetListDataModule {

    /**
     * Function that provides singleton implementation for [SetRepository].
     */
    @Singleton
    @Binds
    abstract fun bindSetRepository(setRepository: SetRepositoryImp): SetRepository
}
