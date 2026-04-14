package com.example.myapp.feature.weather.domain.usecase

import com.example.myapp.core.domain.Result
import com.example.myapp.feature.weather.domain.model.Location
import com.example.myapp.feature.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchLocationsUseCase(
    private val repository: WeatherRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend operator fun invoke(query: String): Result<List<Location>> {
        return withContext(dispatcher) {
            repository.searchLocations(query)
        }
    }
}
