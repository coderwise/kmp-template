package com.example.myapp.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath
import java.io.File

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class DataStoreProvider {
    actual fun createDataStore(): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                val dataDir = File(System.getProperty("user.home"), ".myapp")
                if (!dataDir.exists()) {
                    dataDir.mkdirs()
                }
                File(dataDir, DATASTORE_FILE_NAME).absolutePath.toPath()
            }
        )
    }
}
