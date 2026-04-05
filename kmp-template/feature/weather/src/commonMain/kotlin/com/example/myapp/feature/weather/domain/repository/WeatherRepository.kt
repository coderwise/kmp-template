package com.example.myapp.feature.weather.domain.repository

import com.example.myapp.feature.weather.domain.model.WeatherInfo
import com.example.myapp.core.common.Result

interface WeatherRepository {
    suspend fun getWeather(city: String): Result<WeatherInfo>
}
