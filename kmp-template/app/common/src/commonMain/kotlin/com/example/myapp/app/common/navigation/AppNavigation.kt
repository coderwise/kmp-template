package com.example.myapp.app.common.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.myapp.core.domain.model.Settings
import com.example.myapp.core.domain.model.ThemeType
import com.example.myapp.core.domain.repository.SettingsRepository
import com.example.myapp.core.ui.theme.MyAppTheme
import com.example.myapp.core.ui.util.pop
import com.example.myapp.core.ui.util.push
import com.example.myapp.feature.home.navigation.HomeNavigation
import com.example.myapp.feature.settings.ui.SettingsScreen
import com.example.myapp.feature.weather.ui.WeatherScreen
import com.example.myapp.libs.utils.PlatformColors
import org.koin.compose.koinInject

@Composable
fun AppNavigation() {
    val settingsRepository = koinInject<SettingsRepository>()
    val settings by settingsRepository.observeSettings().collectAsState(initial = Settings())

    val darkTheme = when (settings.theme) {
        ThemeType.LIGHT -> false
        ThemeType.DARK -> true
        ThemeType.SYSTEM -> isSystemInDarkTheme()
    }

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
                    HomeNavigation(
                        onWeatherClick = {
                            backStack.push(WeatherDestination)
                        },
                        onSettingsClick = {
                            backStack.push(SettingsDestination)
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
                entry<SettingsDestination> {
                    SettingsScreen(
                        onBackClick = {
                            backStack.pop()
                        }
                    )
                }
            }
        )
    }
}
