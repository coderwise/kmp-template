package com.example.myapp.feature.home.ui.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapp.core.ui.components.AppButton
import com.example.myapp.core.ui.components.AppIconButton
import com.example.myapp.core.ui.components.AppTextButton
import com.example.myapp.core.ui.layouts.AppTopBar
import com.example.myapp.core.ui.layouts.LabeledField
import com.example.myapp.core.ui.theme.MyAppTheme
import com.example.myapp.core.ui.theme.spacing
import com.example.myapp.core.ui.util.windowInfo
import myapp.feature.home.generated.resources.Res
import myapp.feature.home.generated.resources.home_dialog_description_label
import myapp.feature.home.generated.resources.home_dialog_edit_title
import myapp.feature.home.generated.resources.home_dialog_title_label
import myapp.feature.home.generated.resources.home_edit_save
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeItemEditScreen(viewModel: HomeItemEditViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeItemEditScreenContent(uiState = uiState, onEvent = viewModel::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeItemEditScreenContent(
    uiState: HomeItemEditUiState,
    onEvent: (HomeItemEditUiEvent) -> Unit,
) {
    val scrollState = rememberScrollState()
    val isLandscape = windowInfo.isLandscape

    Scaffold(
        topBar = {
            if (!isLandscape) {
                AppTopBar(
                    title = stringResource(Res.string.home_dialog_edit_title),
                    onBackClick = { onEvent(HomeItemEditUiEvent.NavigateBack) },
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .padding(horizontal = MaterialTheme.spacing.gutter)
                .padding(vertical = if (isLandscape) MaterialTheme.spacing.sm else MaterialTheme.spacing.gutter)
                .imePadding()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.base),
        ) {
            if (isLandscape) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AppIconButton(
                        onClick = { onEvent(HomeItemEditUiEvent.NavigateBack) },
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                    )
                    Text(
                        text = stringResource(Res.string.home_dialog_edit_title),
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    AppTextButton(
                        text = stringResource(Res.string.home_edit_save),
                        onClick = { onEvent(HomeItemEditUiEvent.Save) },
                        enabled = uiState.title.isNotBlank(),
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.base),
                ) {
                    LabeledField(
                        label = stringResource(Res.string.home_dialog_title_label),
                        value = uiState.title,
                        onValueChange = { onEvent(HomeItemEditUiEvent.TitleChanged(it)) },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                    )
                    LabeledField(
                        label = stringResource(Res.string.home_dialog_description_label),
                        value = uiState.description,
                        onValueChange = { onEvent(HomeItemEditUiEvent.DescriptionChanged(it)) },
                        modifier = Modifier.weight(1f),
                    )
                }
            } else {
                LabeledField(
                    label = stringResource(Res.string.home_dialog_title_label),
                    value = uiState.title,
                    onValueChange = { onEvent(HomeItemEditUiEvent.TitleChanged(it)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                LabeledField(
                    label = stringResource(Res.string.home_dialog_description_label),
                    value = uiState.description,
                    onValueChange = { onEvent(HomeItemEditUiEvent.DescriptionChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                )
                AppButton(
                    text = stringResource(Res.string.home_edit_save),
                    onClick = { onEvent(HomeItemEditUiEvent.Save) },
                    enabled = uiState.title.isNotBlank(),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeItemEditScreenPreview() {
    MyAppTheme {
        HomeItemEditScreenContent(
            uiState = HomeItemEditUiState(
                title = "Sample Item",
                description = "This is a sample description.",
            ),
            onEvent = {},
        )
    }
}

@Preview
@Composable
private fun HomeItemEditScreenDarkPreview() {
    MyAppTheme(darkTheme = true) {
        HomeItemEditScreenContent(
            uiState = HomeItemEditUiState(
                title = "Sample Item",
                description = "This is a sample description.",
            ),
            onEvent = {},
        )
    }
}

@Preview
@Composable
private fun HomeItemEditScreenLandscapePreview() {
    Box(modifier = Modifier.size(width = 800.dp, height = 400.dp)) {
        MyAppTheme {
            HomeItemEditScreenContent(
                uiState = HomeItemEditUiState(
                    title = "Sample Item",
                    description = "This is a sample description.",
                ),
                onEvent = {},
            )
        }
    }
}
