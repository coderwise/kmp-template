package com.example.myapp.feature.weather.di

import com.example.myapp.core.api.OpenMeteoApi
import com.example.myapp.feature.weather.data.repository.WeatherRepositoryImpl
import com.example.myapp.feature.weather.domain.repository.WeatherRepository
import com.example.myapp.feature.weather.domain.usecase.GetWeatherUseCase
import com.example.myapp.feature.weather.domain.usecase.SearchLocationsUseCase
import com.example.myapp.feature.weather.ui.WeatherViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val weatherModule = module {
    single { OpenMeteoApi(get()) }
    single<WeatherRepository> { WeatherRepositoryImpl(get()) }
    factory { GetWeatherUseCase(get()) }
    factory { SearchLocationsUseCase(get()) }
    viewModel { WeatherViewModel(get(), get(), get()) }
}
