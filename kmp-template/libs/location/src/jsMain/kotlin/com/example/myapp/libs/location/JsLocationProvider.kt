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
                    cont.resume(LocationResult.Success(position.toGpsLocation()))
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
                trySend(LocationResult.Success(position.toGpsLocation()))
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

private fun dynamic.toGpsLocation(): GpsLocation {
    val coords = this.coords
    val rawHeading = coords.heading
    val rawAltitude = coords.altitude
    val rawSpeed = coords.speed
    return GpsLocation(
        latitude = (coords.latitude as Number).toDouble(),
        longitude = (coords.longitude as Number).toDouble(),
        bearing = if (rawHeading != null && rawHeading != undefined) {
            val f = (rawHeading as Number).toFloat()
            if (f.isNaN()) null else f
        } else null,
        elevation = if (rawAltitude != null && rawAltitude != undefined) {
            (rawAltitude as Number).toDouble()
        } else null,
        time = (this.timestamp as Number).toLong(),
        accuracy = (coords.accuracy as Number).toFloat(),
        speed = if (rawSpeed != null && rawSpeed != undefined) {
            val f = (rawSpeed as Number).toFloat()
            if (f.isNaN()) null else f
        } else null
    )
}
