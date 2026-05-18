package com.example.myapp.app.common.navigation

import com.example.myapp.core.domain.model.ThemeType

data class AppUiState(
    val theme: ThemeType = ThemeType.SYSTEM
)
