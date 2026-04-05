package com.example.myapp.feature.weather.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherInfo(
    val temperature: Double,
    val condition: String,
    val city: String
)
