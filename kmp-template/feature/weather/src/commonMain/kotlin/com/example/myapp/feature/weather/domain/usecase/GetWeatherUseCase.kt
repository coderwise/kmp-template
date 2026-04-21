package com.example.myapp.feature.weather.domain.usecase

import com.example.myapp.core.domain.model.Result
import com.example.myapp.feature.weather.domain.model.WeatherInfo
import com.example.myapp.feature.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class GetWeatherUseCase(
    private val repository: WeatherRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend operator fun invoke(latitude: Double, longitude: Double, city: String): Result<WeatherInfo> {
        return withContext(dispatcher) {
            repository.getWeather(latitude, longitude, city)
        }
    }
}
