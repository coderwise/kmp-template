package com.example.myapp.core.database.di

import app.cash.sqldelight.db.SqlDriver
import com.example.myapp.core.database.AppDatabase
import com.example.myapp.core.database.DatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

expect val databaseDriverModule: Module

val databaseModule = module {
    includes(databaseDriverModule)
    single<SqlDriver> { get<DatabaseDriverFactory>().createDriver() }
    single { AppDatabase(get()) }
}
