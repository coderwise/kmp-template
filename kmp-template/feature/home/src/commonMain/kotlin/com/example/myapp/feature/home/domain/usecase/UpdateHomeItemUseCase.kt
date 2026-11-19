package com.example.myapp.feature.home.domain.usecase

import com.coderwise.libs.arch.SuspendUseCase
import com.example.myapp.core.domain.model.HomeItem
import com.example.myapp.core.domain.repository.HomeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class UpdateHomeItemUseCase(
    private val repository: HomeRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) : SuspendUseCase<HomeItem, Unit>(dispatcher) {
    override suspend fun execute(params: HomeItem) = repository.updateHomeItem(params)
}
