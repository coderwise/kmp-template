package com.example.myapp.libs.settings

import kotlinx.browser.localStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class SettingsDataStoreFactory {
    actual fun <T> create(
        fileName: String,
        defaultValue: T,
        serializer: KSerializer<T>
    ): SettingsDataStore<T> {
        return WebSettingsDataStore(fileName, defaultValue, serializer)
    }
}

private class WebSettingsDataStore<T>(
    private val fileName: String,
    private val defaultValue: T,
    private val serializer: KSerializer<T>
): SettingsDataStore<T> {
    private val _data = MutableStateFlow(loadFromStorage())

    override val data: Flow<T> = _data.asStateFlow()

    override suspend fun updateData(transform: suspend (T) -> T) {
        val current = _data.value
        val updated = transform(current)
        _data.value = updated
        saveToStorage(updated)
    }

    private fun saveToStorage(value: T) {
        try {
            localStorage.setItem(fileName, Json.encodeToString(serializer, value))
        } catch (_: Exception) {
            // Ignore storage errors
        }
    }

    private fun loadFromStorage(): T {
        return try {
            val stored = localStorage.getItem(fileName)
            if (stored != null) {
                Json.decodeFromString(serializer, stored)
            } else {
                defaultValue
            }
        } catch (_: Exception) {
            defaultValue
        }
    }
}