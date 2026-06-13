package com.example.myapp.feature.weather.di

import com.example.myapp.core.api.OpenMeteoApi
import com.example.myapp.feature.weather.data.repository.WeatherRepositoryImpl
import com.example.myapp.feature.weather.domain.repository.WeatherRepository
import com.example.myapp.feature.weather.domain.usecase.GetCurrentLocationUseCase
import com.example.myapp.feature.weather.domain.usecase.GetCurrentLocationUseCaseImpl
import com.example.myapp.feature.weather.domain.usecase.GetSelectedLocationUseCase
import com.example.myapp.feature.weather.domain.usecase.GetSelectedLocationUseCaseImpl
import com.example.myapp.feature.weather.domain.usecase.GetWeatherUseCase
import com.example.myapp.feature.weather.domain.usecase.GetWeatherUseCaseImpl
import com.example.myapp.feature.weather.domain.usecase.ReverseGeocodeUseCase
import com.example.myapp.feature.weather.domain.usecase.ReverseGeocodeUseCaseImpl
import com.example.myapp.feature.weather.domain.usecase.SaveSelectedLocationUseCase
import com.example.myapp.feature.weather.domain.usecase.SaveSelectedLocationUseCaseImpl
import com.example.myapp.feature.weather.domain.usecase.SearchLocationsUseCase
import com.example.myapp.feature.weather.domain.usecase.SearchLocationsUseCaseImpl
import com.example.myapp.feature.weather.ui.WeatherViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val weatherModule = module {
    single { OpenMeteoApi(get()) }
    single<WeatherRepository> { WeatherRepositoryImpl(get()) }
    factory<GetWeatherUseCase> { GetWeatherUseCaseImpl(get()) }
    factory<SearchLocationsUseCase> { SearchLocationsUseCaseImpl(get()) }
    factory<ReverseGeocodeUseCase> { ReverseGeocodeUseCaseImpl(get()) }
    factory<GetSelectedLocationUseCase> { GetSelectedLocationUseCaseImpl(get()) }
    factory<SaveSelectedLocationUseCase> { SaveSelectedLocationUseCaseImpl(get()) }
    factory<GetCurrentLocationUseCase> { GetCurrentLocationUseCaseImpl(get()) }
    viewModel {
        WeatherViewModel(
            getWeatherUseCase = get(),
            searchLocationsUseCase = get(),
            reverseGeocodeUseCase = get(),
            getSelectedLocationUseCase = get(),
            saveSelectedLocationUseCase = get(),
            getCurrentLocationUseCase = get(),
        )
    }
}
