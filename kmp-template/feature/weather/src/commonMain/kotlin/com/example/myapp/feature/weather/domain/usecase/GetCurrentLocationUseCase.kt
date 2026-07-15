package com.example.myapp.feature.weather.domain.usecase

import com.example.myapp.core.domain.arch.SuspendResultUseCase
import com.example.myapp.core.domain.model.Location
import com.coderwise.libs.location.LocationProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Resolves the device's current GPS position into a [Location].
 */
class GetCurrentLocationUseCase(
    private val locationProvider: LocationProvider,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) : SuspendResultUseCase<Result<Location>>(dispatcher) {
    override suspend fun execute(): Result<Location> =
        locationProvider.getCurrentLocation().map { gps ->
            Location(name = "", latitude = gps.latitude, longitude = gps.longitude)
        }
}
