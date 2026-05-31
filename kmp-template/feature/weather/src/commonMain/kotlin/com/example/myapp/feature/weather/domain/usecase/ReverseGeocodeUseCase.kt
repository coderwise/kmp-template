package com.example.myapp.feature.weather.domain.usecase

import com.example.myapp.core.domain.model.Location
import com.example.myapp.feature.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReverseGeocodeUseCase(
    private val repository: WeatherRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend operator fun invoke(latitude: Double, longitude: Double): Result<Location> {
        return withContext(dispatcher) {
            repository.reverseGeocode(latitude, longitude)
        }
    }
}
