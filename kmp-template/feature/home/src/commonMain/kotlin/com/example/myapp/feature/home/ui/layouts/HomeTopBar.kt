package com.example.myapp.feature.home.ui.layouts

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapp.core.ui.components.AppIconButton
import com.example.myapp.core.ui.layouts.AppTopBar
import com.example.myapp.core.ui.theme.MyAppTheme
import myapp.feature.home.generated.resources.Res
import myapp.feature.home.generated.resources.home_refresh_content_description
import myapp.feature.home.generated.resources.home_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeTopBar(
    isRefreshing: Boolean,
    onRefreshClick: () -> Unit,
) {
    AppTopBar(
        title = stringResource(Res.string.home_title),
        actions = {
            AppIconButton(
                icon = Icons.Default.Refresh,
                contentDescription = stringResource(Res.string.home_refresh_content_description),
                onClick = onRefreshClick,
                isLoading = isRefreshing,
            )
        },
    )
}

@Preview
@Composable
private fun HomeTopBarPreview() {
    MyAppTheme {
        HomeTopBar(isRefreshing = false, onRefreshClick = {})
    }
}

@Preview
@Composable
private fun HomeTopBarRefreshingPreview() {
    MyAppTheme {
        HomeTopBar(isRefreshing = true, onRefreshClick = {})
    }
}

@Preview
@Composable
private fun HomeTopBarDarkModePreview() {
    MyAppTheme(darkTheme = true) {
        HomeTopBar(isRefreshing = false, onRefreshClick = {})
    }
}
