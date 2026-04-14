package com.example.myapp.app.common.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.myapp.app.common.theme.PlatformColors
import com.example.myapp.core.ui.MyAppTheme
import com.example.myapp.feature.home.ui.HomeScreen
import com.example.myapp.feature.weather.ui.WeatherScreen

@Composable
fun AppNavigation(darkTheme: Boolean = isSystemInDarkTheme()) {
    val config = SavedStateConfiguration {
        serializersModule = appNavSerializersModule
    }
    val backStack = rememberNavBackStack(config, HomeDestination)
    MyAppTheme(darkTheme) {
        PlatformColors(darkTheme)
        NavDisplay(
            backStack = backStack,
            entryProvider = entryProvider {
                entry<HomeDestination> {
                    HomeScreen(
                        onWeatherClick = {
                            backStack.push(WeatherDestination)
                        }
                    )
                }
                entry<WeatherDestination> {
                    WeatherScreen(
                        onBackClick = {
                            backStack.pop()
                        }
                    )
                }
            }
        )
    }
}

private fun <T : Any> MutableList<T>.push(element: T) {
    add(element)
}

private fun <T : Any> MutableList<T>.pop() {
    if (isNotEmpty()) {
        removeAt(size - 1)
    }
}
