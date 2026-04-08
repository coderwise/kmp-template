package com.example.myapp.core.database.di

import com.example.myapp.core.database.DataStoreProvider
import com.example.myapp.core.database.DatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val databaseDriverModule: Module = module {
    single { DatabaseDriverFactory() }
}
