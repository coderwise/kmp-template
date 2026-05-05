package com.example.myapp.core.ui.atoms

import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapp.core.ui.theme.MyAppTheme

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(onClick = onClick, modifier = modifier, enabled = enabled) {
        Text(text)
    }
}

@Composable
fun AppOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(onClick = onClick, modifier = modifier, enabled = enabled) {
        Text(text)
    }
}

@Composable
fun AppTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    TextButton(onClick = onClick, modifier = modifier, enabled = enabled) {
        Text(text)
    }
}

@Preview
@Composable
private fun AppButtonPreview() {
    MyAppTheme {
        AppButton(
            text = "Button",
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun AppButtonDisabledPreview() {
    MyAppTheme {
        AppButton(
            text = "Disabled",
            onClick = {},
            enabled = false
        )
    }
}
