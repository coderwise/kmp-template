package com.example.myapp.feature.home.domain.usecase

import com.example.myapp.core.domain.arch.SuspendUseCase
import com.example.myapp.core.domain.model.HomeItem
import com.example.myapp.core.domain.repository.HomeRepository

class UpdateHomeItemUseCase(
    private val repository: HomeRepository
) : SuspendUseCase<HomeItem, Unit>() {
    override suspend fun execute(params: HomeItem) = repository.updateHomeItem(params)
}
