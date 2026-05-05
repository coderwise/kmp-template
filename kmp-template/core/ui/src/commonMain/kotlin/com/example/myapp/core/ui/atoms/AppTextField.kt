package com.example.myapp.core.ui.atoms

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapp.core.ui.theme.MyAppTheme

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = false,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        singleLine = singleLine,
        enabled = enabled
    )
}

@Preview
@Composable
private fun AppTextFieldPreview() {
    MyAppTheme {
        AppTextField(
            value = "Sample text",
            onValueChange = {},
            label = "Label"
        )
    }
}

@Preview
@Composable
private fun AppTextFieldEmptyPreview() {
    MyAppTheme {
        AppTextField(
            value = "",
            onValueChange = {},
            label = "Label"
        )
    }
}

@Preview
@Composable
private fun AppTextFieldDarkPreview() {
    MyAppTheme(darkTheme = true) {
        AppTextField(
            value = "Dark theme text",
            onValueChange = {},
            label = "Label"
        )
    }
}
