package com.example.myapp.feature.weather.domain.usecase

import com.example.myapp.core.domain.model.Location
import com.example.myapp.feature.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface SearchLocationsUseCase {
    suspend operator fun invoke(query: String): Result<List<Location>>
}

class SearchLocationsUseCaseImpl(
    private val repository: WeatherRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : SearchLocationsUseCase {
    override suspend operator fun invoke(query: String): Result<List<Location>> =
        withContext(dispatcher) {
            repository.searchLocations(query)
        }
}
