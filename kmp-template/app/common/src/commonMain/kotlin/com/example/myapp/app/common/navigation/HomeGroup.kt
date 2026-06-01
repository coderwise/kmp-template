package com.example.myapp.app.common.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.myapp.feature.home.navigation.HomeNavigation
import com.example.myapp.feature.settings.ui.SettingsScreen
import com.example.myapp.feature.weather.ui.WeatherScreen

@Composable
fun HomeGroup(
    viewModel: AppViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeGroupContent(
        selectedTab = uiState.selectedTab
    )
}

@Composable
private fun HomeGroupContent(
    selectedTab: Int
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (selectedTab) {
            0 -> HomeNavigation()
            1 -> WeatherScreen()
            2 -> SettingsScreen()
        }
    }
}
