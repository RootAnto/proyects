package com.example.pokectcollection.di.cards

import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.cards.CardRepository
import com.example.pokectcollection.domain.cards.usecase.GetAllCardsUseCase
import com.example.pokectcollection.domain.cards.usecase.GetCardListUseCase
import com.example.pokectcollection.domain.cards.usecase.GetCardUseCase
import com.example.pokectcollection.domain.cards.usecase.GetCustomCardListsUseCase
import com.example.pokectcollection.domain.user.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * HILT module [CardDomainModule] that provides the use cases.
 */
@InstallIn(SingletonComponent::class)
@Module
class CardDomainModule {

    /**
     * Factory function, that provides new instance of [GetCardUseCase] on every call.
     *
     * @param cardRepository the [CardRepository].
     */
    @Provides
    fun provideGetCardUseCase(
        cardRepository: CardRepository,
    ) = GetCardUseCase(cardRepository)

    /**
     * Factory function, that provides new instance of [GetCardListUseCase] on every call.
     *
     * @param cardRepository the [CardRepository].
     */
    @Provides
    fun provideGetCardListUseCase(
        cardRepository: CardRepository
    ) = GetCardListUseCase(cardRepository)

    /**
     * Factory function, that provides new instance of [GetCustomCardListsUseCase] on every call.
     *
     * @param authRepository the [AuthRepository].
     * @param userRepository the [UserRepository].
     * @param cardRepository the [CardRepository]
     */
    @Provides
    fun provideGetCustomCardListsUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository,
        cardRepository: CardRepository
    ) = GetCustomCardListsUseCase(authRepository, userRepository, cardRepository)

    @Provides
    fun provideGetAllCardsUseCase(
        cardRepository: CardRepository
    ) = GetAllCardsUseCase(cardRepository)
}
