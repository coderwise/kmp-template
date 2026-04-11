package com.example.myapp.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.core.domain.Result
import com.example.myapp.core.domain.model.HomeItem
import com.example.myapp.feature.home.domain.usecase.AddHomeItemUseCase
import com.example.myapp.feature.home.domain.usecase.GetHomeItemsUseCase
import com.example.myapp.feature.home.domain.usecase.RemoveHomeItemUseCase
import com.example.myapp.feature.home.domain.usecase.SyncHomeItemsUseCase
import com.example.myapp.feature.home.domain.usecase.UpdateHomeItemUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock

data class HomeUiState(
    val items: List<HomeItem> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val isRefreshing: Boolean = false
)

class HomeViewModel(
    private val getHomeItemsUseCase: GetHomeItemsUseCase,
    private val addHomeItemUseCase: AddHomeItemUseCase,
    private val removeHomeItemUseCase: RemoveHomeItemUseCase,
    private val updateHomeItemUseCase: UpdateHomeItemUseCase,
    private val syncHomeItemsUseCase: SyncHomeItemsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadItems()
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            syncHomeItemsUseCase()
            _uiState.update { it.copy(isRefreshing = false) }
        }
    }

    private fun loadItems() {
        viewModelScope.launch {
            getHomeItemsUseCase().collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is Result.Loading -> state.copy(isLoading = true, isError = false)
                        is Result.Success -> state.copy(isLoading = false, items = result.data, isError = false)
                        is Result.Error -> state.copy(
                            isLoading = false,
                            isError = true,
                            errorMessage = result.message ?: result.exception.message
                        )
                    }
                }
            }
        }
    }

    fun addItem(title: String, description: String) {
        viewModelScope.launch {
            val newItem = HomeItem(
                id = Clock.System.now().toEpochMilliseconds().toString(),
                title = title,
                description = description
            )
            addHomeItemUseCase(newItem)
        }
    }

    fun removeItem(id: String) {
        viewModelScope.launch {
            removeHomeItemUseCase(id)
        }
    }

    fun updateItem(item: HomeItem) {
        viewModelScope.launch {
            updateHomeItemUseCase(item)
        }
    }
}
