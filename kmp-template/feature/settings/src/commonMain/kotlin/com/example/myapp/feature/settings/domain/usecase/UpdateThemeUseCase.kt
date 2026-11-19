package com.example.myapp.feature.settings.domain.usecase

import com.coderwise.libs.arch.SuspendUseCase
import com.example.myapp.core.domain.model.ThemeType
import com.example.myapp.core.domain.repository.SettingsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class UpdateThemeUseCase(
    private val repository: SettingsRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) : SuspendUseCase<ThemeType, Unit>(dispatcher) {
    override suspend fun execute(params: ThemeType) {
        repository.updateSettings { it.copy(theme = params) }
    }
}
