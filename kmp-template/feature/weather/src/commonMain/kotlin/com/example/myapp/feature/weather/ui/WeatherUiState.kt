package com.example.myapp.feature.weather.ui

import com.example.myapp.core.domain.model.Location
import com.example.myapp.feature.weather.domain.model.WeatherInfo

data class WeatherUiState(
    val searchQuery: String = "",
    val searchResults: List<Location> = emptyList(),
    val weatherInfo: WeatherInfo? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null
)
