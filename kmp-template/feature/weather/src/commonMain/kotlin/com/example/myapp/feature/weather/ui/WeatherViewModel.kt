package com.example.myapp.feature.weather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.core.domain.model.onError
import com.example.myapp.core.domain.model.onSuccess
import com.example.myapp.core.domain.model.Location
import com.example.myapp.feature.weather.domain.usecase.GetCurrentLocationUseCase
import com.example.myapp.feature.weather.domain.usecase.GetSelectedLocationUseCase
import com.example.myapp.feature.weather.domain.usecase.GetWeatherUseCase
import com.example.myapp.feature.weather.domain.usecase.ReverseGeocodeUseCase
import com.example.myapp.feature.weather.domain.usecase.SaveSelectedLocationUseCase
import com.example.myapp.feature.weather.domain.usecase.SearchLocationsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val searchLocationsUseCase: SearchLocationsUseCase,
    private val reverseGeocodeUseCase: ReverseGeocodeUseCase,
    private val getSelectedLocationUseCase: GetSelectedLocationUseCase,
    private val saveSelectedLocationUseCase: SaveSelectedLocationUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()
    private var searchJob: Job? = null

    init {
        loadSavedLocation()
    }

    private fun loadSavedLocation() {
        viewModelScope.launch {
            val savedLocation = getSelectedLocationUseCase().firstOrNull()
            if (savedLocation != null) {
                fetchWeather(savedLocation.latitude, savedLocation.longitude, savedLocation.name)
            } else {
                // Default location
                fetchWeather(51.5074, -0.1278, "London")
            }
        }
    }

    fun onEvent(event: WeatherUiEvent) {
        when (event) {
            is WeatherUiEvent.OnSearchQueryChange -> onSearchQueryChange(event.query)
            is WeatherUiEvent.OnLocationSelected -> onLocationSelected(event.location)
            is WeatherUiEvent.OnSearchClick -> onSearchClick()
            is WeatherUiEvent.OnCurrentLocationClick -> onCurrentLocationClick()
        }
    }

    private fun onCurrentLocationClick() {
        viewModelScope.launch {
            _uiState.update { it.loading() }
            getCurrentLocationUseCase()
                .onSuccess { current ->
                    val lat = current.latitude
                    val lon = current.longitude

                    reverseGeocodeUseCase(lat, lon).onSuccess { location ->
                        onLocationSelected(location)
                    }.onError { _, _ ->
                        onLocationSelected(
                            Location(
                                name = "Current Location",
                                latitude = lat,
                                longitude = lon
                            )
                        )
                    }
                }
                .onError { exception, message ->
                    _uiState.update { it.error(message ?: exception.message) }
                }
        }
    }

    private fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        if (query.length >= 2) {
            searchLocations(query.trim())
        } else {
            searchJob?.cancel()
            _uiState.update { it.copy(searchResults = emptyList()) }
        }
    }

    private fun searchLocations(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchLocationsUseCase(query).onSuccess { data ->
                if (_uiState.value.searchQuery == query) {
                    _uiState.update { it.copy(searchResults = data) }
                }
            }
        }
    }

    private fun onLocationSelected(location: Location) {
        _uiState.update { it.copy(searchResults = emptyList()) }
        viewModelScope.launch {
            saveSelectedLocationUseCase(location)
        }
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
            getWeatherUseCase(latitude, longitude, city)
                .onSuccess { data ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            weatherInfo = data,
                            isError = false
                        )
                    }
                }
                .onError { exception, message ->
                    _uiState.update { it.error(message ?: exception.message) }
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
