package com.example.myapp.core.data.repository

import com.example.myapp.core.api.model.HomeItemApi
import com.example.myapp.core.data.datasource.HomeLocalDataSource
import com.example.myapp.core.data.datasource.HomeRemoteDataSource
import com.example.myapp.core.database.HomeItemEntity
import com.example.myapp.core.domain.model.HomeItem
import com.example.myapp.core.domain.repository.HomeRepository
import com.example.myapp.libs.logger.AppLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Coordinates the remote and local data sources and maps between the
 * network/persistence models and the domain model.
 *
 * Writes are offline-first: the local store is updated even when the remote
 * call fails, but failures are logged rather than silently swallowed.
 */
class HomeRepositoryImpl(
    private val localDataSource: HomeLocalDataSource,
    private val remoteDataSource: HomeRemoteDataSource
) : HomeRepository {

    private companion object {
        const val TAG = "HomeRepository"
    }

    override fun getHomeItems(): Flow<Result<List<HomeItem>>> =
        localDataSource.observeItems().map { entities ->
            Result.success(entities.map { it.toDomain() })
        }

    override suspend fun syncHomeItems(): Result<Unit> = try {
        val items = remoteDataSource.fetchItems()
        localDataSource.replaceAll(items.map { it.toEntity() })
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun addHomeItem(item: HomeItem) {
        try {
            remoteDataSource.addItem(item.toApi())
        } catch (e: Exception) {
            AppLogger.warn(TAG, "Remote add failed for item ${item.id}; persisting locally only", e)
        }
        localDataSource.insert(item.toEntity())
    }

    override suspend fun removeHomeItem(id: String) {
        try {
            if (remoteDataSource.deleteItem(id)) {
                localDataSource.delete(id)
            }
        } catch (e: Exception) {
            // Offline fallback: remove locally now; a real app would queue the
            // deletion for a later retry against the server.
            AppLogger.warn(TAG, "Remote delete failed for item $id; deleting locally", e)
            localDataSource.delete(id)
        }
    }

    override suspend fun updateHomeItem(item: HomeItem) {
        try {
            remoteDataSource.updateItem(item.toApi())
        } catch (e: Exception) {
            AppLogger.warn(TAG, "Remote update failed for item ${item.id}; persisting locally only", e)
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
