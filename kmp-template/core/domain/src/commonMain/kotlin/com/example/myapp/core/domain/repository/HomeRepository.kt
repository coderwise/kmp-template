package com.example.myapp.core.domain.repository

import com.example.myapp.core.domain.Result
import com.example.myapp.core.domain.model.HomeItem
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getHomeItems(): Flow<Result<List<HomeItem>>>
    suspend fun syncHomeItems(): Result<Unit>
    suspend fun addHomeItem(item: HomeItem)
    suspend fun removeHomeItem(id: String)
    suspend fun updateHomeItem(item: HomeItem)
}
