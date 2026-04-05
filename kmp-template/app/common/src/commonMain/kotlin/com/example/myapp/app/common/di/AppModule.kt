package com.example.myapp.app.common.di

import com.example.myapp.core.database.di.databaseModule
import com.example.myapp.core.network.di.networkModule
import com.example.myapp.feature.home.di.homeModule
import com.example.myapp.feature.weather.di.weatherModule
import org.koin.dsl.module

val appModule = module {
    includes(networkModule, databaseModule, homeModule, weatherModule)
}
