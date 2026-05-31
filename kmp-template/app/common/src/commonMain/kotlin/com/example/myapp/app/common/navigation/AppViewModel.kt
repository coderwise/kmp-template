package com.example.myapp.app.common.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.core.domain.repository.SettingsRepository
import com.example.myapp.core.ui.state.StateConfig
import com.example.myapp.feature.auth.domain.model.AuthState
import com.example.myapp.feature.auth.domain.usecase.ObserveAuthStateUseCase
import com.example.myapp.feature.auth.domain.usecase.SignOutUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(
    settingsRepository: SettingsRepository,
    observeAuthStateUseCase: ObserveAuthStateUseCase,
    private val signOutUseCase: SignOutUseCase,
) : ViewModel() {

    val uiState: StateFlow<AppUiState> = combine(
        settingsRepository.observeSettings(),
        observeAuthStateUseCase(),
    ) { settings, authState ->
        AppUiState(
            theme = settings.theme,
            isAuthenticated = authState is AuthState.Authenticated,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(StateConfig.SubscriptionTimeoutMs),
        initialValue = AppUiState(),
    )

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase()
        }
    }
}
