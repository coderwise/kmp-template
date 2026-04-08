package com.example.myapp.core.datastore.di

import com.example.myapp.core.datastore.DataStoreProvider
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformDataStoreModule: Module

val dataStoreModule = module {
    includes(platformDataStoreModule)
    single { get<DataStoreProvider>().createDataStore() }
}
