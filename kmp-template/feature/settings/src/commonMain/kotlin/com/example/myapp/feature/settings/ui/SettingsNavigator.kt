package com.example.myapp.feature.settings.ui

sealed interface SettingsNavEvent {
    object Back : SettingsNavEvent
}
