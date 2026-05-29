package com.example.myapp.feature.auth.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AuthNavDestination : NavKey {
    @Serializable
    data object Login : AuthNavDestination

    @Serializable
    data object SignUp : AuthNavDestination
}
