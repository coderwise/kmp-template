package com.example.myapp.feature.weather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.core.domain.Result
import com.example.myapp.feature.weather.domain.model.WeatherInfo
import com.example.myapp.feature.weather.domain.usecase.GetWeatherUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<Result<WeatherInfo>>(Result.Loading)
    val state: StateFlow<Result<WeatherInfo>> = _state.asStateFlow()

    init {
        fetchWeather("London")
    }

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            _state.value = Result.Loading
            _state.value = getWeatherUseCase(city)
        }
    }
}
