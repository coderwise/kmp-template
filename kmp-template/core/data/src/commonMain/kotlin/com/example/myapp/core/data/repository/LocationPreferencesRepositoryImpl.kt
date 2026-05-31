package com.example.myapp.core.data.repository

import com.example.myapp.core.domain.model.Location
import com.example.myapp.core.domain.model.Settings
import com.example.myapp.core.domain.repository.LocationPreferencesRepository
import com.example.myapp.libs.settings.SettingsDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocationPreferencesRepositoryImpl(
    private val dataSource: SettingsDataStore<Settings>
) : LocationPreferencesRepository {

    override fun getSelectedLocation(): Flow<Location?> = dataSource.data.map { it.selectedLocation }

    override suspend fun saveSelectedLocation(location: Location) {
        dataSource.updateData { it.copy(selectedLocation = location) }
    }
}
