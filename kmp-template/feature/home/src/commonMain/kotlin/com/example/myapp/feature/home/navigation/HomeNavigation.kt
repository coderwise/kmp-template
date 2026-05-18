package com.example.myapp.feature.home.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.example.myapp.core.domain.model.HomeItem
import com.example.myapp.core.ui.navigation.NavigationState
import com.example.myapp.core.ui.util.BottomSheetSceneStrategy
import com.example.myapp.feature.home.ui.HomeScreen
import com.example.myapp.feature.home.ui.HomeUiEvent
import com.example.myapp.feature.home.ui.HomeViewModel
import com.example.myapp.feature.home.ui.edit.HomeItemSheet
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeNavigation() {
    val navigator = koinInject<NavigationState>()
    val viewModel: HomeViewModel = koinViewModel()

    val backStack = navigator.currentBackStack
    if (backStack.isEmpty()) {
        backStack.add(HomeNavDestination.Root)
    }

    NavDisplay(
        backStack = backStack,
        onBack = { navigator.navigateUp() },
        sceneStrategies = listOf(BottomSheetSceneStrategy(), SinglePaneSceneStrategy()),
        entryProvider = entryProvider {
            entry<HomeNavDestination.Root> {
                HomeScreen(viewModel = viewModel)
            }
            entry<HomeNavDestination.AddItem>(
                metadata = BottomSheetSceneStrategy.bottomSheet(
                    onDismiss = { navigator.navigateUp() }
                )
            ) {
                HomeItemSheet(
                    onDismiss = { navigator.navigateUp() },
                    onConfirm = { title, description ->
                        viewModel.onEvent(HomeUiEvent.AddItem(title, description))
                    }
                )
            }
            entry<HomeNavDestination.EditItem>(
                metadata = BottomSheetSceneStrategy.bottomSheet(
                    onDismiss = { navigator.navigateUp() }
                )
            ) { destination ->
                HomeItemSheet(
                    initialTitle = destination.title,
                    initialDescription = destination.description,
                    onDismiss = { navigator.navigateUp() },
                    onConfirm = { title, description ->
                        viewModel.onEvent(
                            HomeUiEvent.UpdateItem(
                                HomeItem(
                                    id = destination.id,
                                    title = title,
                                    description = description
                                )
                            )
                        )
                    }
                )
            }
        }
    )
}
