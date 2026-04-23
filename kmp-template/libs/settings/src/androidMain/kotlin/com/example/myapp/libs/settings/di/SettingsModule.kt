@file:JvmName("SettingsModuleAndroid")
package com.example.myapp.libs.settings.di

import com.example.myapp.libs.settings.SettingsDataStoreFactory
import org.koin.dsl.module
import org.koin.core.module.Module

actual val platformSettingsModule: Module = module {
    single { SettingsDataStoreFactory(get()) }
}
