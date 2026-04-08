package com.example.myapp.feature.home.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapp.core.version.appVersion
import com.example.myapp.feature.home.domain.model.HomeItem
import myapp.feature.home.generated.resources.Res
import myapp.feature.home.generated.resources.home_add_item_content_description
import myapp.feature.home.generated.resources.home_delete_content_description
import myapp.feature.home.generated.resources.home_dialog_add_title
import myapp.feature.home.generated.resources.home_dialog_cancel
import myapp.feature.home.generated.resources.home_dialog_confirm
import myapp.feature.home.generated.resources.home_dialog_description_label
import myapp.feature.home.generated.resources.home_dialog_edit_title
import myapp.feature.home.generated.resources.home_dialog_title_label
import myapp.feature.home.generated.resources.home_edit_content_description
import myapp.feature.home.generated.resources.home_error
import myapp.feature.home.generated.resources.home_refresh_content_description
import myapp.feature.home.generated.resources.home_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onWeatherClick: () -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var itemToEdit by remember { mutableStateOf<HomeItem?>(null) }

    HomeScreenContent(
        uiState = uiState,
        appVersion = appVersion,
        onRefresh = { viewModel.refresh() },
        onAddClick = { showAddDialog = true },
        onDeleteClick = { viewModel.removeItem(it.id) },
        onEditClick = { itemToEdit = it },
        onWeatherClick = onWeatherClick
    )

    if (showAddDialog) {
        HomeItemDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, description ->
                viewModel.addItem(title, description)
                showAddDialog = false
            }
        )
    }

    itemToEdit?.let { item ->
        HomeItemDialog(
            initialTitle = item.title,
            initialDescription = item.description,
            onDismiss = { itemToEdit = null },
            onConfirm = { title, description ->
                viewModel.updateItem(item.copy(title = title, description = description))
                itemToEdit = null
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    appVersion: String = "",
    onRefresh: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onDeleteClick: (HomeItem) -> Unit = {},
    onEditClick: (HomeItem) -> Unit = {},
    onWeatherClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.home_title)) },
                actions = {
                    IconButton(onClick = onRefresh, enabled = !uiState.isRefreshing) {
                        if (uiState.isRefreshing) {
                            CircularProgressIndicator(modifier = Modifier.padding(8.dp), strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Default.Refresh, contentDescription = stringResource(Res.string.home_refresh_content_description))
                        }
                    }
                    WeatherPill(onClick = onWeatherClick)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = stringResource(Res.string.home_add_item_content_description))
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
                    Text(
                        text = stringResource(Res.string.home_error, uiState.errorMessage ?: ""),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                } else {
                    LazyColumn {
                        items(uiState.items) { item ->
                            HomeItemCard(
                                item = item,
                                onDeleteClick = { onDeleteClick(item) },
                                onEditClick = { onEditClick(item) }
                            )
                        }
                    }
                }
            }

            if (appVersion.isNotBlank()) {
                Text(
                    text = "v$appVersion",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 8.dp)
                )
            }
        }
    }
}

@Composable
fun HomeItemCard(
    item: HomeItem,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Row {
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = stringResource(Res.string.home_edit_content_description))
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(Icons.Default.Delete, contentDescription = stringResource(Res.string.home_delete_content_description))
                    }
                }
            }
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun HomeItemDialog(
    initialTitle: String = "",
    initialDescription: String = "",
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var title by remember { mutableStateOf(initialTitle) }
    var description by remember { mutableStateOf(initialDescription) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialTitle.isEmpty()) stringResource(Res.string.home_dialog_add_title) else stringResource(Res.string.home_dialog_edit_title)) },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(Res.string.home_dialog_title_label)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(Res.string.home_dialog_description_label)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(title, description) },
                enabled = title.isNotBlank()
            ) {
                Text(stringResource(Res.string.home_dialog_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.home_dialog_cancel))
            }
        }
    )
}

@Composable
fun WeatherPill(onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .padding(end = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Cloud,
                contentDescription = null,
                modifier = Modifier.height(18.dp)
            )
            Text(
                text = "72°F",
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
        )
    )
}
