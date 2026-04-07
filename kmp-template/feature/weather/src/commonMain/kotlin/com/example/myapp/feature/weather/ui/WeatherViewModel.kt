package com.example.myapp.feature.weather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.core.domain.Result
import com.example.myapp.feature.weather.domain.model.WeatherInfo
import com.example.myapp.feature.weather.domain.usecase.GetWeatherUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WeatherUiState(
    val weatherInfo: WeatherInfo? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null
)

class WeatherViewModel(
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        fetchWeather("London")
    }

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isError = false) }
            val result = getWeatherUseCase(city)
            _uiState.update { state ->
                when (result) {
                    is Result.Loading -> state.copy(isLoading = true, isError = false)
                    is Result.Success -> state.copy(isLoading = false, weatherInfo = result.data, isError = false)
                    is Result.Error -> state.copy(
                        isLoading = false,
                        isError = true,
                        errorMessage = result.message ?: result.exception.message
                    )
                }
            }
        }
    }
}
