package com.example.myapp.core.data.di

import app.cash.sqldelight.db.SqlDriver
import com.example.myapp.core.data.datasource.HomeLocalDataSource
import com.example.myapp.core.data.datasource.HomeLocalDataSourceImpl
import com.example.myapp.core.data.datasource.HomeRemoteDataSource
import com.example.myapp.core.data.datasource.HomeRemoteDataSourceImpl
import com.example.myapp.core.data.repository.HomeRepositoryImpl
import com.example.myapp.core.data.repository.LocationPreferencesRepositoryImpl
import com.example.myapp.core.data.repository.SettingsRepositoryImpl
import com.example.myapp.core.database.AppDatabase
import com.example.myapp.core.domain.model.Settings
import com.example.myapp.libs.database.di.databaseModule
import com.example.myapp.core.domain.repository.HomeRepository
import com.example.myapp.core.domain.repository.LocationPreferencesRepository
import com.example.myapp.core.domain.repository.SettingsRepository
import com.example.myapp.libs.database.DatabaseDriverFactory
import com.example.myapp.libs.network.di.networkModule
import com.example.myapp.libs.settings.SettingsDataStore
import com.example.myapp.libs.settings.SettingsDataStoreFactory
import org.koin.dsl.module

val dataModule = module {
    includes(databaseModule, networkModule)
    single<SqlDriver> { get<DatabaseDriverFactory>().createDriver(AppDatabase.Schema, "app.db") }
    single { AppDatabase(driver = get()) }

    single<SettingsDataStore<Settings>> {
        get<SettingsDataStoreFactory>().create(
            fileName = "settings.json",
            defaultValue = Settings(),
            serializer = Settings.serializer()
        )
    }

    single<HomeRemoteDataSource> { HomeRemoteDataSourceImpl(get()) }
    single<HomeLocalDataSource> { HomeLocalDataSourceImpl(get()) }
    single<HomeRepository> { HomeRepositoryImpl(get(), get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
    single<LocationPreferencesRepository> { LocationPreferencesRepositoryImpl(get()) }
}
