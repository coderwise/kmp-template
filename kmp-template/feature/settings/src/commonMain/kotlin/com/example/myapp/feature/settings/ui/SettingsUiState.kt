package com.example.myapp.feature.settings.ui

import com.example.myapp.core.domain.model.ThemeType

data class SettingsUiState(
    val theme: ThemeType = ThemeType.SYSTEM,
    val appVersion: String = "",
    val isLoading: Boolean = false
)
