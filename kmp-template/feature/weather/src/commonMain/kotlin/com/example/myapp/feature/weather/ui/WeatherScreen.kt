package com.example.myapp.feature.weather.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapp.core.domain.Result
import com.example.myapp.core.ui.MyAppTheme
import com.example.myapp.feature.weather.domain.model.WeatherInfo
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    onBackClick: () -> Unit,
    viewModel: WeatherViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    WeatherScreenContent(
        weatherInfo = uiState.weatherInfo,
        isLoading = uiState.isLoading,
        isError = uiState.isError,
        errorMessage = uiState.errorMessage,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreenContent(
    weatherInfo: WeatherInfo?,
    isLoading: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weather Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading && weatherInfo == null) {
                CircularProgressIndicator()
            } else if (isError && weatherInfo == null) {
                Text(
                    text = "Error: ${errorMessage ?: "Unknown error"}",
                    color = MaterialTheme.colorScheme.error
                )
            } else if (weatherInfo != null) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = weatherInfo.city,
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "${weatherInfo.temperature}°C",
                        style = MaterialTheme.typography.displayLarge
                    )
                    Text(
                        text = weatherInfo.condition,
                        style = MaterialTheme.typography.titleMedium
                    )
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
            weatherInfo = WeatherInfo(
                temperature = 22.0,
                condition = "Sunny",
                city = "London"
            ),
            onBackClick = {}
        )
    }
}
