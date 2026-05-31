package com.example.myapp.feature.weather.domain.usecase

import com.example.myapp.core.domain.model.Location
import com.example.myapp.core.domain.repository.LocationPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveSelectedLocationUseCase(
    private val repository: LocationPreferencesRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend operator fun invoke(location: Location) = withContext(dispatcher) {
        repository.saveSelectedLocation(location)
    }
}
