package com.example.myapp.feature.weather.domain.usecase

import com.example.myapp.core.domain.arch.SuspendUseCase
import com.example.myapp.core.domain.model.Location
import com.example.myapp.core.domain.repository.LocationPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class SaveSelectedLocationUseCase(
    private val repository: LocationPreferencesRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) : SuspendUseCase<Location, Unit>(dispatcher) {
    override suspend fun execute(params: Location) = repository.saveSelectedLocation(params)
}
