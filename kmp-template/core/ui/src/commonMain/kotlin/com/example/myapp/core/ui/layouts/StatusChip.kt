package com.example.myapp.core.ui.layouts

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun StatusChip(
    label: String,
    status: ChipStatus = ChipStatus.Default,
    modifier: Modifier = Modifier
) {
    val containerColor = when (status) {
        ChipStatus.Default -> MaterialTheme.colorScheme.surfaceVariant
        ChipStatus.Success -> MaterialTheme.colorScheme.primaryContainer
        ChipStatus.Warning -> MaterialTheme.colorScheme.tertiaryContainer
        ChipStatus.Error -> MaterialTheme.colorScheme.errorContainer
    }
    SuggestionChip(
        onClick = {},
        label = { Text(label) },
        modifier = modifier,
        colors = SuggestionChipDefaults.suggestionChipColors(containerColor = containerColor)
    )
}

enum class ChipStatus { Default, Success, Warning, Error }
