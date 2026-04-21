package com.example.myapp.feature.weather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.core.domain.model.Result
import com.example.myapp.feature.weather.domain.model.Location
import com.example.myapp.feature.weather.domain.usecase.GetWeatherUseCase
import com.example.myapp.feature.weather.domain.usecase.SearchLocationsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val searchLocationsUseCase: SearchLocationsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        // Default location
        fetchWeather(51.5074, -0.1278, "London")
    }

    fun onEvent(event: WeatherUiEvent) {
        when (event) {
            is WeatherUiEvent.OnSearchQueryChange -> onSearchQueryChange(event.query)
            is WeatherUiEvent.OnLocationSelected -> onLocationSelected(event.location)
            WeatherUiEvent.OnSearchClick -> onSearchClick()
            WeatherUiEvent.OnBackClick -> { /* Handled in Screen */ }
        }
    }

    private fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        if (query.length >= 2) {
            searchLocations(query)
        } else {
            _uiState.update { it.copy(searchResults = emptyList()) }
        }
    }

    private fun searchLocations(query: String) {
        viewModelScope.launch {
            val result = searchLocationsUseCase(query)
            if (result is Result.Success) {
                _uiState.update { it.copy(searchResults = result.data) }
            }
        }
    }

    private fun onLocationSelected(location: Location) {
        _uiState.update { it.copy(searchQuery = location.name, searchResults = emptyList()) }
        fetchWeather(location.latitude, location.longitude, location.name)
    }

    private fun onSearchClick() {
        val firstResult = _uiState.value.searchResults.firstOrNull()
        if (firstResult != null) {
            onLocationSelected(firstResult)
        }
    }

    private fun fetchWeather(latitude: Double, longitude: Double, city: String) {
        viewModelScope.launch {
            _uiState.update { it.loading() }
            val result = getWeatherUseCase(latitude, longitude, city)
            _uiState.update { state ->
                when (result) {
                    is Result.Loading -> state.loading()
                    is Result.Success -> state.copy(isLoading = false, weatherInfo = result.data, isError = false)
                    is Result.Error -> state.error(result.message ?: result.exception.message)
                }
            }
        }
    }
}

private fun WeatherUiState.loading() = copy(
    isLoading = true, isError = false
)

private fun WeatherUiState.error(message: String?) = copy(
    isLoading = false, isError = true, errorMessage = message
)