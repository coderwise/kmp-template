package com.example.myapp.feature.home.ui

import com.example.myapp.core.domain.model.HomeItem
import com.example.myapp.core.ui.state.LoadableState

data class HomeUiState(
    val items: List<HomeItem> = emptyList(),
    override val isLoading: Boolean = false,
    override val isError: Boolean = false,
    override val errorMessage: String? = null,
    val isRefreshing: Boolean = false
) : LoadableState<HomeUiState> {
    override fun copyLoadState(isLoading: Boolean, isError: Boolean, errorMessage: String?) =
        copy(isLoading = isLoading, isError = isError, errorMessage = errorMessage)
}
