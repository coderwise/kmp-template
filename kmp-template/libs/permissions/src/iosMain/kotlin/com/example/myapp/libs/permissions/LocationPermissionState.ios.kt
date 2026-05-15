package com.example.myapp.libs.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberLocationPermissionState(): LocationPermissionState {
    val manager = remember { CLLocationManager() }
    val statusState = remember { mutableStateOf(currentStatus()) }

    val onResultState = remember { mutableStateOf<((Boolean) -> Unit)?>(null) }

    DisposableEffect(manager) {
        val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
            override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
                val current = currentStatus()
                statusState.value = current
                onResultState.value?.invoke(current.isGranted)
                onResultState.value = null
            }

            override fun locationManager(
                manager: CLLocationManager,
                didChangeAuthorizationStatus: CLAuthorizationStatus
            ) {
                val current = currentStatus()
                statusState.value = current
                onResultState.value?.invoke(current.isGranted)
                onResultState.value = null
            }
        }
        manager.delegate = delegate
        onDispose { manager.delegate = null }
    }

    return remember(manager) {
        object : LocationPermissionState {
            override val status: PermissionStatus
                get() = statusState.value

            override fun launchPermissionRequest(onResult: (Boolean) -> Unit) {
                onResultState.value = onResult
                manager.requestWhenInUseAuthorization()
            }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun currentStatus(): PermissionStatus =
    when (CLLocationManager.authorizationStatus()) {
        kCLAuthorizationStatusAuthorizedAlways,
        kCLAuthorizationStatusAuthorizedWhenInUse -> PermissionStatus.Granted
        kCLAuthorizationStatusNotDetermined -> PermissionStatus.Denied(shouldShowRationale = false)
        else -> PermissionStatus.Denied(shouldShowRationale = false)
    }
