package com.example.myapp.feature.home.data.repository

import com.example.myapp.core.common.Result
import com.example.myapp.feature.home.domain.model.HomeItem
import com.example.myapp.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeRepositoryImpl : HomeRepository {
    override fun getHomeItems(): Flow<Result<List<HomeItem>>> = flow {
        emit(Result.Loading)
        delay(500)
        emit(
            Result.Success(
                listOf(
                    HomeItem("1", "Welcome to MyApp", "This is your first item"),
                    HomeItem("2", "Getting Started", "Edit feature/home to customize this screen")
                )
            )
        )
    }
}
