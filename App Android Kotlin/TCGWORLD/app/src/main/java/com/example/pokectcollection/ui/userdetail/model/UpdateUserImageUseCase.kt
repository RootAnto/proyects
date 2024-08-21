package com.example.pokectcollection.ui.userdetail.model

import com.example.pokectcollection.domain.auth.AuthRepository
import com.example.pokectcollection.domain.user.UserRepository
import com.example.pokectcollection.domain.user.model.User
import javax.inject.Inject


/**
 * Use case [UpdateUserImageUseCase] for updating the user image.
 *
 * @property authRepository The [AuthRepository] to retrieve the current user ID.
 * @property userRepository The [UserRepository] to update the user image.
 */
class UpdateUserImageUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {

    /**
     * Invokes the use case to update the user image.
     *
     * @param userUpdated The [User] object containing the updated user information.
     */
    operator fun invoke(userUpdated: User) {
        val userId = authRepository.getCurrentUserId() as String
        userRepository.updateUserImage(userId, userUpdated)
    }
}
