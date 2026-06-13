package com.example.myapp.feature.weather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.core.domain.model.Location
import com.example.myapp.core.ui.state.error
import com.example.myapp.core.ui.state.loading
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
import kotlinx.coroutines.delay
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
            val location = getSelectedLocationUseCase().firstOrNull() ?: DEFAULT_LOCATION
            fetchWeather(location.latitude, location.longitude, location.name)
        }
    }

    fun onEvent(event: WeatherUiEvent) {
        when (event) {
            is WeatherUiEvent.OnSearchQueryChange -> onSearchQueryChange(event.query)
            is WeatherUiEvent.OnLocationSelected -> onLocationSelected(event.location)
            is WeatherUiEvent.OnSearchClick -> onSearchClick()
            is WeatherUiEvent.OnCurrentLocationClick -> onCurrentLocationClick(event.currentLocationLabel)
        }
    }

    private fun onCurrentLocationClick(currentLocationLabel: String) {
        viewModelScope.launch {
            _uiState.update { it.loading() }
            getCurrentLocationUseCase()
                .onSuccess { current ->
                    val lat = current.latitude
                    val lon = current.longitude

                    reverseGeocodeUseCase(lat, lon).onSuccess { location ->
                        onLocationSelected(location)
                    }.onFailure {
                        // Reverse geocoding failed; fall back to the caller-supplied
                        // localized label rather than a hardcoded English string.
                        onLocationSelected(
                            Location(
                                name = currentLocationLabel,
                                latitude = lat,
                                longitude = lon
                            )
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update { it.error(throwable.message) }
                }
        }
    }

    private fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        if (query.length >= MIN_SEARCH_QUERY_LENGTH) {
            searchLocations(query.trim())
        } else {
            searchJob?.cancel()
            _uiState.update { it.copy(searchResults = emptyList()) }
        }
    }

    private fun searchLocations(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            // Debounce: a new keystroke cancels this job before the delay elapses,
            // so we only hit the network once the user pauses typing.
            delay(SEARCH_DEBOUNCE_MS)
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
                .onFailure { throwable ->
                    _uiState.update { it.error(throwable.message) }
                }
        }
    }

    private companion object {
        // Seed location shown until the user selects one. The name is a proper
        // noun (the screen renders API place names untranslated too), so it lives
        // here as data rather than a localized resource.
        val DEFAULT_LOCATION = Location(name = "London", latitude = 51.5074, longitude = -0.1278)
        const val MIN_SEARCH_QUERY_LENGTH = 2
        const val SEARCH_DEBOUNCE_MS = 300L
    }
}
