package com.example.myapp.feature.weather.domain.usecase

import com.example.myapp.core.common.Result
import com.example.myapp.feature.weather.domain.model.WeatherInfo
import com.example.myapp.feature.weather.domain.repository.WeatherRepository

class GetWeatherUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(city: String): Result<WeatherInfo> {
        return repository.getWeather(city)
    }
}
