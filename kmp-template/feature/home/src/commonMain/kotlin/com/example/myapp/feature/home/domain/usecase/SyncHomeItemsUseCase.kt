package com.example.myapp.feature.home.domain.usecase

import com.example.myapp.feature.home.domain.repository.HomeRepository

class SyncHomeItemsUseCase(private val repository: HomeRepository) {
    suspend operator fun invoke() = repository.syncHomeItems()
}
