package com.example.myapp.feature.settings.di

import com.example.myapp.feature.settings.domain.usecase.GetSettingsUseCase
import com.example.myapp.feature.settings.domain.usecase.UpdateThemeUseCase
import com.example.myapp.feature.settings.ui.SettingsViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureSettingsModule = module {
    factoryOf(::GetSettingsUseCase)
    factoryOf(::UpdateThemeUseCase)
    viewModelOf(::SettingsViewModel)
}
