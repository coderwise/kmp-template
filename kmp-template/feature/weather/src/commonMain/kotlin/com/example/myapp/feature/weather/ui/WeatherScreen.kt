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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapp.core.ui.MyAppTheme
import com.example.myapp.feature.weather.domain.model.WeatherInfo
import myapp.feature.weather.generated.resources.Res
import myapp.feature.weather.generated.resources.weather_back_content_description
import myapp.feature.weather.generated.resources.weather_error
import myapp.feature.weather.generated.resources.weather_search_placeholder
import myapp.feature.weather.generated.resources.weather_temperature
import myapp.feature.weather.generated.resources.weather_title
import myapp.feature.weather.generated.resources.weather_unknown_error
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    onBackClick: () -> Unit,
    viewModel: WeatherViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    WeatherScreenContent(
        uiState = uiState,
        onEvent = { event ->
            if (event is WeatherUiEvent.OnBackClick) {
                onBackClick()
            } else {
                viewModel.onEvent(event)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreenContent(
    uiState: WeatherUiState,
    onEvent: (WeatherUiEvent) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.weather_title)) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(WeatherUiEvent.OnBackClick) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(Res.string.weather_back_content_description))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { onEvent(WeatherUiEvent.OnSearchQueryChange(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(stringResource(Res.string.weather_search_placeholder)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onEvent(WeatherUiEvent.OnSearchClick)
                            keyboardController?.hide()
                        }
                    )
                )
            }

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
                        HorizontalDivider()
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.isLoading && uiState.weatherInfo == null) {
                        CircularProgressIndicator()
                    } else if (uiState.isError && uiState.weatherInfo == null) {
                        Text(
                            text = stringResource(Res.string.weather_error, uiState.errorMessage ?: stringResource(Res.string.weather_unknown_error)),
                            color = MaterialTheme.colorScheme.error
                        )
                    } else if (uiState.weatherInfo != null) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = uiState.weatherInfo.city,
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(Res.string.weather_temperature, uiState.weatherInfo.temperature),
                                style = MaterialTheme.typography.displayLarge
                            )
                            Text(
                                text = uiState.weatherInfo.condition,
                                style = MaterialTheme.typography.titleMedium
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
                    city = "London"
                )
            ),
            onEvent = {}
        )
    }
}
