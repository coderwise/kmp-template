package com.example.myapp.core.common

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

interface AppDestination : NavKey

@Serializable
data object HomeDestination : AppDestination

val appNavSerializersModule = SerializersModule {
    polymorphic(NavKey::class) {
        subclass(HomeDestination::class)
    }
}
