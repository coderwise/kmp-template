package com.example.myapp.app.common.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
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
import com.example.myapp.feature.home.navigation.HomeNavEvent
import com.example.myapp.feature.home.ui.edit.HomeItemEditScreen
import com.example.myapp.feature.home.ui.edit.HomeItemEditViewModel
import com.example.myapp.libs.utils.PlatformColors
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AppNavigation() {
    val viewModel: AppViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val darkTheme = when (uiState.theme) {
        ThemeType.LIGHT -> false
        ThemeType.DARK -> true
        ThemeType.SYSTEM -> isSystemInDarkTheme()
    }

    val navigator = rememberAppNavigator(startRoot = HomeGroupDestination) { event ->
        when (event) {
            is HomeNavEvent.ToEditItem -> {
                val item = event.item
                navigateRoot(EditItemDestination(item.id, item.title, item.description))
            }
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
