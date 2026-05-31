package com.example.myapp.feature.weather.data.repository

/**
 * Maps a WMO weather interpretation code (as returned by Open-Meteo's
 * `weathercode` field) to a human-readable condition.
 *
 * See https://open-meteo.com/en/docs for the code table.
 */
internal fun weatherCodeToCondition(code: Int): String = when (code) {
    0 -> "Clear sky"
    1, 2, 3 -> "Partly cloudy"
    45, 48 -> "Fog"
    51, 53, 55 -> "Drizzle"
    56, 57 -> "Freezing drizzle"
    61, 63, 65 -> "Rain"
    66, 67 -> "Freezing rain"
    71, 73, 75 -> "Snow"
    77 -> "Snow grains"
    80, 81, 82 -> "Rain showers"
    85, 86 -> "Snow showers"
    95 -> "Thunderstorm"
    96, 99 -> "Thunderstorm with hail"
    else -> "Unknown"
}
