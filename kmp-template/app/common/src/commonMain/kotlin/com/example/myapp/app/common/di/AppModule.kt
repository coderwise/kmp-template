package com.example.myapp.app.common.di

import com.example.myapp.app.common.navigation.AppViewModel
import com.example.myapp.core.data.di.dataModule
import com.example.myapp.feature.home.di.homeModule
import com.example.myapp.feature.settings.di.featureSettingsModule
import com.example.myapp.feature.weather.di.weatherModule
import com.example.myapp.libs.location.di.locationModule
import com.example.myapp.libs.settings.di.settingsModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    includes(dataModule, homeModule, weatherModule, settingsModule, featureSettingsModule, locationModule)
    viewModelOf(::AppViewModel)
}
