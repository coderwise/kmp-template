package com.example.myapp.feature.settings.domain.usecase

import com.coderwise.libs.arch.FlowResultUseCase
import com.example.myapp.core.domain.model.Settings
import com.example.myapp.core.domain.repository.SettingsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class GetSettingsUseCase(
    private val repository: SettingsRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) : FlowResultUseCase<Settings>(dispatcher) {
    override fun execute(): Flow<Settings> = repository.observeSettings()
}
