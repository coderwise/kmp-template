package com.example.myapp.app.common.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.core.domain.repository.SettingsRepository
import com.example.myapp.core.ui.state.StateConfig
import com.example.myapp.feature.auth.domain.model.AuthState
import com.example.myapp.feature.auth.domain.usecase.ObserveAuthStateUseCase
import com.example.myapp.feature.auth.domain.usecase.SignOutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(
    settingsRepository: SettingsRepository,
    observeAuthStateUseCase: ObserveAuthStateUseCase,
    private val signOutUseCase: SignOutUseCase,
) : ViewModel() {

    private val _selectedTab = MutableStateFlow(0)

    val uiState: StateFlow<AppUiState> = combine(
        settingsRepository.observeSettings(),
        observeAuthStateUseCase(),
        _selectedTab,
    ) { settings, authState, selectedTab ->
        AppUiState(
            theme = settings.theme,
            isAuthenticated = authState is AuthState.Authenticated,
            selectedTab = selectedTab,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(StateConfig.SubscriptionTimeoutMs),
        initialValue = AppUiState(),
    )

    /**
     * False until the saved settings and auth state have loaded. `isAuthenticated` is the marker:
     * it stays null until the combined flow first emits, at which point `theme` is the saved one
     * too. The Android splash is held until then, so no frame is drawn in the wrong theme.
     */
    val isReady: Boolean get() = uiState.value.isAuthenticated != null

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase()
        }
    }

    fun setTab(index: Int) {
        _selectedTab.update { index }
    }
}
