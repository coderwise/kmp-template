package com.example.myapp.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun getPrefs(): Flow<String?>
    suspend fun setPrefs(value: String)
}