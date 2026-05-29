package com.example.myapp.app.common.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.myapp.core.ui.layouts.AppBottomBar
import com.example.myapp.core.ui.layouts.BottomBarItem
import com.example.myapp.feature.home.navigation.HomeNavigation
import com.example.myapp.feature.settings.ui.SettingsScreen
import com.example.myapp.feature.weather.ui.WeatherScreen

private val homeGroupTabs = listOf(
    BottomBarItem(label = "Home", icon = Icons.Default.Home),
    BottomBarItem(label = "Weather", icon = Icons.Default.Cloud),
    BottomBarItem(label = "Settings", icon = Icons.Default.Settings),
)

@Composable
fun HomeGroup() {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            AppBottomBar(
                items = homeGroupTabs,
                selectedIndex = selectedTab,
                onItemSelected = { selectedTab = it },
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                0 -> HomeNavigation()
                1 -> WeatherScreen()
                2 -> SettingsScreen()
            }
        }
    }
}
