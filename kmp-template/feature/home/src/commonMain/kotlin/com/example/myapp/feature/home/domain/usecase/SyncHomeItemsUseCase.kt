package com.example.myapp.feature.home.domain.usecase

import com.example.myapp.core.domain.arch.SuspendResultUseCase
import com.example.myapp.core.domain.repository.HomeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class SyncHomeItemsUseCase(
    private val repository: HomeRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) : SuspendResultUseCase<Result<Unit>>(dispatcher) {
    override suspend fun execute(): Result<Unit> = repository.syncHomeItems()
}
