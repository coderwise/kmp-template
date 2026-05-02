package com.example.myapp.feature.settings.domain.usecase

import com.example.myapp.core.domain.model.Settings
import com.example.myapp.core.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetSettingsUseCase(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<Settings> = repository.observeSettings()
}
