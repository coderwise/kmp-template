package com.example.myapp.feature.home.domain.usecase

import com.example.myapp.core.common.Result
import com.example.myapp.feature.home.domain.model.HomeItem
import com.example.myapp.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

class GetHomeItemsUseCase(private val repository: HomeRepository) {
    operator fun invoke(): Flow<Result<List<HomeItem>>> = repository.getHomeItems()
}
