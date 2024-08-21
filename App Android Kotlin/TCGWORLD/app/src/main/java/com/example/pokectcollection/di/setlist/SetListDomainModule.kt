package com.example.pokectcollection.di.setlist

import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.cards.CardRepository
import com.example.pokectcollection.domain.setlist.SetRepository
import com.example.pokectcollection.domain.setlist.usecase.GetSetsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * HILT module [SetListDomainModule] that provides use cases.
 */
@InstallIn(SingletonComponent::class)
@Module
class SetListDomainModule {

    /**
     * Factory function, that provides new instance of [GetSetsUseCase] on every call.
     *
     * @param authRepository the [AuthRepository].
     * @param setRepository the [SetRepository].
     * @param cardRepository the [CardRepository].
     */
    @Provides
    fun provideGetSetsUseCase(
        authRepository: AuthRepository,
        setRepository: SetRepository,
        cardRepository: CardRepository
    ) = GetSetsUseCase(authRepository, setRepository, cardRepository)
}
