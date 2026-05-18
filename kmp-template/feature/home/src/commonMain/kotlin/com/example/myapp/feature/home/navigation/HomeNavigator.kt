package com.example.myapp.feature.home.navigation

import com.example.myapp.core.domain.model.HomeItem

sealed interface HomeNavEvent {
    data object ToWeather : HomeNavEvent
    data object ToSettings : HomeNavEvent
    data object ToAddItem : HomeNavEvent
    data class ToEditItem(val item: HomeItem) : HomeNavEvent
}
