package com.example.myapp.core.domain.repository

import com.example.myapp.core.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationPreferencesRepository {
    fun getSelectedLocation(): Flow<Location?>
    suspend fun saveSelectedLocation(location: Location)
}
