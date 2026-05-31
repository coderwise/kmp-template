package com.example.myapp.feature.weather.domain.usecase

import com.example.myapp.core.domain.model.Location
import com.example.myapp.core.domain.repository.LocationPreferencesRepository
import kotlinx.coroutines.flow.Flow

class GetSelectedLocationUseCase(
    private val repository: LocationPreferencesRepository
) {
    operator fun invoke(): Flow<Location?> = repository.getSelectedLocation()
}
