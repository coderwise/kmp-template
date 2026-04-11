package com.example.myapp.feature.home.domain.usecase

import com.example.myapp.core.domain.model.HomeItem
import com.example.myapp.core.domain.repository.HomeRepository

class UpdateHomeItemUseCase(private val repository: HomeRepository) {
    suspend operator fun invoke(item: HomeItem) = repository.updateHomeItem(item)
}
