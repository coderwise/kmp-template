package com.example.myapp.feature.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.myapp.core.domain.model.ThemeType
import com.example.myapp.core.ui.theme.MyAppTheme
import myapp.feature.settings.generated.resources.Res
import myapp.feature.settings.generated.resources.settings_theme_dark
import myapp.feature.settings.generated.resources.settings_theme_light
import myapp.feature.settings.generated.resources.settings_theme_selection
import myapp.feature.settings.generated.resources.settings_theme_system
import myapp.feature.settings.generated.resources.settings_title
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    SettingsScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    uiState: SettingsUiState,
    onBackClick: () -> Unit,
    onEvent: (SettingsUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.settings_theme_selection),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Column(Modifier.selectableGroup()) {
                ThemeOption(
                    text = stringResource(Res.string.settings_theme_system),
                    selected = uiState.theme == ThemeType.SYSTEM,
                    onClick = { onEvent(SettingsUiEvent.ThemeChanged(ThemeType.SYSTEM)) }
                )
                ThemeOption(
                    text = stringResource(Res.string.settings_theme_light),
                    selected = uiState.theme == ThemeType.LIGHT,
                    onClick = { onEvent(SettingsUiEvent.ThemeChanged(ThemeType.LIGHT)) }
                )
                ThemeOption(
                    text = stringResource(Res.string.settings_theme_dark),
                    selected = uiState.theme == ThemeType.DARK,
                    onClick = { onEvent(SettingsUiEvent.ThemeChanged(ThemeType.DARK)) }
                )
            }
        }
    }
}

@Composable
private fun ThemeOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null // null recommended for accessibility with screen readers
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    MyAppTheme {
        SettingsScreenContent(
            uiState = SettingsUiState(theme = ThemeType.SYSTEM),
            onBackClick = {},
            onEvent = {}
        )
    }
}

@Preview
@Composable
private fun SettingsScreenDarkPreview() {
    MyAppTheme(darkTheme = true) {
        SettingsScreenContent(
            uiState = SettingsUiState(theme = ThemeType.DARK),
            onBackClick = {},
            onEvent = {}
        )
    }
}
