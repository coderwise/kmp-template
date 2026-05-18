package com.example.myapp.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.core.domain.model.HomeItem
import com.example.myapp.core.domain.model.onError
import com.example.myapp.core.domain.model.onLoading
import com.example.myapp.core.domain.model.onSuccess
import com.example.myapp.core.ui.navigation.NavEventHandler
import com.example.myapp.feature.home.domain.usecase.AddHomeItemUseCase
import com.example.myapp.feature.home.domain.usecase.GetHomeItemsUseCase
import com.example.myapp.feature.home.domain.usecase.RemoveHomeItemUseCase
import com.example.myapp.feature.home.domain.usecase.SyncHomeItemsUseCase
import com.example.myapp.feature.home.domain.usecase.UpdateHomeItemUseCase
import com.example.myapp.feature.home.navigation.HomeNavEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock

class HomeViewModel(
    private val getHomeItemsUseCase: GetHomeItemsUseCase,
    private val addHomeItemUseCase: AddHomeItemUseCase,
    private val removeHomeItemUseCase: RemoveHomeItemUseCase,
    private val updateHomeItemUseCase: UpdateHomeItemUseCase,
    private val syncHomeItemsUseCase: SyncHomeItemsUseCase,
    private val navEventHandler: NavEventHandler,
    appVersion: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(appVersion = appVersion))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadItems()
        onEvent(HomeUiEvent.Refresh)
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.Refresh -> refresh()
            is HomeUiEvent.AddItem -> addItem(event.title, event.description)
            is HomeUiEvent.DeleteItem -> removeItem(event.id)
            is HomeUiEvent.UpdateItem -> updateItem(event.item)
            is HomeUiEvent.NavigateToWeather -> navEventHandler.onEvent(HomeNavEvent.ToWeather)
            is HomeUiEvent.NavigateToSettings -> navEventHandler.onEvent(HomeNavEvent.ToSettings)
            is HomeUiEvent.NavigateToAddItem -> navEventHandler.onEvent(HomeNavEvent.ToAddItem)
            is HomeUiEvent.NavigateToEditItem -> navEventHandler.onEvent(HomeNavEvent.ToEditItem(event.item))
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            syncHomeItemsUseCase()
            _uiState.update { it.copy(isRefreshing = false) }
        }
    }

    private fun loadItems() {
        viewModelScope.launch {
            getHomeItemsUseCase().collect { result ->
                result.onLoading {
                    _uiState.update { it.loading() }
                }.onSuccess { data ->
                    _uiState.update { it.copy(isLoading = false, items = data, isError = false) }
                }.onError { exception, message ->
                    _uiState.update { it.error(message ?: exception.message) }
                }
            }
        }
    }

    private fun addItem(title: String, description: String) {
        viewModelScope.launch {
            val newItem = HomeItem(
                id = Clock.System.now().toEpochMilliseconds().toString(),
                title = title,
                description = description
            )
            addHomeItemUseCase(newItem)
            navEventHandler.onEvent(HomeNavEvent.Back)
        }
    }

    private fun removeItem(id: String) {
        viewModelScope.launch {
            removeHomeItemUseCase(id)
        }
    }

    private fun updateItem(item: HomeItem) {
        viewModelScope.launch {
            updateHomeItemUseCase(item)
            navEventHandler.onEvent(HomeNavEvent.Back)
        }
    }
}

private fun HomeUiState.loading() = copy(
    isLoading = true, isError = false
)

private fun HomeUiState.error(message: String?) = copy(
    isLoading = false, isError = true, errorMessage = message
)
