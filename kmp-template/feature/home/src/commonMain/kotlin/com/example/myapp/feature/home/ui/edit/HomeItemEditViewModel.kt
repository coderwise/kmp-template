package com.example.myapp.feature.home.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.core.domain.model.HomeItem
import com.example.myapp.core.ui.navigation.GlobalNavEvent
import com.example.myapp.core.ui.navigation.NavEventHandler
import com.example.myapp.feature.home.domain.usecase.UpdateHomeItemUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeItemEditViewModel(
    private val itemId: String,
    initialTitle: String,
    initialDescription: String,
    private val updateHomeItemUseCase: UpdateHomeItemUseCase,
    private val navEventHandler: NavEventHandler,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        HomeItemEditUiState(title = initialTitle, description = initialDescription)
    )
    val uiState: StateFlow<HomeItemEditUiState> = _uiState.asStateFlow()

    fun onEvent(event: HomeItemEditUiEvent) {
        when (event) {
            is HomeItemEditUiEvent.TitleChanged -> _uiState.update { it.copy(title = event.title) }
            is HomeItemEditUiEvent.DescriptionChanged -> _uiState.update { it.copy(description = event.description) }
            HomeItemEditUiEvent.Save -> save()
            HomeItemEditUiEvent.NavigateBack -> navEventHandler.onEvent(GlobalNavEvent.Back)
        }
    }

    private fun save() {
        viewModelScope.launch {
            updateHomeItemUseCase(
                HomeItem(
                    id = itemId,
                    title = _uiState.value.title,
                    description = _uiState.value.description,
                )
            )
            navEventHandler.onEvent(GlobalNavEvent.Back)
        }
    }
}
