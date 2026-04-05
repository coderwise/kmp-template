package com.example.myapp.feature.home.domain.usecase

import com.example.myapp.feature.home.domain.repository.HomeRepository

class RemoveHomeItemUseCase(private val repository: HomeRepository) {
    suspend operator fun invoke(id: String) = repository.removeHomeItem(id)
}
