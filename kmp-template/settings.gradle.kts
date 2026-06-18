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
        mavenLocal() // TEMP: shared com.coderwise.libs:* served from ~/.m2 until libs-mobile publishes to Central
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
