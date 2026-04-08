package com.example.myapp.core.data.di

import com.example.myapp.core.data.repository.PreferencesRepositoryImpl
import com.example.myapp.core.domain.PreferencesRepository
import org.koin.dsl.module

val dataModule = module {
    single<PreferencesRepository> { PreferencesRepositoryImpl(get()) }
}
