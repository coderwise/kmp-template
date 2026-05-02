@file:JvmName("LocationModuleAndroid")
package com.example.myapp.libs.location.di

import com.example.myapp.libs.location.AndroidLocationProvider
import com.example.myapp.libs.location.LocationProvider
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformLocationModule: Module = module {
    single<LocationProvider> { AndroidLocationProvider(get()) }
}
