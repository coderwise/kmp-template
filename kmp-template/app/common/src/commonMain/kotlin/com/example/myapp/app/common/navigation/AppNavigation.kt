package com.example.myapp.app.common.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.myapp.core.common.HomeDestination
import com.example.myapp.core.common.appNavSerializersModule
import com.example.myapp.core.ui.MyAppTheme
import com.example.myapp.feature.home.ui.HomeScreen

@Composable
fun AppNavigation() {
    val config = SavedStateConfiguration {
        serializersModule = appNavSerializersModule
    }
    val backStack = rememberNavBackStack(config, HomeDestination)
    MyAppTheme {
        NavDisplay(
            backStack = backStack,
            entryProvider = entryProvider {
                entry<HomeDestination> { HomeScreen() }
            }
        )
    }
}
