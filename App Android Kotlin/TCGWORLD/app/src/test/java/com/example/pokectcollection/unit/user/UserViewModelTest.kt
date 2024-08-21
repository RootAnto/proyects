package com.example.pokectcollection.unit.user

import com.example.pokectcollection.data.user.GetProfileUserDataUseCase
import com.example.pokectcollection.domain.auth.usecase.SignOutUseCase
import com.example.pokectcollection.domain.user.model.ProfileUserInfo
import com.example.pokectcollection.domain.user.model.User
import com.example.pokectcollection.rule.MainCoroutineRule
import com.example.pokectcollection.ui.userdetail.UserDetailViewModel
import com.example.pokectcollection.ui.userdetail.model.UpdateUserImageUseCase
import com.example.pokectcollection.ui.userdetail.model.UserProfileUiState
import com.example.pokectcollection.ui.userdetail.model.toUiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {

    @get:Rule
    var mainCoroutine = MainCoroutineRule()

    private val updateUserImageUseCase = mockk<UpdateUserImageUseCase>()
    private val signOutUseCase = mockk<SignOutUseCase>()
    private val getProfileUserDataUseCase = mockk<GetProfileUserDataUseCase>()

    private fun createViewModel() = UserDetailViewModel(
        updateUserImageUseCase = updateUserImageUseCase,
        signOutUseCase = signOutUseCase,
        getProfileUserDataUseCase = getProfileUserDataUseCase
    )

    private val cardList: List<String> = listOf(
        "sv3pt5-1",
        "sv3pt5-2",
        "sv3pt5-3",
        "sv3pt5-4"
    )



    private fun getConvertedDate(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return format.format(date)
    }
}
