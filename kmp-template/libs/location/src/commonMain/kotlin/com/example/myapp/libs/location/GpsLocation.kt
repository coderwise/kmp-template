package com.example.myapp.libs.location

data class GpsLocation(
    val latitude: Double,
    val longitude: Double,
    val bearing: Float? = null
)
