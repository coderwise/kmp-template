package com.example.myapp.core.api.resources

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
@Resource("/api/home")
class Home {
    @Serializable
    @Resource("items")
    class Items(val parent: Home = Home()) {
        @Serializable
        @Resource("{id}")
        class Id(val parent: Items = Items(), val id: String)
    }
}
