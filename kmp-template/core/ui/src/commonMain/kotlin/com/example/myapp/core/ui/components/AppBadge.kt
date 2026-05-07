package com.example.myapp.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapp.core.ui.theme.MyAppTheme
import com.example.myapp.core.ui.theme.spacing

@Composable
fun AppBadge(
    count: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    BadgedBox(
        badge = { if (count > 0) Badge { Text(count.toString()) } },
        modifier = modifier
    ) {
        content()
    }
}

@Preview
@Composable
private fun AppBadgePreview() {
    MyAppTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(MaterialTheme.spacing.gutter)
        ) {
            AppBadge(count = 5) {
                Text("Messages")
            }
        }
    }
}

@Preview
@Composable
private fun AppBadgeEmptyPreview() {
    MyAppTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(MaterialTheme.spacing.gutter)
        ) {
            AppBadge(count = 0) {
                Text("Messages")
            }
        }
    }
}

@Preview
@Composable
private fun AppBadgeDarkModePreview() {
    MyAppTheme(darkTheme = true) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(MaterialTheme.spacing.gutter)
        ) {
            AppBadge(count = 99) {
                Text("Notifications")
            }
        }
    }
}
