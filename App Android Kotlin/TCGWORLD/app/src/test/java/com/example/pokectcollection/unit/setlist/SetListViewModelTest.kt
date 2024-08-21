package com.example.pokectcollection.unit.setlist

import com.example.pokectcollection.domain.setlist.model.Set
import com.example.pokectcollection.domain.setlist.model.SetImages
import com.example.pokectcollection.domain.setlist.model.SetLegalities
import com.example.pokectcollection.domain.setlist.usecase.GetSetsUseCase
import com.example.pokectcollection.rule.MainCoroutineRule
import com.example.pokectcollection.ui.setlist.SetListViewModel
import com.example.pokectcollection.ui.setlist.model.toUiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SetListViewModelTest {
    @get:Rule
    var mainCoroutine = MainCoroutineRule()

    private val getSetsUseCase = mockk<GetSetsUseCase>()

    private fun createViewModel() = SetListViewModel(
        getSetUseCase = getSetsUseCase
    )

    private val _sampleSet = Set(
        id = "",
        name = "",
        series = "",
        total = 0,
        legalities = SetLegalities.getEmptySetLegalities(),
        releaseDate = "",
        images = SetImages.getEmptySetImages(),
        ownedCards = 0
    )

   
}
