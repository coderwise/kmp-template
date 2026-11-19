@file:Suppress("UnstableApiUsage", "UnstableApiUsage")

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
        mavenCentral() // Coderwise shared libraries (e.g. com.coderwise.libs:utils) live here
        // Unreleased com.coderwise.libs modules, published from libs.mobile with
        // `./gradlew -PlibVersion=<version> :<module>:publishToMavenLocal`. Only these modules
        // come from Maven local; drop this once they are on Maven Central.
        exclusiveContent {
            forRepository { mavenLocal() }
            filter { includeModuleByRegex("com\\.coderwise\\.libs", "(arch|uistate|navigation)(-.+)?") }
        }
    }
}

// Automatically include core modules
file("core").listFiles()?.filter { it.isDirectory && File(it, "build.gradle.kts").exists() }?.forEach {
    include(":core:${it.name}")
}

// Automatically include libs modules
file("libs").listFiles()?.filter { it.isDirectory && File(it, "build.gradle.kts").exists() }?.forEach {
    include(":libs:${it.name}")
}

// Automatically include feature modules
file("feature").listFiles()?.filter { it.isDirectory && File(it, "build.gradle.kts").exists() }?.forEach {
    include(":feature:${it.name}")
}

// Automatically include app modules
file("app").listFiles()?.filter { it.isDirectory && File(it, "build.gradle.kts").exists() }?.forEach {
    include(":app:${it.name}")
}
