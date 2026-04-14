package com.example.myapp.core.data.di

import com.example.myapp.core.data.repository.HomeRepositoryImpl
import com.example.myapp.core.data.repository.PreferencesRepositoryImpl
import com.example.myapp.libs.database.di.databaseModule
import com.example.myapp.core.domain.PreferencesRepository
import com.example.myapp.core.domain.repository.HomeRepository
import com.example.myapp.libs.network.di.networkModule
import org.koin.dsl.module

val dataModule = module {
    includes(databaseModule, networkModule)
    single<PreferencesRepository> { PreferencesRepositoryImpl(get()) }
    single<HomeRepository> { HomeRepositoryImpl(get(), get()) }
}
