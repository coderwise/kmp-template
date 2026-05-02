package com.example.myapp.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    val theme: ThemeType = ThemeType.SYSTEM,
    val showDebugInfo: Boolean = false,
    val selectedLocation: Location? = null
)