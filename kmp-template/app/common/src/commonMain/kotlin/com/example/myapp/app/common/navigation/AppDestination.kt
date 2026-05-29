package com.example.myapp.app.common.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

interface AppDestination : NavKey

@Serializable
data object AuthDestination : AppDestination

@Serializable
data object HomeGroupDestination : AppDestination

@Serializable
data class EditItemDestination(
    val id: String,
    val title: String,
    val description: String,
) : AppDestination
