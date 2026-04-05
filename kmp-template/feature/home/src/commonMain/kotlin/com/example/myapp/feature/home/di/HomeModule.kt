package com.example.myapp.feature.home.di

import com.example.myapp.feature.home.data.repository.HomeRepositoryImpl
import com.example.myapp.feature.home.domain.repository.HomeRepository
import com.example.myapp.feature.home.domain.usecase.GetHomeItemsUseCase
import com.example.myapp.feature.home.ui.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    single<HomeRepository> { HomeRepositoryImpl() }
    factory { GetHomeItemsUseCase(get()) }
    viewModel { HomeViewModel(get()) }
}
