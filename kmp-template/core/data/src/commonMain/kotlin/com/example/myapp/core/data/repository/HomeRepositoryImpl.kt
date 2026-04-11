package com.example.myapp.core.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.myapp.core.api.Home
import com.example.myapp.core.api.model.HomeItemApi
import com.example.myapp.core.database.AppDatabase
import com.example.myapp.core.database.HomeItemEntity
import com.example.myapp.core.domain.Result
import com.example.myapp.core.domain.model.HomeItem
import com.example.myapp.core.domain.repository.HomeRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.delete
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.plugins.resources.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class HomeRepositoryImpl(
    private val database: AppDatabase,
    private val httpClient: HttpClient
) : HomeRepository {
    override fun getHomeItems(): Flow<Result<List<HomeItem>>> {
        return database.homeItemQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities ->
                Result.Success(
                    entities.map { it.toDomain() }
                )
            }
    }

    override suspend fun syncHomeItems(): Result<Unit> = withContext(Dispatchers.Default) {
        try {
            val items = httpClient.get(Home.Items()).body<List<HomeItemApi>>()
            database.homeItemQueries.transaction {
                database.homeItemQueries.deleteAll()
                items.forEach { item ->
                    database.homeItemQueries.insert(item.id, item.title, item.description)
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun addHomeItem(item: HomeItem) {
        withContext(Dispatchers.Default) {
            try {
                httpClient.post(Home.Items()) {
                    contentType(ContentType.Application.Json)
                    setBody(item.toApi())
                }
            } catch (e: Exception) {
                // Handle or log error
            }
            database.homeItemQueries.insert(item.id, item.title, item.description)
        }
    }

    override suspend fun removeHomeItem(id: String) {
        withContext(Dispatchers.Default) {
            try {
                val response = httpClient.delete(Home.Items.Id(id = id))
                if (response.status == HttpStatusCode.OK) {
                    database.homeItemQueries.delete(id)
                }
            } catch (e: Exception) {
                // In a real app, you might want to mark for deletion later
                database.homeItemQueries.delete(id)
            }
        }
    }

    override suspend fun updateHomeItem(item: HomeItem) {
        withContext(Dispatchers.Default) {
            try {
                httpClient.put(Home.Items.Id(id = item.id)) {
                    contentType(ContentType.Application.Json)
                    setBody(item.toApi())
                }
            } catch (e: Exception) {
                // Handle error
            }
            database.homeItemQueries.update(item.id, item.title, item.description)
        }
    }
}

private fun HomeItem.toApi() = HomeItemApi(
    id = id,
    title = title,
    description = description
)

private fun HomeItemApi.toDomain() = HomeItem(
    id = id,
    title = title,
    description = description
)

private fun HomeItemEntity.toDomain() = HomeItem(
    id = id,
    title = title,
    description = description
)
