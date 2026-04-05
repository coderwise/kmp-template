package com.example.myapp.core.database.di

import com.example.myapp.core.database.AppDatabase
import com.example.myapp.core.database.DatabaseDriverFactory
import org.koin.dsl.module

val databaseModule = module {
    single { DatabaseDriverFactory(get()).createDriver() }
    single { AppDatabase(get()) }
}
