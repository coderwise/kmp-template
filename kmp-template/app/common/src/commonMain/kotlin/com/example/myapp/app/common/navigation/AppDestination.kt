package com.example.myapp.app.common.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

interface AppDestination : NavKey

@Serializable
data object HomeDestination : AppDestination

@Serializable
data object WeatherDestination : AppDestination

@Serializable
data object SettingsDestination : AppDestination
