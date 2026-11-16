package com.example.myapp.feature.weather.domain.usecase

import com.example.myapp.core.domain.arch.FlowResultUseCase
import com.example.myapp.core.domain.model.Location
import com.example.myapp.core.domain.repository.LocationPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class GetSelectedLocationUseCase(
    private val repository: LocationPreferencesRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) : FlowResultUseCase<Location?>(dispatcher) {
    override fun execute(): Flow<Location?> = repository.getSelectedLocation()
}
