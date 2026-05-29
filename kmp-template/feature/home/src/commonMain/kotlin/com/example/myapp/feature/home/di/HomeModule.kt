package com.example.myapp.feature.home.di

import com.example.myapp.feature.home.domain.usecase.*
import com.example.myapp.feature.home.ui.HomeViewModel
import com.example.myapp.feature.home.ui.edit.HomeItemEditViewModel
import com.example.myapp.libs.version.appVersion
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    factory<GetHomeItemsUseCase> { GetHomeItemsUseCaseImpl(get()) }
    factory<AddHomeItemUseCase> { AddHomeItemUseCaseImpl(get()) }
    factory<RemoveHomeItemUseCase> { RemoveHomeItemUseCaseImpl(get()) }
    factory<UpdateHomeItemUseCase> { UpdateHomeItemUseCaseImpl(get()) }
    factory<SyncHomeItemsUseCase> { SyncHomeItemsUseCaseImpl(get()) }
    viewModel { HomeViewModel(get(), get(), get(), get(), get(), appVersion) }
    viewModel { params -> HomeItemEditViewModel(params.get(), params.get(), params.get(), get(), get()) }
}
