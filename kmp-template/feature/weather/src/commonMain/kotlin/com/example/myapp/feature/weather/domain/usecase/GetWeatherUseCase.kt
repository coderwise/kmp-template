package com.example.myapp.feature.weather.domain.usecase

import com.example.myapp.feature.weather.domain.model.WeatherInfo
import com.example.myapp.feature.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface GetWeatherUseCase {
    suspend operator fun invoke(latitude: Double, longitude: Double, city: String): Result<WeatherInfo>
}

class GetWeatherUseCaseImpl(
    private val repository: WeatherRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : GetWeatherUseCase {
    override suspend operator fun invoke(latitude: Double, longitude: Double, city: String): Result<WeatherInfo> =
        withContext(dispatcher) {
            repository.getWeather(latitude, longitude, city)
        }
}
