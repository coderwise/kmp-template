package com.example.myapp.app.common.navigation

import androidx.compose.foundation.isSystemInDarkTheme
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
    )
}

@Composable
private fun AppNavigationContent(
    darkTheme: Boolean,
    navigator: Navigator,
) {
    val navigationState = navigator as NavigationState
    MyAppTheme(darkTheme) {
        PlatformColors(darkTheme)
        NavDisplay(
            backStack = navigationState.rootBackStack,
            onBack = { navigator.navigateUp() },
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
                    HomeGroup()
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
}
