package com.example.myapp.feature.weather.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import com.example.myapp.core.ui.components.AppDivider
import com.example.myapp.core.ui.screens.EmptyState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapp.core.ui.layouts.AppTopBar
import com.example.myapp.core.ui.layouts.SearchBar
import com.example.myapp.core.ui.navigation.Navigator
import com.example.myapp.core.ui.theme.MyAppTheme
import com.example.myapp.core.ui.theme.spacing
import com.example.myapp.feature.weather.domain.model.WeatherInfo
import com.example.myapp.libs.permissions.isGranted
import com.example.myapp.libs.permissions.rememberLocationPermissionState
import myapp.feature.weather.generated.resources.Res
import myapp.feature.weather.generated.resources.weather_back_content_description
import myapp.feature.weather.generated.resources.weather_coordinates
import myapp.feature.weather.generated.resources.weather_error
import myapp.feature.weather.generated.resources.weather_search_placeholder
import myapp.feature.weather.generated.resources.weather_temperature
import myapp.feature.weather.generated.resources.weather_title
import myapp.feature.weather.generated.resources.weather_unknown_error
import myapp.feature.weather.generated.resources.weather_use_current_location
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    navigator: Navigator,
    viewModel: WeatherViewModel = koinViewModel { parametersOf(navigator) }
) {
    val uiState by viewModel.uiState.collectAsState()

    WeatherScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreenContent(
    uiState: WeatherUiState,
    onEvent: (WeatherUiEvent) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val permissionState = rememberLocationPermissionState()
    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(Res.string.weather_title),
                onBackClick = { onEvent(WeatherUiEvent.OnBackClick) },
                backContentDescription = stringResource(Res.string.weather_back_content_description)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(MaterialTheme.spacing.gutter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = { onEvent(WeatherUiEvent.OnSearchQueryChange(it)) },
                onSearch = {
                    onEvent(WeatherUiEvent.OnSearchClick)
                    keyboardController?.hide()
                },
                placeholder = stringResource(Res.string.weather_search_placeholder),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (permissionState.status.isGranted) {
                                onEvent(WeatherUiEvent.OnCurrentLocationClick)
                            } else {
                                permissionState.launchPermissionRequest { isGranted ->
                                    if (isGranted) {
                                        onEvent(WeatherUiEvent.OnCurrentLocationClick)
                                    }
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = stringResource(Res.string.weather_use_current_location)
                        )
                    }
                }
            )

            if (uiState.searchResults.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(uiState.searchResults) { location ->
                        ListItem(
                            headlineContent = { Text(location.name) },
                            supportingContent = {
                                Text("${location.admin1 ?: ""}, ${location.country ?: ""}")
                            },
                            leadingContent = {
                                Icon(Icons.Default.LocationOn, contentDescription = null)
                            },
                            modifier = Modifier.clickable {
                                onEvent(WeatherUiEvent.OnLocationSelected(location))
                                keyboardController?.hide()
                            }
                        )
                        AppDivider()
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.gutter))
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.isLoading && uiState.weatherInfo == null) {
                        CircularProgressIndicator()
                    } else if (uiState.isError && uiState.weatherInfo == null) {
                        EmptyState(
                            message = stringResource(Res.string.weather_error, uiState.errorMessage ?: stringResource(Res.string.weather_unknown_error))
                        )
                    } else if (uiState.weatherInfo != null) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = uiState.weatherInfo.city,
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.gutter))
                            Text(
                                text = stringResource(Res.string.weather_temperature, uiState.weatherInfo.temperature),
                                style = MaterialTheme.typography.displayLarge
                            )
                            Text(
                                text = uiState.weatherInfo.condition,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.base))
                            Text(
                                text = stringResource(Res.string.weather_coordinates, uiState.weatherInfo.latitude, uiState.weatherInfo.longitude),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun WeatherScreenPreview() {
    MyAppTheme {
        WeatherScreenContent(
            uiState = WeatherUiState(
                searchQuery = "",
                weatherInfo = WeatherInfo(
                    temperature = 22.0,
                    condition = "Sunny",
                    city = "London",
                    latitude = 51.5074,
                    longitude = -0.1278
                )
            ),
            onEvent = {}
        )
    }
}
