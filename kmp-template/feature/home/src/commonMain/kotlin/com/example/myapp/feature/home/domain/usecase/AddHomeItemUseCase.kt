package com.example.myapp.feature.home.domain.usecase

import com.example.myapp.core.domain.arch.SuspendUseCase
import com.example.myapp.core.domain.model.HomeItem
import com.example.myapp.core.domain.repository.HomeRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AddHomeItemUseCase(
    private val repository: HomeRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) : SuspendUseCase<AddHomeItemUseCase.Params, Unit>(dispatcher) {

    data class Params(val title: String, val description: String)

    suspend operator fun invoke(title: String, description: String) = invoke(Params(title, description))

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun execute(params: Params) {
        val item = HomeItem(
            id = Uuid.random().toString(),
            title = params.title,
            description = params.description
        )
        repository.addHomeItem(item)
    }
}
