rootProject.name = "MyApp"

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

// App entry points
include(":app:android")
include(":app:desktop")
include(":app:web")
include(":app:server")
include(":app:common")

// Core modules
include(":core:common")
include(":core:network")
include(":core:ui")
include(":core:database")

// Feature modules
include(":feature:home")
