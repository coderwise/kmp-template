package com.example.myapp.feature.home.domain.usecase

import com.example.myapp.core.domain.Result
import com.example.myapp.core.domain.model.HomeItem
import com.example.myapp.core.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

open class GetHomeItemsUseCase(private val repository: HomeRepository) {
    operator fun invoke(): Flow<Result<List<HomeItem>>> = repository.getHomeItems()
}
