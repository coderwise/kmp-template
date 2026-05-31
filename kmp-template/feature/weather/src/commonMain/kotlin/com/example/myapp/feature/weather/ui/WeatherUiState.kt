package com.example.myapp.feature.weather.ui

import com.example.myapp.core.domain.model.Location
import com.example.myapp.core.ui.state.LoadableState
import com.example.myapp.feature.weather.domain.model.WeatherInfo

data class WeatherUiState(
    val searchQuery: String = "",
    val searchResults: List<Location> = emptyList(),
    val weatherInfo: WeatherInfo? = null,
    override val isLoading: Boolean = false,
    override val isError: Boolean = false,
    override val errorMessage: String? = null
) : LoadableState<WeatherUiState> {
    override fun copyLoadState(isLoading: Boolean, isError: Boolean, errorMessage: String?) =
        copy(isLoading = isLoading, isError = isError, errorMessage = errorMessage)
}
