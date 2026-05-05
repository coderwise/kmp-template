package com.example.myapp.core.ui.molecules

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myapp.core.ui.atoms.AppTextField
import com.example.myapp.core.ui.theme.spacing

@Composable
fun LabeledField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    singleLine: Boolean = false
) {
    Column(modifier = modifier) {
        AppTextField(
            value = value,
            onValueChange = onValueChange,
            label = label,
            singleLine = singleLine
        )
        if (hint.isNotBlank()) {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.xs))
            Text(
                text = hint,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
