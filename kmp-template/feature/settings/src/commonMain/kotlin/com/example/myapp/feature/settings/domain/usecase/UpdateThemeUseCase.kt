package com.example.myapp.feature.settings.domain.usecase

import com.example.myapp.core.domain.arch.SuspendUseCase
import com.example.myapp.core.domain.model.ThemeType
import com.example.myapp.core.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.first

class UpdateThemeUseCase(
    private val repository: SettingsRepository
) : SuspendUseCase<ThemeType, Unit>() {
    override suspend fun execute(params: ThemeType) {
        val currentSettings = repository.observeSettings().first()
        repository.updateSettings(currentSettings.copy(theme = params))
    }
}
