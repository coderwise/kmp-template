package com.example.myapp.feature.home.domain.repository

import com.example.myapp.core.common.Result
import com.example.myapp.feature.home.domain.model.HomeItem
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getHomeItems(): Flow<Result<List<HomeItem>>>
}
