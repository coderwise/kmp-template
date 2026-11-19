package com.example.myapp.feature.home.navigation

import com.coderwise.libs.navigation.NavEvent
import com.example.myapp.core.domain.model.HomeItem

sealed interface HomeNavEvent : NavEvent {
    data object ToAddItem : HomeNavEvent
    data class ToEditItem(val item: HomeItem) : HomeNavEvent
}
