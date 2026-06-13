package com.example.myapp.feature.weather.domain.usecase

import com.example.myapp.core.domain.model.Location
import com.example.myapp.core.domain.repository.LocationPreferencesRepository
import kotlinx.coroutines.flow.Flow

interface GetSelectedLocationUseCase {
    operator fun invoke(): Flow<Location?>
}

class GetSelectedLocationUseCaseImpl(
    private val repository: LocationPreferencesRepository
) : GetSelectedLocationUseCase {
    override operator fun invoke(): Flow<Location?> = repository.getSelectedLocation()
}
