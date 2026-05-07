package com.example.myapp.feature.home.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.myapp.core.domain.model.HomeItem
import com.example.myapp.core.ui.util.BottomSheetSceneStrategy
import com.example.myapp.core.ui.util.pop
import com.example.myapp.core.ui.util.push
import com.example.myapp.feature.home.ui.HomeScreen
import com.example.myapp.feature.home.ui.HomeUiEvent
import com.example.myapp.feature.home.ui.HomeViewModel
import com.example.myapp.feature.home.ui.edit.HomeItemSheet
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.viewmodel.koinViewModel

private val homeNavSerializersModule = SerializersModule {
    polymorphic(NavKey::class) {
        subclass(HomeNavDestination.Root::class, HomeNavDestination.Root.serializer())
        subclass(HomeNavDestination.AddItem::class, HomeNavDestination.AddItem.serializer())
        subclass(HomeNavDestination.EditItem::class, HomeNavDestination.EditItem.serializer())
    }
}

@Composable
fun HomeNavigation(
    onWeatherClick: () -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val config = remember {
        SavedStateConfiguration {
            serializersModule = homeNavSerializersModule
        }
    }
    val backStack = rememberNavBackStack(config, HomeNavDestination.Root)

    NavDisplay(
        backStack = backStack,
        sceneStrategies = listOf(BottomSheetSceneStrategy(), SinglePaneSceneStrategy()),
        entryProvider = entryProvider {
            entry<HomeNavDestination.Root> {
                HomeScreen(
                    onWeatherClick = onWeatherClick,
                    onSettingsClick = onSettingsClick,
                    onAddItemClick = {
                        backStack.push(HomeNavDestination.AddItem())
                    },
                    onEditItemClick = { item ->
                        backStack.push(HomeNavDestination.EditItem(item.id, item.title, item.description))
                    },
                    viewModel = viewModel
                )
            }
            entry<HomeNavDestination.AddItem>(
                metadata = BottomSheetSceneStrategy.bottomSheet()
            ) {
                HomeItemSheet(
                    onDismiss = { backStack.pop() },
                    onConfirm = { title, description ->
                        viewModel.onEvent(HomeUiEvent.AddItem(title, description))
                        backStack.pop()
                    }
                )
            }
            entry<HomeNavDestination.EditItem>(
                metadata = BottomSheetSceneStrategy.bottomSheet()
            ) { destination ->
                HomeItemSheet(
                    initialTitle = destination.title,
                    initialDescription = destination.description,
                    onDismiss = { backStack.pop() },
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
                        backStack.pop()
                    }
                )
            }
        }
    )
}
