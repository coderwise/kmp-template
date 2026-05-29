package com.example.myapp.feature.home.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface HomeNavDestination : NavKey {
    @Serializable
    data object Root : HomeNavDestination

    @Serializable
    data class AddItem(val title: String = "", val description: String = "") : HomeNavDestination
}