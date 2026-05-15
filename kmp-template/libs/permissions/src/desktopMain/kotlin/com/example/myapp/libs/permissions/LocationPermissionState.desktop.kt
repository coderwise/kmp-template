package com.example.myapp.libs.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberLocationPermissionState(): LocationPermissionState = remember {
    object : LocationPermissionState {
        override val status: PermissionStatus = PermissionStatus.Granted
        override fun launchPermissionRequest(onResult: (Boolean) -> Unit) {
            onResult(true)
        }
    }
}
