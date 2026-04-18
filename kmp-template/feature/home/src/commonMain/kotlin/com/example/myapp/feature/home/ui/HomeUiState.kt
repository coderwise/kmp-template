package com.example.myapp.feature.home.ui

import com.example.myapp.core.domain.model.HomeItem

data class HomeUiState(
    val items: List<HomeItem> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val isRefreshing: Boolean = false,
    val appVersion: String = ""
)
