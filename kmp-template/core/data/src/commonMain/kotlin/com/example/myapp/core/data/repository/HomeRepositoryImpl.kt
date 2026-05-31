package com.example.myapp.core.data.repository

import com.example.myapp.core.api.model.HomeItemApi
import com.example.myapp.core.data.datasource.HomeLocalDataSource
import com.example.myapp.core.data.datasource.HomeRemoteDataSource
import com.example.myapp.core.database.HomeItemEntity
import com.example.myapp.core.domain.model.HomeItem
import com.example.myapp.core.domain.model.Result
import com.example.myapp.core.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Coordinates the remote and local data sources and maps between the
 * network/persistence models and the domain model.
 */
class HomeRepositoryImpl(
    private val localDataSource: HomeLocalDataSource,
    private val remoteDataSource: HomeRemoteDataSource
) : HomeRepository {

    override fun getHomeItems(): Flow<Result<List<HomeItem>>> =
        localDataSource.observeItems().map { entities ->
            Result.Success(entities.map { it.toDomain() })
        }

    override suspend fun syncHomeItems(): Result<Unit> = try {
        val items = remoteDataSource.fetchItems()
        localDataSource.replaceAll(items.map { it.toEntity() })
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e)
    }

    override suspend fun addHomeItem(item: HomeItem) {
        try {
            remoteDataSource.addItem(item.toApi())
        } catch (e: Exception) {
            // Handle or log error
        }
        localDataSource.insert(item.toEntity())
    }

    override suspend fun removeHomeItem(id: String) {
        try {
            if (remoteDataSource.deleteItem(id)) {
                localDataSource.delete(id)
            }
        } catch (e: Exception) {
            // In a real app, you might want to mark for deletion later
            localDataSource.delete(id)
        }
    }

    override suspend fun updateHomeItem(item: HomeItem) {
        try {
            remoteDataSource.updateItem(item.toApi())
        } catch (e: Exception) {
            // Handle error
        }
        localDataSource.update(item.toEntity())
    }
}

private fun HomeItem.toApi() = HomeItemApi(
    id = id,
    title = title,
    description = description
)

private fun HomeItem.toEntity() = HomeItemEntity(
    id = id,
    title = title,
    description = description
)

private fun HomeItemApi.toEntity() = HomeItemEntity(
    id = id,
    title = title,
    description = description
)

private fun HomeItemEntity.toDomain() = HomeItem(
    id = id,
    title = title,
    description = description
)
