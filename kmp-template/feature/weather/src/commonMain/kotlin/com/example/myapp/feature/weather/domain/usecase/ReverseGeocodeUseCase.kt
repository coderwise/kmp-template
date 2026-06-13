package com.example.myapp.feature.weather.domain.usecase

import com.example.myapp.core.domain.model.Location
import com.example.myapp.feature.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ReverseGeocodeUseCase {
    suspend operator fun invoke(latitude: Double, longitude: Double): Result<Location>
}

class ReverseGeocodeUseCaseImpl(
    private val repository: WeatherRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : ReverseGeocodeUseCase {
    override suspend operator fun invoke(latitude: Double, longitude: Double): Result<Location> =
        withContext(dispatcher) {
            repository.reverseGeocode(latitude, longitude)
        }
}
