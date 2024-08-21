package com.example.pokectcollection.di.cards

import com.example.pokectcollection.data.card.CardRepositoryImp
import com.example.pokectcollection.domain.cards.CardRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * HILT module [CardDataModule] that provides implementation for card repositories interfaces.
 */
@InstallIn(SingletonComponent::class)
@Module
abstract class CardDataModule {

    /**
     * Function that provides singleton implementation for [CardRepository]
     */
    @Singleton
    @Binds
    abstract fun bindCardRepository(cardRepository: CardRepositoryImp): CardRepository
}
