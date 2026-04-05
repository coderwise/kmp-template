package com.example.myapp.feature.home.di

import com.example.myapp.feature.home.data.repository.HomeRepositoryImpl
import com.example.myapp.feature.home.domain.repository.HomeRepository
import com.example.myapp.feature.home.domain.usecase.AddHomeItemUseCase
import com.example.myapp.feature.home.domain.usecase.GetHomeItemsUseCase
import com.example.myapp.feature.home.domain.usecase.RemoveHomeItemUseCase
import com.example.myapp.feature.home.domain.usecase.SyncHomeItemsUseCase
import com.example.myapp.feature.home.domain.usecase.UpdateHomeItemUseCase
import com.example.myapp.feature.home.ui.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    single<HomeRepository> { HomeRepositoryImpl(get(), get()) }
    factory { GetHomeItemsUseCase(get()) }
    factory { AddHomeItemUseCase(get()) }
    factory { RemoveHomeItemUseCase(get()) }
    factory { UpdateHomeItemUseCase(get()) }
    factory { SyncHomeItemsUseCase(get()) }
    viewModel { HomeViewModel(get(), get(), get(), get(), get()) }
}
