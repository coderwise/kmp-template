package com.example.myapp.feature.home.domain.usecase

import com.example.myapp.core.domain.model.HomeItem
import com.example.myapp.core.domain.repository.HomeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface AddHomeItemUseCase {
    suspend operator fun invoke(item: HomeItem)
}

class AddHomeItemUseCaseImpl(
    private val repository: HomeRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : AddHomeItemUseCase {
    override suspend operator fun invoke(item: HomeItem) = withContext(dispatcher) {
        repository.addHomeItem(item)
    }
}
