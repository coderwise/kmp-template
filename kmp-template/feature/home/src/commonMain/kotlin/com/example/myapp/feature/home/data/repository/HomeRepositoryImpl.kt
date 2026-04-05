package com.example.myapp.feature.home.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.myapp.core.domain.Result
import com.example.myapp.core.database.AppDatabase
import com.example.myapp.feature.home.domain.model.HomeItem
import com.example.myapp.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HomeRepositoryImpl(
    private val database: AppDatabase
) : HomeRepository {
    override fun getHomeItems(): Flow<Result<List<HomeItem>>> {
        return database.homeItemQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities ->
                Result.Success(
                    entities.map {
                        HomeItem(it.id, it.title, it.description)
                    }
                )
            }
    }

    override suspend fun addHomeItem(item: HomeItem) {
        database.homeItemQueries.insert(item.id, item.title, item.description)
    }

    override suspend fun removeHomeItem(id: String) {
        database.homeItemQueries.delete(id)
    }

    override suspend fun updateHomeItem(item: HomeItem) {
        database.homeItemQueries.update(item.id, item.title, item.description)
    }
}
