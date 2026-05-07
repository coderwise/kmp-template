package com.example.myapp.feature.home.ui.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapp.core.ui.components.AppButton
import com.example.myapp.core.ui.components.AppTextButton
import com.example.myapp.core.ui.layouts.LabeledField
import com.example.myapp.core.ui.theme.MyAppTheme
import com.example.myapp.core.ui.theme.spacing
import myapp.feature.home.generated.resources.Res
import myapp.feature.home.generated.resources.home_dialog_add_title
import myapp.feature.home.generated.resources.home_dialog_cancel
import myapp.feature.home.generated.resources.home_dialog_confirm
import myapp.feature.home.generated.resources.home_dialog_description_label
import myapp.feature.home.generated.resources.home_dialog_edit_title
import myapp.feature.home.generated.resources.home_dialog_title_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeItemSheet(
    initialTitle: String = "",
    initialDescription: String = "",
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit,
) {
    var title by remember { mutableStateOf(initialTitle) }
    var description by remember { mutableStateOf(initialDescription) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.gutter)
            .padding(bottom = MaterialTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.base)
    ) {
        Text(
            text = if (initialTitle.isEmpty()) stringResource(Res.string.home_dialog_add_title)
            else stringResource(Res.string.home_dialog_edit_title),
            style = MaterialTheme.typography.titleLarge
        )
        LabeledField(
            label = stringResource(Res.string.home_dialog_title_label),
            value = title,
            onValueChange = { title = it },
            singleLine = true
        )
        LabeledField(
            label = stringResource(Res.string.home_dialog_description_label),
            value = description,
            onValueChange = { description = it }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            AppTextButton(
                text = stringResource(Res.string.home_dialog_cancel),
                onClick = onDismiss
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.base))
            AppButton(
                text = stringResource(Res.string.home_dialog_confirm),
                onClick = { onConfirm(title, description) },
                enabled = title.isNotBlank()
            )
        }
    }
}

@Preview
@Composable
private fun HomeItemSheetAddPreview() {
    MyAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            HomeItemSheet(onDismiss = {}) { _, _ -> }
        }
    }
}

@Preview
@Composable
private fun HomeItemSheetEditPreview() {
    MyAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            HomeItemSheet(
                initialTitle = "Sample Item",
                initialDescription = "This is a sample description for the item.",
                onDismiss = {}
            ) { _, _ -> }
        }
    }
}

@Preview
@Composable
private fun HomeItemSheetDarkModePreview() {
    MyAppTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            HomeItemSheet(
                initialTitle = "Sample Item",
                initialDescription = "This is a sample description for the item.",
                onDismiss = {}
            ) { _, _ -> }
        }
    }
}
