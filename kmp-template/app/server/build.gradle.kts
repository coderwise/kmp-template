plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    application
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:api"))
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.resources)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.koin.core)
    implementation(libs.koin.ktor)
    implementation(libs.logback.classic)
}

application { mainClass.set("com.example.myapp.server.ApplicationKt") }
