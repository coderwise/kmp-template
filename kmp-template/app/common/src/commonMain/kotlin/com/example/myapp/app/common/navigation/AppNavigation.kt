package com.example.myapp.app.common.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.myapp.core.ui.theme.MyAppTheme
import com.example.myapp.feature.home.ui.HomeScreen
import com.example.myapp.feature.weather.ui.WeatherScreen
import com.example.myapp.libs.utils.PlatformColors

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
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            popTransitionSpec = {
                slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
            },
            predictivePopTransitionSpec = {
                slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
            },
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
