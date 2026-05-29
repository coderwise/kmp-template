package com.example.myapp.feature.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapp.core.domain.model.ThemeType
import com.example.myapp.core.ui.layouts.ActionRow
import com.example.myapp.core.ui.layouts.AppTopBar
import com.example.myapp.core.ui.theme.MyAppTheme
import com.example.myapp.core.ui.theme.spacing
import myapp.feature.settings.generated.resources.Res
import myapp.feature.settings.generated.resources.settings_theme_dark
import myapp.feature.settings.generated.resources.settings_theme_light
import myapp.feature.settings.generated.resources.settings_theme_selection
import myapp.feature.settings.generated.resources.settings_theme_system
import myapp.feature.settings.generated.resources.settings_title
import myapp.feature.settings.generated.resources.settings_version
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    SettingsScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun SettingsScreenContent(
    uiState: SettingsUiState,
    onEvent: (SettingsUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(title = stringResource(Res.string.settings_title))
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(MaterialTheme.spacing.gutter)
        ) {
            Text(
                text = stringResource(Res.string.settings_theme_selection),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = MaterialTheme.spacing.base)
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

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(Res.string.settings_version, uiState.appVersion),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun ThemeOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    ActionRow(
        label = text,
        modifier = Modifier
            .selectable(selected = selected, onClick = onClick, role = Role.RadioButton)
            .padding(vertical = MaterialTheme.spacing.sm),
        leadingContent = {
            RadioButton(selected = selected, onClick = null)
        }
    )
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    MyAppTheme {
        SettingsScreenContent(
            uiState = SettingsUiState(
                theme = ThemeType.SYSTEM,
                appVersion = "1.0.0 (1)"
            ),
            onEvent = {}
        )
    }
}

@Preview
@Composable
private fun SettingsScreenDarkPreview() {
    MyAppTheme(darkTheme = true) {
        SettingsScreenContent(
            uiState = SettingsUiState(
                theme = ThemeType.DARK,
                appVersion = "1.0.0 (1)"
            ),
            onEvent = {}
        )
    }
}
