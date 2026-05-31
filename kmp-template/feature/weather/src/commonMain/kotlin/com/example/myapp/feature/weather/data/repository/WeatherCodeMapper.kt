package com.example.myapp.feature.weather.data.repository

private const val CLEAR_SKY = 0
private const val MAINLY_CLEAR = 1
private const val PARTLY_CLOUDY = 2
private const val OVERCAST = 3
private const val FOG = 45
private const val DEPOSITING_RIME_FOG = 48
private const val DRIZZLE_LIGHT = 51
private const val DRIZZLE_MODERATE = 53
private const val DRIZZLE_DENSE = 55
private const val FREEZING_DRIZZLE_LIGHT = 56
private const val FREEZING_DRIZZLE_DENSE = 57
private const val RAIN_SLIGHT = 61
private const val RAIN_MODERATE = 63
private const val RAIN_HEAVY = 65
private const val FREEZING_RAIN_LIGHT = 66
private const val FREEZING_RAIN_HEAVY = 67
private const val SNOW_FALL_SLIGHT = 71
private const val SNOW_FALL_MODERATE = 73
private const val SNOW_FALL_HEAVY = 75
private const val SNOW_GRAINS = 77
private const val RAIN_SHOWERS_SLIGHT = 80
private const val RAIN_SHOWERS_MODERATE = 81
private const val RAIN_SHOWERS_VIOLENT = 82
private const val SNOW_SHOWERS_SLIGHT = 85
private const val SNOW_SHOWERS_HEAVY = 86
private const val THUNDERSTORM_SLIGHT = 95
private const val THUNDERSTORM_WITH_HAIL_SLIGHT = 96
private const val THUNDERSTORM_WITH_HAIL_HEAVY = 99

private val weatherCodeMap = mapOf(
    CLEAR_SKY to "Clear sky",
    MAINLY_CLEAR to "Partly cloudy",
    PARTLY_CLOUDY to "Partly cloudy",
    OVERCAST to "Partly cloudy",
    FOG to "Fog",
    DEPOSITING_RIME_FOG to "Fog",
    DRIZZLE_LIGHT to "Drizzle",
    DRIZZLE_MODERATE to "Drizzle",
    DRIZZLE_DENSE to "Drizzle",
    FREEZING_DRIZZLE_LIGHT to "Freezing drizzle",
    FREEZING_DRIZZLE_DENSE to "Freezing drizzle",
    RAIN_SLIGHT to "Rain",
    RAIN_MODERATE to "Rain",
    RAIN_HEAVY to "Rain",
    FREEZING_RAIN_LIGHT to "Freezing rain",
    FREEZING_RAIN_HEAVY to "Freezing rain",
    SNOW_FALL_SLIGHT to "Snow",
    SNOW_FALL_MODERATE to "Snow",
    SNOW_FALL_HEAVY to "Snow",
    SNOW_GRAINS to "Snow grains",
    RAIN_SHOWERS_SLIGHT to "Rain showers",
    RAIN_SHOWERS_MODERATE to "Rain showers",
    RAIN_SHOWERS_VIOLENT to "Rain showers",
    SNOW_SHOWERS_SLIGHT to "Snow showers",
    SNOW_SHOWERS_HEAVY to "Snow showers",
    THUNDERSTORM_SLIGHT to "Thunderstorm",
    THUNDERSTORM_WITH_HAIL_SLIGHT to "Thunderstorm with hail",
    THUNDERSTORM_WITH_HAIL_HEAVY to "Thunderstorm with hail"
)

/**
 * Maps a WMO weather interpretation code (as returned by Open-Meteo's
 * `weathercode` field) to a human-readable condition.
 *
 * See https://open-meteo.com/en/docs for the code table.
 */
internal fun weatherCodeToCondition(code: Int): String = weatherCodeMap[code] ?: "Unknown"
