package com.example.myapp.feature.home.data.repository

import com.example.myapp.core.domain.model.HomeItem
import com.example.myapp.core.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * In-memory [HomeRepository] used on the web target, where the SQLDelight web
 * worker driver needs extra WASM/webpack setup.
 *
 * TODO(production): data here is not persisted and resets on reload. Wire up the
 * SQLDelight web driver (or another web-capable store) before relying on web
 * persistence; until then web behavior intentionally diverges from other targets.
 */
class InMemoryHomeRepository : HomeRepository {
    private val items = MutableStateFlow(
        listOf(
            HomeItem("1", "Welcome to MyApp (Web)", "In-memory data for web platform"),
            HomeItem("2", "Getting Started", "SQLDelight web driver requires additional setup")
        )
    )

    override fun getHomeItems(): Flow<Result<List<HomeItem>>> =
        items.map { Result.success(it) }

    override suspend fun syncHomeItems(): Result<Unit> = Result.success(Unit)

    override suspend fun addHomeItem(item: HomeItem) {
        items.value += item
    }

    override suspend fun removeHomeItem(id: String) {
        items.value = items.value.filter { it.id != id }
    }

    override suspend fun updateHomeItem(item: HomeItem) {
        items.value = items.value.map { if (it.id == item.id) item else it }
    }
}
