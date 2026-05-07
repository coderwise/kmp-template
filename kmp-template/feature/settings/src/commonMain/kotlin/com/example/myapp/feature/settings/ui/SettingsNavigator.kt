package com.example.myapp.feature.settings.ui

import com.example.myapp.core.ui.navigation.Navigator

sealed interface SettingsNavEvent

interface SettingsNavigator : Navigator<SettingsNavEvent>
