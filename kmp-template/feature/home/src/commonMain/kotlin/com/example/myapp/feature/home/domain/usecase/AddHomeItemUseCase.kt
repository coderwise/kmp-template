package com.example.myapp.feature.home.domain.usecase

import com.example.myapp.core.domain.model.HomeItem
import com.example.myapp.core.domain.repository.HomeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface AddHomeItemUseCase {
    suspend operator fun invoke(title: String, description: String)
}

class AddHomeItemUseCaseImpl(
    private val repository: HomeRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : AddHomeItemUseCase {
    @OptIn(ExperimentalUuidApi::class)
    override suspend operator fun invoke(title: String, description: String) = withContext(dispatcher) {
        val item = HomeItem(
            id = Uuid.random().toString(),
            title = title,
            description = description
        )
        repository.addHomeItem(item)
    }
}
