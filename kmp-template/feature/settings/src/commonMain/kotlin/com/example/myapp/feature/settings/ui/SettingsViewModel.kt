package com.example.myapp.feature.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.feature.settings.domain.usecase.GetSettingsUseCase
import com.example.myapp.feature.settings.domain.usecase.UpdateThemeUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateThemeUseCase: UpdateThemeUseCase
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = getSettingsUseCase()
        .map { settings ->
            SettingsUiState(theme = settings.theme)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsUiState(isLoading = true)
        )

    fun onEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.ThemeChanged -> {
                viewModelScope.launch {
                    updateThemeUseCase(event.theme)
                }
            }
        }
    }
}
