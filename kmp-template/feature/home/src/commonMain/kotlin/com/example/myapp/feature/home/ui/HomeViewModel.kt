package com.example.myapp.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.core.common.Result
import com.example.myapp.feature.home.domain.model.HomeItem
import com.example.myapp.feature.home.domain.usecase.AddHomeItemUseCase
import com.example.myapp.feature.home.domain.usecase.GetHomeItemsUseCase
import com.example.myapp.feature.home.domain.usecase.RemoveHomeItemUseCase
import com.example.myapp.feature.home.domain.usecase.UpdateHomeItemUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getHomeItemsUseCase: GetHomeItemsUseCase,
    private val addHomeItemUseCase: AddHomeItemUseCase,
    private val removeHomeItemUseCase: RemoveHomeItemUseCase,
    private val updateHomeItemUseCase: UpdateHomeItemUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<Result<List<HomeItem>>>(Result.Loading)
    val state: StateFlow<Result<List<HomeItem>>> = _state.asStateFlow()

    init {
        loadItems()
    }

    private fun loadItems() {
        viewModelScope.launch {
            getHomeItemsUseCase().collect { _state.value = it }
        }
    }

    fun addItem(title: String, description: String) {
        viewModelScope.launch {
            val newItem = HomeItem(
                id = kotlin.time.Clock.System.now().toEpochMilliseconds().toString(),
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
