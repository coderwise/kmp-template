package com.example.myapp.libs.network.di

import com.example.myapp.libs.network.createHttpClient
import org.koin.dsl.module

val networkModule = module {
    single { createHttpClient() }
}
