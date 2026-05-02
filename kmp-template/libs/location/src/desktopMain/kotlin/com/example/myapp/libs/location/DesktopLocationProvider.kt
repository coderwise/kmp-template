package com.example.myapp.libs.location

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DesktopLocationProvider : LocationProvider {
    override suspend fun getCurrentLocation(): LocationResult<GpsLocation> =
        LocationResult.Error(
            UnsupportedOperationException("Location not supported on desktop"),
            "Location not supported on desktop"
        )

    override fun locationUpdates(): Flow<LocationResult<GpsLocation>> = flowOf(
        LocationResult.Error(
            UnsupportedOperationException("Location not supported on desktop"),
            "Location not supported on desktop"
        )
    )
}
