package com.example.myapp.feature.home.domain.usecase

import com.example.myapp.core.domain.arch.FlowResultUseCase
import com.example.myapp.core.domain.model.HomeItem
import com.example.myapp.core.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

class GetHomeItemsUseCase(
    private val repository: HomeRepository
) : FlowResultUseCase<Result<List<HomeItem>>>() {
    override fun execute(): Flow<Result<List<HomeItem>>> = repository.getHomeItems()
}
