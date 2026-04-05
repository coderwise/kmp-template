package com.example.myapp.core.database.di

import com.example.myapp.core.database.AppDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

expect val databaseDriverModule: Module

val databaseModule = module {
    includes(databaseDriverModule)
    single { AppDatabase(get()) }
}
