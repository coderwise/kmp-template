package com.example.myapp.feature.settings.navigation

sealed interface SettingsNavEvent {
    data object SignOut : SettingsNavEvent
}
