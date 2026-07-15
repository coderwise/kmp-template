package com.example.myapp.feature.home.domain.usecase

import com.example.myapp.core.domain.arch.SuspendResultUseCase
import com.example.myapp.core.domain.repository.HomeRepository

class SyncHomeItemsUseCase(
    private val repository: HomeRepository
) : SuspendResultUseCase<Result<Unit>>() {
    override suspend fun execute(): Result<Unit> = repository.syncHomeItems()
}
