package com.example.pokectcollection.di.user

import com.example.pokectcollection.data.user.GetProfileUserDataUseCase
import com.example.pokectcollection.domain.achievements.AchievementRepository
import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.cards.CardRepository
import com.example.pokectcollection.domain.setlist.SetRepository
import com.example.pokectcollection.domain.user.UserRepository
import com.example.pokectcollection.domain.user.usecase.CheckAchievementsUseCase
import com.example.pokectcollection.domain.user.usecase.CreateCustomCardListUseCase
import com.example.pokectcollection.domain.user.usecase.CreateUserDocUseCase
import com.example.pokectcollection.domain.user.usecase.DeleteCustomCardListUseCase
import com.example.pokectcollection.domain.user.usecase.GetUserOwnedCardsUseCase
import com.example.pokectcollection.domain.user.usecase.GetUserProfilePictureUseCase
import com.example.pokectcollection.domain.user.usecase.LoadFirebaseResourcesUseCase
import com.example.pokectcollection.domain.user.usecase.RemoveCardFromCustomListUseCase
import com.example.pokectcollection.domain.user.usecase.SaveCustomListUseCase
import com.example.pokectcollection.domain.user.usecase.UpdateCardOwnershipUseCase
import com.example.pokectcollection.domain.user.usecase.UpdateCustomListNameUseCase
import com.example.pokectcollection.domain.user.usecase.UpdateSetAchievementUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * HILT module [UserDomainModule] that provides use cases.
 */
@InstallIn(SingletonComponent::class)
@Module
class UserDomainModule {

    /**
     * Factory function, that provides new instance of [CreateUserDocUseCase] on every call.
     *
     * @param userRepository the [UserRepository].
     */
    @Provides
    fun provideCreateUserDocUseCaseUseCase(
        userRepository: UserRepository
    ) = CreateUserDocUseCase(userRepository)

    /**
     * Factory function, that provides new instance of [GetUserProfilePictureUseCase] on every call.
     *
     * @param authRepository the [AuthRepository].
     * @param userRepository the [UserRepository].
     */
    @Provides
    fun provideGetUserProfilePictureUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository
    ) = GetUserProfilePictureUseCase(authRepository, userRepository)

    /**
     * Factory function, that provides new instance of [GetProfileUserDataUseCase] on every call.
     *
     * @param authRepository the [AuthRepository].
     * @param userRepository the [UserRepository].
     */
    @Provides
    fun provideGetProfileUserDataUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository,
        cardRepository: CardRepository
    ) = GetProfileUserDataUseCase(authRepository, userRepository, cardRepository)

    // TODO: not imported usecase :D
    /**
     * Factory function, that provides new instance of [UpdateCardOwnershipUseCasee] on every call.
     *
     * @param authRepository the [AuthRepository].
     * @param userRepository the [UserRepository].
     * @param cardRepository the [CardRepository].
     */
    @Provides
    fun provideUpdateCardOwnershipUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository,
        cardRepository: CardRepository
    ) = UpdateCardOwnershipUseCase(authRepository, userRepository, cardRepository)

    /**
     * Factory function, that provides new instance of [GetUserOwnedCardsUseCase] on every call.
     *
     * @param authRepository the [AuthRepository].
     * @param userRepository the [UserRepository].
     */
    @Provides
    fun provideGetUserOwnedCardsUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository
    ) = GetUserOwnedCardsUseCase(authRepository, userRepository)

    /**
     * Factory function, that provides new instance of [CreateCustomCardListUseCase] on every call.
     *
     * @param authRepository the [AuthRepository].
     * @param userRepository the [UserRepository].
     */
    @Provides
    fun provideCreateCustomCardListUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository
    ) = CreateCustomCardListUseCase(authRepository, userRepository)

    /**
     * Factory function, that provides new instance of [LoadFirebaseResourcesUseCase] on every call.
     *
     * @param authRepository the [AuthRepository].
     * @param setRepository the [SetRepository].
     * @param cardRepository the [CardRepository].
     * @param userRepository the [UserRepository].
     */
    @Provides
    fun provideLoadFirebaseResourcesUseCase(
        authRepository: AuthRepository,
        setRepository: SetRepository,
        cardRepository: CardRepository,
        userRepository: UserRepository
    ) = LoadFirebaseResourcesUseCase(authRepository, setRepository, cardRepository, userRepository)

    /**
     * Factory function, that provides new instance of [LoadFirebaseResourcesUseCase] on every call.
     *
     * @param authRepository the [AuthRepository].
     * @param setRepository the [SetRepository].
     * @param cardRepository the [CardRepository].
     * @param userRepository the [UserRepository].
     */
    @Provides
    fun provideRemoveCardFromCustomListUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository
    ) = RemoveCardFromCustomListUseCase(authRepository, userRepository)

    /**
     * Factory function, that provides new instance of [SaveCustomListUseCase] on every call.
     *
     * @param authRepository the [AuthRepository].
     * @param userRepository the [UserRepository].
     */
    @Provides
    fun provideSaveCustomListUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository
    ) = SaveCustomListUseCase(authRepository, userRepository)

    /**
     * Factory function, that provides new instance of [DeleteCustomCardListUseCase] on every call.
     *
     * @param authRepository the [AuthRepository].
     * @param userRepository the [UserRepository].
     */
    @Provides
    fun provideDeleteCustomCardListUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository
    ) = DeleteCustomCardListUseCase(authRepository, userRepository)

    /**
     * Factory function, that provides new instance of [CheckAchievementsUseCase] on every call.
     *
     * @param authRepository the [AuthRepository].
     * @param userRepository the [UserRepository].
     * @param cardRepository the [CardRepository].
     * @param achievementRepository the [AchievementRepository].
     */
    @Provides
    fun provideCheckAchievementsUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository,
        cardRepository: CardRepository,
        achievementRepository: AchievementRepository
    ) = CheckAchievementsUseCase(authRepository, userRepository, cardRepository, achievementRepository)

    /**
     * Factory function, that provides new instance of [UpdateCustomListNameUseCase] on every call.
     *
     * @param authRepository the [AuthRepository].
     * @param userRepository the [UserRepository].
     */
    @Provides
    fun provideUpdateCustomListNameUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository
    ) = UpdateCustomListNameUseCase(authRepository, userRepository)

    /**
     * Factory function, that provides new instance of [UpdateSetAchievementUseCase] on every call.
     *
     * @param authRepository the [AuthRepository].
     * @param userRepository the [UserRepository].
     * @param cardRepository the [CardRepository].
     */
    @Provides
    fun provideUpdateSetAchievementUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository,
        cardRepository: CardRepository
    ) = UpdateSetAchievementUseCase(authRepository, userRepository, cardRepository)

}
