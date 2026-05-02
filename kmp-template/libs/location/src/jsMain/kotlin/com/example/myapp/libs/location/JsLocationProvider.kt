package com.example.myapp.libs.location

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class JsLocationProvider : LocationProvider {
    override suspend fun getCurrentLocation(): LocationResult<GpsLocation> =
        suspendCancellableCoroutine { cont ->
            val geolocation = js("navigator.geolocation")
            if (geolocation == null || geolocation == undefined) {
                cont.resume(
                    LocationResult.Error(
                        IllegalStateException("Geolocation unavailable"),
                        "Geolocation unavailable"
                    )
                )
                return@suspendCancellableCoroutine
            }
            val onSuccess: (dynamic) -> Unit = { position ->
                if (cont.isActive) {
                    val lat = (position.coords.latitude as Number).toDouble()
                    val lon = (position.coords.longitude as Number).toDouble()
                    val rawHeading = position.coords.heading
                    val bearing: Float? = if (rawHeading != null && rawHeading != undefined) {
                        val f = (rawHeading as Number).toFloat()
                        if (f.isNaN()) null else f
                    } else null
                    cont.resume(
                        LocationResult.Success(
                            GpsLocation(
                                latitude = lat,
                                longitude = lon,
                                bearing = bearing
                            )
                        )
                    )
                }
            }
            val onError: (dynamic) -> Unit = { error ->
                if (cont.isActive) {
                    val message = error.message as? String
                    cont.resume(
                        LocationResult.Error(RuntimeException(message ?: "Geolocation error"), message)
                    )
                }
            }
            geolocation.getCurrentPosition(onSuccess, onError)
        }

    override fun locationUpdates(): Flow<LocationResult<GpsLocation>> {
        val geolocation = js("navigator.geolocation")
        if (geolocation == null || geolocation == undefined) {
            return flowOf(
                LocationResult.Error(
                    IllegalStateException("Geolocation unavailable"),
                    "Geolocation unavailable"
                )
            )
        }
        return callbackFlow {
            val onSuccess: (dynamic) -> Unit = { position ->
                val lat = (position.coords.latitude as Number).toDouble()
                val lon = (position.coords.longitude as Number).toDouble()
                val rawHeading = position.coords.heading
                val bearing: Float? = if (rawHeading != null && rawHeading != undefined) {
                    val f = (rawHeading as Number).toFloat()
                    if (f.isNaN()) null else f
                } else null
                trySend(
                    LocationResult.Success(
                        GpsLocation(
                            latitude = lat,
                            longitude = lon,
                            bearing = bearing
                        )
                    )
                )
            }
            val onError: (dynamic) -> Unit = { error ->
                val message = error.message as? String
                trySend(
                    LocationResult.Error(RuntimeException(message ?: "Geolocation error"), message)
                )
            }
            val watchId = geolocation.watchPosition(onSuccess, onError)
            awaitClose { geolocation.clearWatch(watchId) }
        }
    }
}
