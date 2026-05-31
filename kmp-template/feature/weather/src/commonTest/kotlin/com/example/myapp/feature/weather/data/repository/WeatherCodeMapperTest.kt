package com.example.myapp.feature.weather.data.repository

import kotlin.test.Test
import kotlin.test.assertEquals

class WeatherCodeMapperTest {

    @Test
    fun `maps representative WMO codes`() {
        assertEquals("Clear sky", weatherCodeToCondition(0))
        assertEquals("Partly cloudy", weatherCodeToCondition(2))
        assertEquals("Fog", weatherCodeToCondition(45))
        assertEquals("Rain", weatherCodeToCondition(63))
        assertEquals("Snow", weatherCodeToCondition(75))
        assertEquals("Thunderstorm", weatherCodeToCondition(95))
    }

    @Test
    fun `unknown code falls back to Unknown`() {
        assertEquals("Unknown", weatherCodeToCondition(123))
        assertEquals("Unknown", weatherCodeToCondition(-1))
    }
}
