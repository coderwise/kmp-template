package com.example.myapp.libs.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable

@Stable
interface LocationPermissionState {
    val status: PermissionStatus
    fun launchPermissionRequest()
}

@Composable
expect fun rememberLocationPermissionState(): LocationPermissionState
