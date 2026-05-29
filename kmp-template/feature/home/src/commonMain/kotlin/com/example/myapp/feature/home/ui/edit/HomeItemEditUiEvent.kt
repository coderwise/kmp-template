package com.example.myapp.feature.home.ui.edit

sealed interface HomeItemEditUiEvent {
    data class TitleChanged(val title: String) : HomeItemEditUiEvent
    data class DescriptionChanged(val description: String) : HomeItemEditUiEvent
    data object Save : HomeItemEditUiEvent
    data object NavigateBack : HomeItemEditUiEvent
}
