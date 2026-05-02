package com.example.myapp.app.common.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

interface AppDestination : NavKey

@Serializable
data object HomeDestination : AppDestination

@Serializable
data object WeatherDestination : AppDestination

@Serializable
data object SettingsDestination : AppDestination

val appNavSerializersModule = SerializersModule {
    polymorphic(NavKey::class) {
        subclass(HomeDestination::class, HomeDestination.serializer())
        subclass(WeatherDestination::class, WeatherDestination.serializer())
        subclass(SettingsDestination::class, SettingsDestination.serializer())
    }
}
