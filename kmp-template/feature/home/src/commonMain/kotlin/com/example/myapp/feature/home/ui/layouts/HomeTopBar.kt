package com.example.myapp.feature.home.ui.layouts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.myapp.core.ui.layouts.AppTopBar
import com.example.myapp.core.ui.theme.spacing
import myapp.feature.home.generated.resources.Res
import myapp.feature.home.generated.resources.home_refresh_content_description
import myapp.feature.home.generated.resources.home_settings_content_description
import myapp.feature.home.generated.resources.home_title
import myapp.feature.home.generated.resources.home_weather_temp
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    isRefreshing: Boolean,
    onRefreshClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onWeatherClick: () -> Unit
) {
    AppTopBar(
        title = stringResource(Res.string.home_title),
        actions = {
            IconButton(
                onClick = onRefreshClick,
                enabled = !isRefreshing
            ) {
                if (isRefreshing) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(MaterialTheme.spacing.base),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = stringResource(Res.string.home_refresh_content_description)
                    )
                }
            }
            IconButton(
                onClick = onSettingsClick
            ) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = stringResource(Res.string.home_settings_content_description)
                )
            }
            WeatherPill(onClick = onWeatherClick)
        }
    )
}

@Composable
fun WeatherPill(onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .padding(end = MaterialTheme.spacing.gutter)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.sm, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.base)
        ) {
            Icon(
                imageVector = Icons.Default.Cloud,
                contentDescription = null,
                modifier = Modifier.height(18.dp)
            )
            Text(
                text = stringResource(Res.string.home_weather_temp),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
