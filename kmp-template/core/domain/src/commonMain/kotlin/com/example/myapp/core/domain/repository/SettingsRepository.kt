package com.example.myapp.core.domain.repository

import com.example.myapp.core.domain.model.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun observeSettings(): Flow<Settings>
    suspend fun updateSettings(settings: Settings)
}
