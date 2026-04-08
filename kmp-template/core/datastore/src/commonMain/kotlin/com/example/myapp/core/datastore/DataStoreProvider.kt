package com.example.myapp.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

expect class DataStoreProvider {
    fun createDataStore(): DataStore<Preferences>
}

internal const val DATASTORE_FILE_NAME = "app_preferences.preferences_pb"
