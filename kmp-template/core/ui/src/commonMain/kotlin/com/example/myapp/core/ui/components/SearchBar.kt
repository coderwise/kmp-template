package com.example.myapp.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapp.core.ui.theme.MyAppTheme

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text(placeholder) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch()
            }
        )
    )
}

@Preview
@Composable
private fun SearchBarPreview() {
    MyAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            SearchBar(
                query = "",
                onQueryChange = {},
                onSearch = {},
                placeholder = "Search...",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun SearchBarDarkModePreview() {
    MyAppTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            SearchBar(
                query = "Android",
                onQueryChange = {},
                onSearch = {},
                placeholder = "Search...",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
