package com.example.myapp.libs.location

import kotlinx.coroutines.flow.Flow

sealed class LocationResult<out T> {
    data class Success<T>(val data: T) : LocationResult<T>()
    data class Error(val exception: Throwable, val message: String? = null) : LocationResult<Nothing>()
}

interface LocationProvider {
    suspend fun getCurrentLocation(): LocationResult<GpsLocation>

    fun locationUpdates(): Flow<LocationResult<GpsLocation>>
}
