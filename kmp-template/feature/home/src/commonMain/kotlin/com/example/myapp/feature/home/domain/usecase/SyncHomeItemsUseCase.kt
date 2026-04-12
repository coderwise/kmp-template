package com.example.myapp.feature.home.domain.usecase

import com.example.myapp.core.domain.repository.HomeRepository

open class SyncHomeItemsUseCase(private val repository: HomeRepository) {
    suspend operator fun invoke() = repository.syncHomeItems()
}
