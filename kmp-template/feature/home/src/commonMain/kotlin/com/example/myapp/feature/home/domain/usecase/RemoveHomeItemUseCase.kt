package com.example.myapp.feature.home.domain.usecase

import com.example.myapp.core.domain.arch.SuspendUseCase
import com.example.myapp.core.domain.repository.HomeRepository

class RemoveHomeItemUseCase(
    private val repository: HomeRepository
) : SuspendUseCase<String, Unit>() {
    override suspend fun execute(params: String) = repository.removeHomeItem(params)
}
