package com.example.myapp.feature.weather.domain.usecase

import com.example.myapp.core.domain.model.Location
import com.example.myapp.core.domain.model.Result
import com.example.myapp.libs.location.LocationProvider
import com.example.myapp.libs.location.LocationResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Resolves the device's current GPS position into a [Location] (without a
 * display name — callers reverse-geocode if they need one). Wraps the
 * platform [LocationProvider] so ViewModels depend on the domain layer only.
 */
class GetCurrentLocationUseCase(
    private val locationProvider: LocationProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend operator fun invoke(): Result<Location> = withContext(dispatcher) {
        when (val result = locationProvider.getCurrentLocation()) {
            is LocationResult.Success -> Result.Success(
                Location(
                    name = "",
                    latitude = result.data.latitude,
                    longitude = result.data.longitude
                )
            )
            is LocationResult.Error -> Result.Error(result.exception, result.message)
        }
    }
}
