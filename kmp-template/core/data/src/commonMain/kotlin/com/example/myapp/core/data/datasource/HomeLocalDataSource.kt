package com.example.myapp.core.data.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.myapp.core.database.AppDatabase
import com.example.myapp.core.database.HomeItemEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Local database access for home items. Speaks the persistence model
 * ([HomeItemEntity]) only; mapping to/from the domain model is the
 * repository's responsibility.
 */
interface HomeLocalDataSource {
    fun observeItems(): Flow<List<HomeItemEntity>>
    suspend fun replaceAll(items: List<HomeItemEntity>)
    suspend fun insert(item: HomeItemEntity)
    suspend fun update(item: HomeItemEntity)
    suspend fun delete(id: String)
}

class HomeLocalDataSourceImpl(
    private val database: AppDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : HomeLocalDataSource {

    private val queries get() = database.homeItemQueries

    override fun observeItems(): Flow<List<HomeItemEntity>> =
        queries.selectAll().asFlow().mapToList(dispatcher)

    override suspend fun replaceAll(items: List<HomeItemEntity>) = withContext(dispatcher) {
        queries.transaction {
            queries.deleteAll()
            items.forEach { queries.insert(it.id, it.title, it.description) }
        }
    }

    override suspend fun insert(item: HomeItemEntity): Unit = withContext(dispatcher) {
        queries.insert(item.id, item.title, item.description)
    }

    override suspend fun update(item: HomeItemEntity): Unit = withContext(dispatcher) {
        queries.update(item.id, item.title, item.description)
    }

    override suspend fun delete(id: String): Unit = withContext(dispatcher) {
        queries.delete(id)
    }
}
