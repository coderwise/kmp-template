package com.example.myapp.feature.home.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import com.example.myapp.core.ui.organisms.EmptyState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapp.core.domain.model.HomeItem
import com.example.myapp.core.ui.organisms.HomeItemCard
import com.example.myapp.core.ui.theme.spacing
import myapp.feature.home.generated.resources.Res
import myapp.feature.home.generated.resources.home_add_item_content_description
import myapp.feature.home.generated.resources.home_app_version
import myapp.feature.home.generated.resources.home_error
import myapp.feature.home.generated.resources.home_refresh_content_description
import myapp.feature.home.generated.resources.home_settings_content_description
import myapp.feature.home.generated.resources.home_title
import myapp.feature.home.generated.resources.home_weather_temp
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onWeatherClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onAddItemClick: () -> Unit,
    onEditItemClick: (HomeItem) -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onWeatherClick = onWeatherClick,
        onSettingsClick = onSettingsClick,
        onAddItemClick = onAddItemClick,
        onEditItemClick = onEditItemClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    onWeatherClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onAddItemClick: () -> Unit = {},
    onEditItemClick: (HomeItem) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.home_title)) },
                actions = {
                    IconButton(
                        onClick = { onEvent(HomeUiEvent.Refresh) },
                        enabled = !uiState.isRefreshing
                    ) {
                        if (uiState.isRefreshing) {
                            CircularProgressIndicator(
                                modifier = Modifier.padding(MaterialTheme.spacing.base),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = stringResource(Res.string.home_refresh_content_description)
                            )
                        }
                    }
                    IconButton(
                        onClick = onSettingsClick
                    ) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = stringResource(Res.string.home_settings_content_description)
                        )
                    }
                    WeatherPill(onClick = onWeatherClick)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddItemClick) {
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
                                onEditClick = { onEditItemClick(item) }
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

@Composable
fun WeatherPill(onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .padding(end = MaterialTheme.spacing.gutter)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.sm, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.base)
        ) {
            Icon(
                imageVector = Icons.Default.Cloud,
                contentDescription = null,
                modifier = Modifier.height(18.dp)
            )
            Text(
                text = stringResource(Res.string.home_weather_temp),
                style = MaterialTheme.typography.labelLarge
            )
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
