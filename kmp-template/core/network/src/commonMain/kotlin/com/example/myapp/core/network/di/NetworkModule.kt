package com.example.myapp.core.network.di

import com.example.myapp.core.network.createHttpClient
import org.koin.dsl.module

val networkModule = module {
    single { createHttpClient() }
}
