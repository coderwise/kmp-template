package com.example.myapp.server

import com.example.myapp.core.api.resources.Home
import com.example.myapp.core.api.resources.Weather
import com.example.myapp.core.api.model.HomeItemApi
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.resources.Resources
import io.ktor.server.resources.delete
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(CIO, port = 8080, module = Application::module).start(wait = true)
}

private val homeItems = mutableListOf(
    HomeItemApi("1", "Server Item 1", "Description from server"),
    HomeItemApi("2", "Server Item 2", "Another one from server")
)

fun Application.module() {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    install(CallLogging)
    install(Resources)
    routing {
        get("/health") {
            call.respond(mapOf("status" to "ok", "app" to "MyApp"))
        }

        get<Weather.City> { resource ->
            call.respond(mapOf(
                "city" to resource.city,
                "temperature" to 20,
                "condition" to "Cloudy"
            ))
        }

        get<Home.Items> {
            call.respond(homeItems)
        }

        post<Home.Items> {
            val item = call.receive<HomeItemApi>()
            homeItems.add(item)
            call.respond(item)
        }

        put<Home.Items.Id> { resource ->
            val item = call.receive<HomeItemApi>()
            val index = homeItems.indexOfFirst { it.id == resource.id }
            if (index != -1) {
                homeItems[index] = item
                call.respond(item)
            } else {
                call.respond(io.ktor.http.HttpStatusCode.NotFound)
            }
        }

        delete<Home.Items.Id> { resource ->
            val removed = homeItems.removeIf { it.id == resource.id }
            if (removed) {
                call.respond(io.ktor.http.HttpStatusCode.OK)
            } else {
                call.respond(io.ktor.http.HttpStatusCode.NotFound)
            }
        }
    }
}
