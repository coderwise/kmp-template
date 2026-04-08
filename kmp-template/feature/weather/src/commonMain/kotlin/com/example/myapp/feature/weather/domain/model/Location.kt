package com.example.myapp.feature.weather.domain.model

data class Location(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String? = null,
    val admin1: String? = null
)
