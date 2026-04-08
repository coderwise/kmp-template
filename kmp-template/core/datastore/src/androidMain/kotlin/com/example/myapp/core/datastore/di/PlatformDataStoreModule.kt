package com.example.myapp.core.datastore.di

import com.example.myapp.core.datastore.DataStoreProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformDataStoreModule: Module = module {
    single { DataStoreProvider(androidContext()) }
}
