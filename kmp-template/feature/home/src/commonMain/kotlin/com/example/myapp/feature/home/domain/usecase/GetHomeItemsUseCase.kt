package com.example.myapp.feature.home.domain.usecase

import com.example.myapp.core.domain.model.Result
import com.example.myapp.core.domain.model.HomeItem
import com.example.myapp.core.domain.repository.HomeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

interface GetHomeItemsUseCase {
    operator fun invoke(): Flow<Result<List<HomeItem>>>
}

class GetHomeItemsUseCaseImpl(
    private val repository: HomeRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : GetHomeItemsUseCase {
    override operator fun invoke(): Flow<Result<List<HomeItem>>> = repository.getHomeItems().flowOn(dispatcher)
}
