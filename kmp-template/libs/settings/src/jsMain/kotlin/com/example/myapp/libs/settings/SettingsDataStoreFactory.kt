package com.example.myapp.libs.settings

import kotlinx.serialization.KSerializer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class SettingsDataStoreFactory {
    actual fun <T> create(
        fileName: String,
        defaultValue: T,
        serializer: KSerializer<T>
    ): SettingsDataStore<T> {
        // Simple in-memory implementation for JS for now
        return object : SettingsDataStore<T> {
            private val state = MutableStateFlow(defaultValue)
            override val data: Flow<T> = state
            override suspend fun updateData(transform: suspend (T) -> T) {
                state.value = transform(state.value)
            }
        }
    }
}
