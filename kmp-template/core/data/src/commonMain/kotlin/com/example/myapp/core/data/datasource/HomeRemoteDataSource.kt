package com.example.myapp.core.data.datasource

import com.example.myapp.core.api.model.HomeItemApi
import com.example.myapp.core.api.resources.Home
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

/**
 * Network access for home items. Speaks the API model ([HomeItemApi]) only;
 * mapping to/from the domain model is the repository's responsibility.
 */
interface HomeRemoteDataSource {
    suspend fun fetchItems(): List<HomeItemApi>
    suspend fun addItem(item: HomeItemApi)
    suspend fun updateItem(item: HomeItemApi)

    /** Returns true when the server confirmed the deletion. */
    suspend fun deleteItem(id: String): Boolean
}

class HomeRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : HomeRemoteDataSource {

    override suspend fun fetchItems(): List<HomeItemApi> =
        httpClient.get(Home.Items()).body()

    override suspend fun addItem(item: HomeItemApi) {
        httpClient.post(Home.Items()) {
            contentType(ContentType.Application.Json)
            setBody(item)
        }
    }

    override suspend fun updateItem(item: HomeItemApi) {
        httpClient.put(Home.Items.Id(id = item.id)) {
            contentType(ContentType.Application.Json)
            setBody(item)
        }
    }

    override suspend fun deleteItem(id: String): Boolean =
        httpClient.delete(Home.Items.Id(id = id)).status == HttpStatusCode.OK
}
