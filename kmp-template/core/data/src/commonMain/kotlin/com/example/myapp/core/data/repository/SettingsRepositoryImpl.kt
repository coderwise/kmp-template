package com.example.myapp.core.data.repository

import com.example.myapp.core.domain.model.Location
import com.example.myapp.core.domain.model.Settings
import com.example.myapp.core.domain.repository.SettingsRepository
import com.example.myapp.libs.settings.SettingsDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val dataSource: SettingsDataStore<Settings>
) : SettingsRepository {

    override fun observeSettings(): Flow<Settings> = dataSource.data

    override suspend fun updateSettings(settings: Settings) {
        dataSource.updateData { settings }
    }

    override fun getSelectedLocation(): Flow<Location?> = dataSource.data.map { it.selectedLocation }

    override suspend fun saveSelectedLocation(location: Location) {
        dataSource.updateData { it.copy(selectedLocation = location) }
    }
}
