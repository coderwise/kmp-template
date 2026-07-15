package com.example.myapp.feature.weather.domain.usecase

import com.example.myapp.core.domain.arch.SuspendUseCase
import com.example.myapp.core.domain.model.Location
import com.example.myapp.feature.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class ReverseGeocodeUseCase(
    private val repository: WeatherRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) : SuspendUseCase<ReverseGeocodeUseCase.Params, Result<Location>>(dispatcher) {

    data class Params(val latitude: Double, val longitude: Double)

    suspend operator fun invoke(latitude: Double, longitude: Double) = invoke(Params(latitude, longitude))

    override suspend fun execute(params: Params): Result<Location> =
        repository.reverseGeocode(params.latitude, params.longitude)
}
