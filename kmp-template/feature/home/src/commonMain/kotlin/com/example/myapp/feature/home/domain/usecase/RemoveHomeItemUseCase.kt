package com.example.myapp.feature.home.domain.usecase

import com.coderwise.libs.arch.SuspendUseCase
import com.example.myapp.core.domain.repository.HomeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class RemoveHomeItemUseCase(
    private val repository: HomeRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) : SuspendUseCase<String, Unit>(dispatcher) {
    override suspend fun execute(params: String) = repository.removeHomeItem(params)
}
