package com.example.myapp.feature.home.ui.layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapp.core.ui.components.AppIconButton
import com.example.myapp.core.ui.theme.MyAppTheme
import com.example.myapp.core.ui.theme.spacing
import myapp.feature.home.generated.resources.Res
import myapp.feature.home.generated.resources.home_delete_content_description
import myapp.feature.home.generated.resources.home_edit_content_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeItemCard(
    title: String,
    description: String,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.gutter, vertical = MaterialTheme.spacing.base)
    ) {
        Column(modifier = Modifier.padding(MaterialTheme.spacing.gutter)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Row {
                    AppIconButton(
                        icon = Icons.Default.Edit,
                        contentDescription = stringResource(Res.string.home_edit_content_description),
                        onClick = onEditClick
                    )
                    AppIconButton(
                        icon = Icons.Default.Delete,
                        contentDescription = stringResource(Res.string.home_delete_content_description),
                        onClick = onDeleteClick
                    )
                }
            }
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview
@Composable
private fun HomeItemCardPreview() {
    MyAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            HomeItemCard(
                title = "Example Item",
                description = "This is a sample description for the HomeItemCard preview.",
                onDeleteClick = {},
                onEditClick = {}
            )
        }
    }
}

@Preview
@Composable
private fun HomeItemCardDarkModePreview() {
    MyAppTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            HomeItemCard(
                title = "Example Item",
                description = "This is a sample description for the HomeItemCard preview.",
                onDeleteClick = {},
                onEditClick = {}
            )
        }
    }
}
