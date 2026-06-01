package com.example.myapp.app.common.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.myapp.core.domain.model.ThemeType
import com.example.myapp.core.ui.navigation.NavigationState
import com.example.myapp.core.ui.navigation.Navigator
import com.example.myapp.core.ui.navigation.rememberAppNavigator
import com.example.myapp.core.ui.theme.MyAppTheme
import com.example.myapp.feature.auth.navigation.AuthNavigation
import com.example.myapp.feature.home.navigation.HomeNavEvent
import com.example.myapp.feature.home.ui.edit.HomeItemEditScreen
import com.example.myapp.feature.home.ui.edit.HomeItemEditViewModel
import com.example.myapp.feature.settings.navigation.SettingsNavEvent
import com.example.myapp.libs.utils.PlatformColors
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AppNavigation() {
    val viewModel: AppViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val isAuthenticated = uiState.isAuthenticated ?: return

    val darkTheme = when (uiState.theme) {
        ThemeType.LIGHT -> false
        ThemeType.DARK -> true
        ThemeType.SYSTEM -> isSystemInDarkTheme()
    }

    val startRoot = if (isAuthenticated) HomeGroupDestination else AuthDestination

    val navigator = rememberAppNavigator(startRoot = startRoot) { event ->
        when (event) {
            is SettingsNavEvent.SignOut -> viewModel.signOut()
            is HomeNavEvent.ToEditItem -> {
                val item = event.item
                navigateRoot(EditItemDestination(item.id, item.title, item.description))
            }
        }
    }

    // Auth state is the single source of truth for the root destination:
    // signing in/out flips isAuthenticated, which switches the root here.
    LaunchedEffect(isAuthenticated) {
        val target = if (isAuthenticated) HomeGroupDestination else AuthDestination
        if ((navigator as NavigationState).currentRootKey != target) {
            navigator.replaceRoot(target)
        }
    }

    AppNavigationContent(
        darkTheme = darkTheme,
        navigator = navigator,
        uiState = uiState,
        viewModel = viewModel,
        onTabSelected = viewModel::setTab,
    )
}

@Composable
private fun AppNavigationContent(
    darkTheme: Boolean,
    navigator: Navigator,
    uiState: AppUiState,
    viewModel: AppViewModel,
    onTabSelected: (Int) -> Unit,
) {
    val navigationState = navigator as NavigationState

    MyAppTheme(darkTheme) {
        PlatformColors(darkTheme)

        val showNavigationSuite = uiState.isAuthenticated == true

        if (showNavigationSuite) {
            BoxWithConstraints {
                val layoutType = if (maxWidth > maxHeight) {
                    NavigationSuiteType.NavigationRail
                } else {
                    NavigationSuiteType.NavigationBar
                }

                NavigationSuiteScaffold(
                    layoutType = layoutType,
                    navigationSuiteItems = {
                        item(
                            label = { Text("Home") },
                            icon = { Icon(Icons.Default.Home, contentDescription = null) },
                            selected = uiState.selectedTab == 0,
                            onClick = {
                                onTabSelected(0)
                                navigationState.switchRoot(HomeGroupDestination)
                            },
                        )
                        item(
                            label = { Text("Weather") },
                            icon = { Icon(Icons.Default.Cloud, contentDescription = null) },
                            selected = uiState.selectedTab == 1,
                            onClick = {
                                onTabSelected(1)
                                navigationState.switchRoot(HomeGroupDestination)
                            },
                        )
                        item(
                            label = { Text("Settings") },
                            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                            selected = uiState.selectedTab == 2,
                            onClick = {
                                onTabSelected(2)
                                navigationState.switchRoot(HomeGroupDestination)
                            },
                        )
                    }
                ) {
                    AppNavDisplay(navigationState, viewModel)
                }
            }
        } else {
            AppNavDisplay(navigationState, viewModel)
        }
    }
}

@Composable
private fun AppNavDisplay(
    navigationState: NavigationState,
    viewModel: AppViewModel,
) {
    NavDisplay(
        backStack = navigationState.rootBackStack,
        onBack = { navigationState.navigateUp() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        popTransitionSpec = {
            slideInPopTransform()
        },
        predictivePopTransitionSpec = { _ ->
            slideInPopTransform()
        },
        entryProvider = entryProvider {
            entry<AuthDestination> {
                AuthNavigation()
            }
            entry<HomeGroupDestination> {
                HomeGroup(viewModel = viewModel)
            }
            entry<EditItemDestination> { destination ->
                val editViewModel: HomeItemEditViewModel = koinViewModel {
                    parametersOf(destination.id, destination.title, destination.description)
                }
                HomeItemEditScreen(viewModel = editViewModel)
            }
        }
    )
}
