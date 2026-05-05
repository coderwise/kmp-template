package com.example.myapp.core.ui.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Surface
import com.example.myapp.core.ui.atoms.AppButton
import com.example.myapp.core.ui.theme.MyAppTheme
import com.example.myapp.core.ui.theme.spacing

@Composable
fun EmptyState(
    message: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    action: Pair<String, () -> Unit>? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.gutter),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.base)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        if (action != null) {
            AppButton(text = action.first, onClick = action.second)
        }
    }
}

@Preview
@Composable
private fun EmptyStatePreview() {
    MyAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            EmptyState(
                message = "No items found. Try searching for something else."
            )
        }
    }
}

@Preview
@Composable
private fun EmptyStateWithIconPreview() {
    MyAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            EmptyState(
                message = "No items found. Try searching for something else.",
                icon = Icons.Default.Info
            )
        }
    }
}

@Preview
@Composable
private fun EmptyStateWithActionPreview() {
    MyAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            EmptyState(
                message = "No items found. Try searching for something else.",
                icon = Icons.Default.Info,
                action = "Retry" to {}
            )
        }
    }
}

@Preview
@Composable
private fun EmptyStateDarkModePreview() {
    MyAppTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            EmptyState(
                message = "No items found. Try searching for something else.",
                icon = Icons.Default.Info,
                action = "Retry" to {}
            )
        }
    }
}
