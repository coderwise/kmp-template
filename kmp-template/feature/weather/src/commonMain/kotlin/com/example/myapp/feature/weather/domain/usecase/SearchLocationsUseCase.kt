package com.example.myapp.feature.weather.domain.usecase

import com.example.myapp.core.domain.arch.SuspendUseCase
import com.example.myapp.core.domain.model.Location
import com.example.myapp.feature.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class SearchLocationsUseCase(
    private val repository: WeatherRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) : SuspendUseCase<String, Result<List<Location>>>(dispatcher) {
    override suspend fun execute(params: String): Result<List<Location>> =
        repository.searchLocations(params)
}
