package com.example.myapp.app.common.di

import com.example.myapp.app.common.navigation.AppViewModel
import com.example.myapp.core.data.di.dataModule
import com.example.myapp.core.ui.di.uiModule
import com.example.myapp.feature.auth.di.authModule
import com.example.myapp.feature.home.di.homeModule
import com.example.myapp.feature.settings.di.featureSettingsModule
import com.example.myapp.feature.weather.di.weatherModule
import com.example.myapp.libs.location.di.locationModule
import com.coderwise.libs.settings.di.settingsModule
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    includes(
        dataModule,
        uiModule,
        authModule,
        homeModule,
        weatherModule,
        settingsModule,
        featureSettingsModule,
        locationModule
    )
    viewModel { AppViewModel(get(), get(), get()) }
}
