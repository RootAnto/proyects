package com.example.pokectcollection.domain.user.usecase

import com.example.pokectcollection.domain.user.UserRepository
import com.example.pokectcollection.ui.login.model.UserData

/**
 * Use case to create a user document in the repository.
 *
 * @property userRepository The repository to handle user data operations.
 */
class CreateUserDocUseCase(
    private val userRepository: UserRepository
) {
    /**
     * Executes the use case to create a user document.
     *
     * @param googleUser The user data obtained from Google sign-in.
     */
    operator fun invoke(googleUser: UserData) =
        userRepository.createUserDoc(googleUser)
}
