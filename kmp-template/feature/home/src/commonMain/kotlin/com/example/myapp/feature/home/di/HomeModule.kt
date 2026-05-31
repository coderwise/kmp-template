package com.example.myapp.feature.home.di

import com.example.myapp.feature.home.domain.usecase.AddHomeItemUseCase
import com.example.myapp.feature.home.domain.usecase.AddHomeItemUseCaseImpl
import com.example.myapp.feature.home.domain.usecase.GetHomeItemsUseCase
import com.example.myapp.feature.home.domain.usecase.GetHomeItemsUseCaseImpl
import com.example.myapp.feature.home.domain.usecase.RemoveHomeItemUseCase
import com.example.myapp.feature.home.domain.usecase.RemoveHomeItemUseCaseImpl
import com.example.myapp.feature.home.domain.usecase.SyncHomeItemsUseCase
import com.example.myapp.feature.home.domain.usecase.SyncHomeItemsUseCaseImpl
import com.example.myapp.feature.home.domain.usecase.UpdateHomeItemUseCase
import com.example.myapp.feature.home.domain.usecase.UpdateHomeItemUseCaseImpl
import com.example.myapp.feature.home.ui.HomeViewModel
import com.example.myapp.feature.home.ui.edit.HomeItemEditViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    factory<GetHomeItemsUseCase> { GetHomeItemsUseCaseImpl(get()) }
    factory<AddHomeItemUseCase> { AddHomeItemUseCaseImpl(get()) }
    factory<RemoveHomeItemUseCase> { RemoveHomeItemUseCaseImpl(get()) }
    factory<UpdateHomeItemUseCase> { UpdateHomeItemUseCaseImpl(get()) }
    factory<SyncHomeItemsUseCase> { SyncHomeItemsUseCaseImpl(get()) }
    viewModel { HomeViewModel(get(), get(), get(), get(), get()) }
    viewModel { params -> HomeItemEditViewModel(params.get(), params.get(), params.get(), get(), get()) }
}
