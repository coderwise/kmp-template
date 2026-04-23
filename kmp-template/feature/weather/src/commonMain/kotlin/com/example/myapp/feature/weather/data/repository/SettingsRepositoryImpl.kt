package com.example.myapp.feature.weather.data.repository

import com.example.myapp.feature.weather.domain.model.Location
import com.example.myapp.feature.weather.domain.repository.SettingsRepository
import com.example.myapp.libs.settings.SettingsDataStore
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(
    private val dataStore: SettingsDataStore<Location?>
) : SettingsRepository {
    override fun getSelectedLocation(): Flow<Location?> = dataStore.data

    override suspend fun saveSelectedLocation(location: Location) {
        dataStore.updateData { location }
    }
}
