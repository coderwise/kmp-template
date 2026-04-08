package com.example.myapp.feature.weather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.core.domain.Result
import com.example.myapp.feature.weather.domain.model.Location
import com.example.myapp.feature.weather.domain.model.WeatherInfo
import com.example.myapp.feature.weather.domain.usecase.GetWeatherUseCase
import com.example.myapp.feature.weather.domain.usecase.SearchLocationsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WeatherUiState(
    val searchQuery: String = "",
    val searchResults: List<Location> = emptyList(),
    val weatherInfo: WeatherInfo? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null
)

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

    fun onSearchQueryChange(query: String) {
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

    fun onLocationSelected(location: Location) {
        _uiState.update { it.copy(searchQuery = location.name, searchResults = emptyList()) }
        fetchWeather(location.latitude, location.longitude, location.name)
    }

    fun onSearchClick() {
        val firstResult = _uiState.value.searchResults.firstOrNull()
        if (firstResult != null) {
            onLocationSelected(firstResult)
        }
    }

    fun fetchWeather(latitude: Double, longitude: Double, city: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isError = false) }
            val result = getWeatherUseCase(latitude, longitude, city)
            _uiState.update { state ->
                when (result) {
                    is Result.Loading -> state.copy(isLoading = true, isError = false)
                    is Result.Success -> state.copy(isLoading = false, weatherInfo = result.data, isError = false)
                    is Result.Error -> state.copy(
                        isLoading = false,
                        isError = true,
                        errorMessage = result.message ?: result.exception.message
                    )
                }
            }
        }
    }
}
