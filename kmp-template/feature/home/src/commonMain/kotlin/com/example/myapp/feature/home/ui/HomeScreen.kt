package com.example.myapp.feature.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapp.core.domain.model.HomeItem
import com.example.myapp.core.ui.screens.EmptyState
import com.example.myapp.core.ui.theme.spacing
import com.example.myapp.feature.home.ui.layouts.HomeItemCard
import com.example.myapp.feature.home.ui.layouts.HomeTopBar
import myapp.feature.home.generated.resources.Res
import myapp.feature.home.generated.resources.home_add_item_content_description
import myapp.feature.home.generated.resources.home_app_version
import myapp.feature.home.generated.resources.home_error
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            HomeTopBar(
                isRefreshing = uiState.isRefreshing,
                onRefreshClick = { onEvent(HomeUiEvent.Refresh) },
                onSettingsClick = { onEvent(HomeUiEvent.NavigateToSettings) },
                onWeatherClick = { onEvent(HomeUiEvent.NavigateToWeather) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(HomeUiEvent.NavigateToAddItem) }) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(Res.string.home_add_item_content_description)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (uiState.isLoading && uiState.items.isEmpty()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (uiState.isError && uiState.items.isEmpty()) {
                    EmptyState(
                        message = stringResource(Res.string.home_error, uiState.errorMessage ?: ""),
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn {
                        items(uiState.items) { item ->
                            HomeItemCard(
                                title = item.title,
                                description = item.description,
                                onDeleteClick = { onEvent(HomeUiEvent.DeleteItem(item.id)) },
                                onEditClick = { onEvent(HomeUiEvent.NavigateToEditItem(item)) }
                            )
                        }
                    }
                }
            }

            if (uiState.appVersion.isNotBlank()) {
                Text(
                    text = stringResource(Res.string.home_app_version, uiState.appVersion),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = MaterialTheme.spacing.base)
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreenContent(
        uiState = HomeUiState(
            items = listOf(
                HomeItem("1", "Preview Item", "This is a preview description"),
                HomeItem("2", "Another Item", "Another preview description")
            )
        ),
        onEvent = {}
    )
}
