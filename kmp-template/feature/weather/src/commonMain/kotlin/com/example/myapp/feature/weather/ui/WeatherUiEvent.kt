package com.example.myapp.feature.weather.ui

import com.example.myapp.feature.weather.domain.model.Location

sealed interface WeatherUiEvent {
    data class OnSearchQueryChange(val query: String) : WeatherUiEvent
    data class OnLocationSelected(val location: Location) : WeatherUiEvent
    data object OnSearchClick : WeatherUiEvent
    data object OnBackClick : WeatherUiEvent
}
