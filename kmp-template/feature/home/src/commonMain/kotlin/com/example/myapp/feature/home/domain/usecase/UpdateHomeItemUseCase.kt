package com.example.myapp.feature.home.domain.usecase

import com.example.myapp.core.domain.model.HomeItem
import com.example.myapp.core.domain.repository.HomeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class UpdateHomeItemUseCase(
    private val repository: HomeRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend operator fun invoke(item: HomeItem) = withContext(dispatcher) {
        repository.updateHomeItem(item)
    }
}
