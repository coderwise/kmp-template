package com.example.myapp.feature.home.ui

import com.example.myapp.core.domain.model.HomeItem

sealed interface HomeUiEvent {
    data object Refresh : HomeUiEvent
    data class AddItem(val title: String, val description: String) : HomeUiEvent
    data class DeleteItem(val id: String) : HomeUiEvent
    data object NavigateToAddItem : HomeUiEvent
    data class NavigateToEditItem(val item: HomeItem) : HomeUiEvent
}
