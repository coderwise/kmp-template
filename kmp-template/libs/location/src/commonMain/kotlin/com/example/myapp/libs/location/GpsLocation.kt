package com.example.myapp.libs.location

data class GpsLocation(
    val latitude: Double,
    val longitude: Double,
    val bearing: Float? = null,
    val elevation: Double? = null,
    val time: Long? = null,
    val accuracy: Float? = null,
    val speed: Float? = null
)
