package com.example.myapp.feature.home.domain.usecase

import com.example.myapp.core.domain.model.HomeItem
import com.example.myapp.core.domain.repository.HomeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface UpdateHomeItemUseCase {
    suspend operator fun invoke(item: HomeItem)
}

class UpdateHomeItemUseCaseImpl(
    private val repository: HomeRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : UpdateHomeItemUseCase {
    override suspend operator fun invoke(item: HomeItem) = withContext(dispatcher) {
        repository.updateHomeItem(item)
    }
}
