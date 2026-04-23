package com.example.myapp.feature.weather.domain.repository

import com.example.myapp.feature.weather.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSelectedLocation(): Flow<Location?>
    suspend fun saveSelectedLocation(location: Location)
}
