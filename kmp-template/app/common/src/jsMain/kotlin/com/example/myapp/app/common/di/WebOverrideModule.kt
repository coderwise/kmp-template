package com.example.myapp.app.common.di

import com.example.myapp.core.domain.repository.HomeRepository
import com.example.myapp.feature.home.data.repository.InMemoryHomeRepository
import org.koin.dsl.module

// SQLDelight's web worker driver requires complex WASM/webpack setup.
// For web, use an in-memory repository instead — loaded AFTER appModule so it wins.
val webOverrideModule = module {
    single<HomeRepository> { InMemoryHomeRepository() }
}
