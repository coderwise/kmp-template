package com.example.myapp.feature.home.domain.usecase

import com.example.myapp.core.domain.repository.HomeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface SyncHomeItemsUseCase {
    suspend operator fun invoke(): Result<Unit>
}

class SyncHomeItemsUseCaseImpl(
    private val repository: HomeRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : SyncHomeItemsUseCase {
    override suspend operator fun invoke(): Result<Unit> = withContext(dispatcher) {
        repository.syncHomeItems()
    }
}
