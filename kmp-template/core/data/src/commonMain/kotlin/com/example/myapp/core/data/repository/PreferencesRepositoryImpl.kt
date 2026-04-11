package com.example.myapp.core.data.repository

import com.example.myapp.core.data.local.PrefsDataSource
import com.example.myapp.core.domain.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesRepositoryImpl(
    private val dataSource: PrefsDataSource
) : PreferencesRepository {

    override fun getPrefs(): Flow<String?> {
        return dataSource.data.map { preferences ->
            preferences
        }
    }

    override suspend fun setPrefs(value: String) {
        dataSource.edit { preferences ->
            value
        }
    }
}
