package com.example.pokectcollection.ui.splash

import androidx.lifecycle.ViewModel
import com.example.pokectcollection.domain.user.usecase.LoadFirebaseResourcesUseCase
import com.example.pokectcollection.ui.splash.view.SplashScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the [SplashScreen].
 *
 * @property loadFirebaseResourcesUseCase Use case to load necessary Firebase resources.
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loadFirebaseResourcesUseCase: LoadFirebaseResourcesUseCase
) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)

    /**
     * Initialization block to load Firebase resources.
     */
    init {
        scope.launch {
            loadFirebaseResourcesUseCase()
            this.cancel("All firebase resources loaded, freeing android resources.")
        }
    }
}
