package com.example.myapp.core.data.repository

import com.example.myapp.core.domain.model.Settings
import com.example.myapp.core.domain.repository.SettingsRepository
import com.coderwise.libs.settings.SettingsDataStore
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(
    private val dataSource: SettingsDataStore<Settings>
) : SettingsRepository {

    override fun observeSettings(): Flow<Settings> = dataSource.data

    override suspend fun updateSettings(settings: Settings) {
        dataSource.updateData { settings }
    }
}
