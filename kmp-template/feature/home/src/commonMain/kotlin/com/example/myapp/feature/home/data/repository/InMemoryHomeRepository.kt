package com.example.myapp.feature.home.data.repository

import com.example.myapp.core.domain.Result
import com.example.myapp.feature.home.domain.model.HomeItem
import com.example.myapp.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class InMemoryHomeRepository : HomeRepository {
    private val items = MutableStateFlow(
        listOf(
            HomeItem("1", "Welcome to MyApp (Web)", "In-memory data for web platform"),
            HomeItem("2", "Getting Started", "SQLDelight web driver requires additional setup")
        )
    )

    override fun getHomeItems(): Flow<Result<List<HomeItem>>> =
        items.map { Result.Success(it) }

    override suspend fun addHomeItem(item: HomeItem) {
        items.value = items.value + item
    }

    override suspend fun removeHomeItem(id: String) {
        items.value = items.value.filter { it.id != id }
    }

    override suspend fun updateHomeItem(item: HomeItem) {
        items.value = items.value.map { if (it.id == item.id) item else it }
    }
}
