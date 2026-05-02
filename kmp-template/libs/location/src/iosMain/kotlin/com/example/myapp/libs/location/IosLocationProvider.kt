package com.example.myapp.libs.location

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.resume

@OptIn(ExperimentalForeignApi::class)
class IosLocationProvider : LocationProvider {

    override suspend fun getCurrentLocation(): LocationResult<GpsLocation> {
        return suspendCancellableCoroutine { cont ->
            val manager = CLLocationManager()
            val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
                override fun locationManager(
                    manager: CLLocationManager,
                    didUpdateLocations: List<*>
                ) {
                    manager.stopUpdatingLocation()
                    val loc = didUpdateLocations.lastOrNull() as? CLLocation
                    if (!cont.isActive) return
                    if (loc != null) {
                        val bearing = if (loc.course >= 0) loc.course.toFloat() else null
                        loc.coordinate.useContents {
                            cont.resume(
                                LocationResult.Success(
                                    GpsLocation(
                                        latitude = latitude,
                                        longitude = longitude,
                                        bearing = bearing
                                    )
                                )
                            )
                        }
                    } else {
                        cont.resume(LocationResult.Error(IllegalStateException("No location received")))
                    }
                }

                override fun locationManager(
                    manager: CLLocationManager,
                    didFailWithError: NSError
                ) {
                    manager.stopUpdatingLocation()
                    if (cont.isActive) {
                        cont.resume(
                            LocationResult.Error(
                                RuntimeException(didFailWithError.localizedDescription),
                                didFailWithError.localizedDescription
                            )
                        )
                    }
                }
            }
            manager.delegate = delegate
            manager.requestWhenInUseAuthorization()
            manager.startUpdatingLocation()
            cont.invokeOnCancellation {
                manager.stopUpdatingLocation()
                manager.delegate = null
            }
        }
    }

    override fun locationUpdates(): Flow<LocationResult<GpsLocation>> = callbackFlow {
        val manager = CLLocationManager()
        val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
            override fun locationManager(
                manager: CLLocationManager,
                didUpdateLocations: List<*>
            ) {
                val loc = didUpdateLocations.lastOrNull() as? CLLocation ?: return
                val bearing = if (loc.course >= 0) loc.course.toFloat() else null
                loc.coordinate.useContents {
                    trySend(
                        LocationResult.Success(
                            GpsLocation(
                                latitude = latitude,
                                longitude = longitude,
                                bearing = bearing
                            )
                        )
                    )
                }
            }

            override fun locationManager(
                manager: CLLocationManager,
                didFailWithError: NSError
            ) {
                trySend(
                    LocationResult.Error(
                        RuntimeException(didFailWithError.localizedDescription),
                        didFailWithError.localizedDescription
                    )
                )
            }
        }
        manager.delegate = delegate
        manager.requestWhenInUseAuthorization()
        manager.startUpdatingLocation()
        awaitClose {
            manager.stopUpdatingLocation()
            manager.delegate = null
        }
    }
}
