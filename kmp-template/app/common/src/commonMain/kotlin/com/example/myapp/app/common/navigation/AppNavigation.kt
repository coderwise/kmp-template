package com.example.myapp.app.common.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.myapp.core.domain.model.ThemeType
import com.example.myapp.core.ui.layouts.AppBottomBar
import com.example.myapp.core.ui.layouts.BottomBarItem
import com.example.myapp.core.ui.navigation.NavigationState
import com.example.myapp.core.ui.navigation.Navigator
import com.example.myapp.core.ui.navigation.rememberAppNavigator
import com.example.myapp.core.ui.theme.MyAppTheme
import com.example.myapp.feature.home.navigation.HomeNavigation
import com.example.myapp.feature.settings.ui.SettingsScreen
import com.example.myapp.feature.weather.ui.WeatherScreen
import com.example.myapp.libs.utils.PlatformColors
import org.koin.compose.viewmodel.koinViewModel

private val bottomBarDestinations = listOf(HomeDestination, WeatherDestination, SettingsDestination)

@Composable
fun AppNavigation() {
    val viewModel: AppViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val darkTheme = when (uiState.theme) {
        ThemeType.LIGHT -> false
        ThemeType.DARK -> true
        ThemeType.SYSTEM -> isSystemInDarkTheme()
    }

    val navigator = rememberAppNavigator(startRoot = HomeDestination)

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
    val navigationState = navigator as NavigationState
    val bottomBarItems = listOf(
        BottomBarItem(label = "Home", icon = Icons.Default.Home),
        BottomBarItem(label = "Weather", icon = Icons.Default.Cloud),
        BottomBarItem(label = "Settings", icon = Icons.Default.Settings),
    )
    MyAppTheme(darkTheme) {
        PlatformColors(darkTheme)
        Scaffold(
            bottomBar = {
                val selectedIndex = bottomBarDestinations.indexOf(navigationState.currentRootKey)
                    .coerceAtLeast(0)
                AppBottomBar(
                    items = bottomBarItems,
                    selectedIndex = selectedIndex,
                    onItemSelected = { index -> navigator.switchRoot(bottomBarDestinations[index]) },
                )
            }
        ) { paddingValues ->
            NavDisplay(
                modifier = Modifier.padding(paddingValues),
                backStack = navigationState.rootBackStack,
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
                        HomeNavigation()
                    }
                    entry<WeatherDestination> {
                        WeatherScreen()
                    }
                    entry<SettingsDestination> {
                        SettingsScreen()
                    }
                }
            )
        }
    }
}
