package com.example.myapp.feature.weather.ui

sealed interface WeatherNavEvent {
    data object Back : WeatherNavEvent
}
