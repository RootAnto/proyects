package com.example.pokectcollection.ui.setlist

import androidx.lifecycle.ViewModel
import com.example.pokectcollection.domain.setlist.usecase.GetSetsUseCase
import com.example.pokectcollection.ui.setlist.model.SetListUiState
import com.example.pokectcollection.ui.setlist.model.toUiState
import com.example.pokectcollection.ui.setlist.view.SetListScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The ViewModel for [SetListScreen].
 *
 * @property getSetUseCase the use case for retrieving the sets.
 */
@HiltViewModel
class SetListViewModel @Inject constructor(
    private val getSetUseCase: GetSetsUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<SetListUiState> =
        MutableStateFlow(SetListUiState.getEmptyState())

    /**
     * [StateFlow] to be collected in the UI for displaying the list of sets.
     */
    val uiState: StateFlow<SetListUiState> = _uiState.asStateFlow()

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    /**
     * Refreshes the data by invoking the [GetSetsUseCase] to retrieve the latest set list.
     */
    fun refreshData() {
        scope.launch {
            getSetUseCase().collect { setList ->
                _uiState.value = SetListUiState(
                    setList.map { it.toUiState() }
                        .sortedByDescending { it.releaseDate }
                )
            }
        }
    }
}
