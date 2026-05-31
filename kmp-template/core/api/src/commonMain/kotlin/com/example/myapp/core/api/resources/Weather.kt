package com.example.myapp.core.api.resources

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
@Resource("/api/weather")
class Weather {
    @Serializable
    @Resource("{city}")
    class City(val parent: Weather = Weather(), val city: String)
}
