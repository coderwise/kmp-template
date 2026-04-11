package com.example.myapp.app.common.di

import com.example.myapp.core.data.di.dataModule
import com.example.myapp.feature.home.di.homeModule
import com.example.myapp.feature.weather.di.weatherModule
import org.koin.dsl.module

val appModule = module {
    includes(dataModule, homeModule, weatherModule)
}
