package com.example.myapp.core.domain

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

interface AppDestination : NavKey

@Serializable
data object HomeDestination : AppDestination

@Serializable
data object WeatherDestination : AppDestination

val appNavSerializersModule = SerializersModule {
    polymorphic(NavKey::class) {
        subclass(HomeDestination::class)
        subclass(WeatherDestination::class)
    }
}
