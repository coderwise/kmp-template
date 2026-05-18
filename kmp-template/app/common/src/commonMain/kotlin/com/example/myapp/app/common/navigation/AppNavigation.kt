package com.example.myapp.app.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.myapp.core.domain.model.ThemeType
import com.example.myapp.core.ui.navigation.GlobalNavEvent
import com.example.myapp.core.ui.navigation.Navigator
import com.example.myapp.core.ui.navigation.rememberAppNavigator
import com.example.myapp.core.ui.theme.MyAppTheme
import com.example.myapp.feature.home.navigation.HomeNavEvent
import com.example.myapp.feature.home.navigation.HomeNavigation
import com.example.myapp.feature.settings.ui.SettingsScreen
import com.example.myapp.feature.weather.ui.WeatherScreen
import com.example.myapp.libs.utils.PlatformColors
import androidx.compose.foundation.isSystemInDarkTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigation() {
    val viewModel: AppViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val darkTheme = when (uiState.theme) {
        ThemeType.LIGHT -> false
        ThemeType.DARK -> true
        ThemeType.SYSTEM -> isSystemInDarkTheme()
    }

    val navigator = rememberAppNavigator(startRoot = HomeDestination) { event ->
        when (event) {
            is GlobalNavEvent.Back -> pop()
            is HomeNavEvent.ToWeather -> switchRoot(WeatherDestination)
            is HomeNavEvent.ToSettings -> switchRoot(SettingsDestination)
        }
    }

    AppNavigationContent(
        darkTheme = darkTheme,
        navigator = navigator,
    )
}

@Composable
private fun AppNavigationContent(
    darkTheme: Boolean,
    navigator: Navigator
) {
    MyAppTheme(darkTheme) {
        PlatformColors(darkTheme)
        NavDisplay(
            backStack = navigator.currentBackStack,
            onBack = { navigator.navigateUp() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            popTransitionSpec = {
                slideInPopTransform()
            },
            predictivePopTransitionSpec = { _ ->
                slideInPopTransform()
            },
            entryProvider = entryProvider {
                entry<HomeDestination> {
                    HomeNavigation(navigator)
                }
                entry<WeatherDestination> {
                    WeatherScreen(navigator)
                }
                entry<SettingsDestination>(metadata = popTransition()) {
                    SettingsScreen(navigator)
                }
            }
        )
    }
}
