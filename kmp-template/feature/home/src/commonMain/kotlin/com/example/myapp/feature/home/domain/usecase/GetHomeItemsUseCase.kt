package com.example.myapp.feature.home.domain.usecase

import com.coderwise.libs.arch.FlowResultUseCase
import com.example.myapp.core.domain.model.HomeItem
import com.example.myapp.core.domain.repository.HomeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class GetHomeItemsUseCase(
    private val repository: HomeRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) : FlowResultUseCase<Result<List<HomeItem>>>(dispatcher) {
    override fun execute(): Flow<Result<List<HomeItem>>> = repository.getHomeItems()
}
