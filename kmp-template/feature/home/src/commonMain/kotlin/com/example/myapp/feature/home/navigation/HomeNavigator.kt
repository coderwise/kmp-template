package com.example.myapp.feature.home.navigation

import com.example.myapp.core.domain.model.HomeItem

interface HomeNavigator {
    fun toWeather()
    fun toSettings()
    fun toAddItem()
    fun toEditItem(item: HomeItem)
    fun back()
}