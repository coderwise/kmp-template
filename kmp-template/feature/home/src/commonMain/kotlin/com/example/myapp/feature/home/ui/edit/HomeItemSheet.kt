package com.example.myapp.feature.home.ui.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
    onConfirm: (String, String) -> Unit
) {
    var title by remember { mutableStateOf(initialTitle) }
    var description by remember { mutableStateOf(initialDescription) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.gutter)
            .padding(bottom = MaterialTheme.spacing.md)
    ) {
        Text(
            text = if (initialTitle.isEmpty()) stringResource(Res.string.home_dialog_add_title) else stringResource(
                Res.string.home_dialog_edit_title
            ),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.gutter))
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(stringResource(Res.string.home_dialog_title_label)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.base))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(stringResource(Res.string.home_dialog_description_label)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.md))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.home_dialog_cancel))
            }
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.base))
            Button(
                onClick = { onConfirm(title, description) },
                enabled = title.isNotBlank()
            ) {
                Text(stringResource(Res.string.home_dialog_confirm))
            }
        }
    }
}
