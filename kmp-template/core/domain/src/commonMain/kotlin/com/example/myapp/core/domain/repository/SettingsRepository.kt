package com.example.myapp.core.domain.repository

import com.example.myapp.core.domain.model.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun observeSettings(): Flow<Settings>
    /**
     * Replaces the settings with [transform] applied to the latest saved value, atomically, so
     * concurrent updates to different fields do not overwrite each other.
     */
    suspend fun updateSettings(transform: (Settings) -> Settings)
}
