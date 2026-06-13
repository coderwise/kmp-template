package com.example.myapp.feature.weather.domain.usecase

import com.example.myapp.core.domain.model.Location
import com.example.myapp.libs.location.LocationProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Resolves the device's current GPS position into a [Location] (without a
 * display name — callers reverse-geocode if they need one). Wraps the
 * platform [LocationProvider] so ViewModels depend on the domain layer only.
 */
interface GetCurrentLocationUseCase {
    suspend operator fun invoke(): Result<Location>
}

class GetCurrentLocationUseCaseImpl(
    private val locationProvider: LocationProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : GetCurrentLocationUseCase {
    override suspend operator fun invoke(): Result<Location> = withContext(dispatcher) {
        locationProvider.getCurrentLocation().map { gps ->
            Location(name = "", latitude = gps.latitude, longitude = gps.longitude)
        }
    }
}
