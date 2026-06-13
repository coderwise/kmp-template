package com.example.myapp.feature.weather.ui

import com.example.myapp.core.domain.model.Location

sealed interface WeatherUiEvent {
    data class OnSearchQueryChange(val query: String) : WeatherUiEvent
    data class OnLocationSelected(val location: Location) : WeatherUiEvent
    data object OnSearchClick : WeatherUiEvent

    /**
     * @param currentLocationLabel localized fallback name shown when reverse
     * geocoding the device position fails. Resolved in the UI layer so the
     * ViewModel stays free of presentation resources.
     */
    data class OnCurrentLocationClick(val currentLocationLabel: String) : WeatherUiEvent
}
