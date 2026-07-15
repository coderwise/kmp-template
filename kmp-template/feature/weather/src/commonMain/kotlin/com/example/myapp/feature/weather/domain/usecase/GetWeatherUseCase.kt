package com.example.myapp.feature.weather.domain.usecase

import com.example.myapp.core.domain.arch.SuspendUseCase
import com.example.myapp.feature.weather.domain.model.WeatherInfo
import com.example.myapp.feature.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class GetWeatherUseCase(
    private val repository: WeatherRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) : SuspendUseCase<GetWeatherUseCase.Params, Result<WeatherInfo>>(dispatcher) {

    data class Params(val latitude: Double, val longitude: Double, val city: String)

    suspend operator fun invoke(latitude: Double, longitude: Double, city: String) =
        invoke(Params(latitude, longitude, city))

    override suspend fun execute(params: Params): Result<WeatherInfo> =
        repository.getWeather(params.latitude, params.longitude, params.city)
}
