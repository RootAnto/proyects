package com.example.pokectcollection.di.auth

import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.auth.usecase.GetCurrentUserUseCase
import com.example.pokectcollection.domain.auth.usecase.LoginWithEmailAndPasswordUseCase
import com.example.pokectcollection.domain.auth.usecase.SignOutUseCase
import com.example.pokectcollection.domain.auth.usecase.SignUpWithEmailAndPasswordUseCase
import com.example.pokectcollection.domain.user.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * HILT module [AuthDomainModule] that provides use cases.
 */
@InstallIn(SingletonComponent::class)
@Module
class AuthDomainModule {

    /**
     * Factory function, that provides new instance of [GetCurrentUserUseCase] on every call.
     *
     * @param authRepository the [AuthRepository].
     * @param userRepository the [UserRepository].
     */
    @Provides
    fun provideGetCurrentUserUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository
    ) = GetCurrentUserUseCase(authRepository, userRepository)

    /**
     * Factory function, that provides new instance of [LoginWithEmailAndPasswordUseCase] on every call.
     *
     * @param authRepository the [AuthRepository].
     */
    @Provides
    fun provideLoginWithEmailAndPasswordUseCase(
        authRepository: AuthRepository,
    ) = LoginWithEmailAndPasswordUseCase(authRepository)

    /**
     * Factory function, that provides new instance of [SignUpWithEmailAndPasswordUseCase] on every call.
     *
     * @param authRepository the [AuthRepository].
     */
    @Provides
    fun provideSignUpWithEmailAndPasswordUseCase(
        authRepository: AuthRepository
    ) = SignUpWithEmailAndPasswordUseCase(authRepository)

    /**
     * Factory function, that provides new instance of [SignOutUseCase] on every call.
     *
     * @param authRepository the [AuthRepository].
     */
    @Provides
    fun provideSignOutUseCase(
        authRepository: AuthRepository
    ) = SignOutUseCase(authRepository)
}
