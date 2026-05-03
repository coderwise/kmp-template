package com.example.myapp.core.data.di

import com.example.myapp.core.data.repository.HomeRepositoryImpl
import com.example.myapp.core.data.repository.SettingsRepositoryImpl
import com.example.myapp.core.domain.model.Settings
import com.example.myapp.libs.database.di.databaseModule
import com.example.myapp.core.domain.repository.HomeRepository
import com.example.myapp.core.domain.repository.SettingsRepository
import com.example.myapp.libs.network.di.networkModule
import com.example.myapp.libs.settings.SettingsDataStore
import com.example.myapp.libs.settings.SettingsDataStoreFactory
import org.koin.dsl.module

val dataModule = module {
    includes(databaseModule, networkModule)

    single<SettingsDataStore<Settings>> {
        get<SettingsDataStoreFactory>().create(
            fileName = "settings.json",
            defaultValue = Settings(),
            serializer = Settings.serializer()
        )
    }

    single<HomeRepository> { HomeRepositoryImpl(get(), get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
}
