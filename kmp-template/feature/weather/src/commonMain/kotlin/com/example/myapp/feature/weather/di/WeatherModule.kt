package com.example.myapp.feature.weather.di

import com.example.myapp.core.api.OpenMeteoApi
import com.example.myapp.feature.weather.data.repository.SettingsRepositoryImpl
import com.example.myapp.feature.weather.data.repository.WeatherRepositoryImpl
import com.example.myapp.feature.weather.domain.model.Location
import com.example.myapp.feature.weather.domain.repository.SettingsRepository
import com.example.myapp.feature.weather.domain.repository.WeatherRepository
import com.example.myapp.feature.weather.domain.usecase.GetWeatherUseCase
import com.example.myapp.feature.weather.domain.usecase.SearchLocationsUseCase
import com.example.myapp.feature.weather.ui.WeatherViewModel
import com.example.myapp.libs.settings.SettingsDataStoreFactory
import kotlinx.serialization.serializer
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val weatherModule = module {
    single { OpenMeteoApi(get()) }
    single<WeatherRepository> { WeatherRepositoryImpl(get()) }
    single {
        get<SettingsDataStoreFactory>().create(
            fileName = "weather_settings",
            defaultValue = null,
            serializer = serializer<Location?>()
        )
    }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
    factory { GetWeatherUseCase(get()) }
    factory { SearchLocationsUseCase(get()) }
    viewModel { WeatherViewModel(get(), get(), get()) }
}
