package com.example.myapp.feature.settings.navigation

import com.coderwise.libs.navigation.NavEvent

sealed interface SettingsNavEvent : NavEvent {
    data object SignOut : SettingsNavEvent
}
