package com.example.myapp.feature.home.navigation

import com.example.myapp.core.domain.model.HomeItem
import com.example.myapp.core.ui.navigation.Navigator

sealed interface HomeNavEvent {
    data object Back : HomeNavEvent
    data object ToWeather : HomeNavEvent
    data object ToSettings : HomeNavEvent
    data object ToAddItem : HomeNavEvent
    data class ToEditItem(val item: HomeItem) : HomeNavEvent
}
