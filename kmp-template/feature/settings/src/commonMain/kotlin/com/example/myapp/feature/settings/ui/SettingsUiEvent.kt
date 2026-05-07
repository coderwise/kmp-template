package com.example.myapp.feature.settings.ui

import com.example.myapp.core.domain.model.ThemeType

sealed interface SettingsUiEvent {
    data class ThemeChanged(val theme: ThemeType) : SettingsUiEvent
    data object NavigateBack : SettingsUiEvent
}
