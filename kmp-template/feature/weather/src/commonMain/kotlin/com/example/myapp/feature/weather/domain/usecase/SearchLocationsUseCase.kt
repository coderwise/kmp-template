package com.example.myapp.feature.weather.domain.usecase

import com.example.myapp.core.domain.Result
import com.example.myapp.feature.weather.domain.model.Location
import com.example.myapp.feature.weather.domain.repository.WeatherRepository

class SearchLocationsUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(query: String): Result<List<Location>> {
        return repository.searchLocations(query)
    }
}
