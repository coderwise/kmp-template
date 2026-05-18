package com.example.myapp.feature.home.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.myapp.core.domain.model.HomeItem
import com.example.myapp.core.ui.navigation.GlobalNavEvent
import com.example.myapp.core.ui.navigation.Navigator
import com.example.myapp.core.ui.navigation.rememberNavigator
import com.example.myapp.core.ui.util.BottomSheetSceneStrategy
import com.example.myapp.feature.home.ui.HomeScreen
import com.example.myapp.feature.home.ui.HomeUiEvent
import com.example.myapp.feature.home.ui.HomeViewModel
import com.example.myapp.feature.home.ui.edit.HomeItemSheet
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

private val homeNavConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(HomeNavDestination.Root::class, HomeNavDestination.Root.serializer())
            subclass(HomeNavDestination.AddItem::class, HomeNavDestination.AddItem.serializer())
            subclass(HomeNavDestination.EditItem::class, HomeNavDestination.EditItem.serializer())
        }
    }
}

@Composable
fun HomeNavigation(
    appNavigator: Navigator
) {
    val navigator = rememberNavigator(HomeNavDestination.Root, homeNavConfig) { event ->
        when (event) {
            GlobalNavEvent.Back, HomeNavEvent.Back -> pop()
            is HomeNavEvent.ToAddItem -> push(HomeNavDestination.AddItem())
            is HomeNavEvent.ToEditItem -> {
                val item = event.item
                push(
                    HomeNavDestination.EditItem(
                        item.id,
                        item.title,
                        item.description
                    )
                )
            }
            else -> appNavigator.onEvent(event)
        }
    }

    val viewModel: HomeViewModel = koinViewModel { parametersOf(navigator) }

    NavDisplay(
        backStack = navigator.backStack,
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
