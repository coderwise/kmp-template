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
    }
}

// Build the unreleased com.coderwise.libs modules from a local libs.mobile checkout instead of
// Maven Central. Set `localLibs=../../libs.mobile` in local.properties (read by Android Studio
// sync too), or pass -PlocalLibs=<path> on the command line. Only the modules listed are
// substituted; every other com.coderwise.libs dependency still resolves from Maven Central.
val localLibs: String? = providers.gradleProperty("localLibs").orNull
    ?: file("local.properties").takeIf { it.exists() }?.let { local ->
        java.util.Properties().apply { local.inputStream().use { load(it) } }.getProperty("localLibs")
    }
localLibs?.let { path ->
    includeBuild(path) {
        dependencySubstitution {
            listOf("arch", "uistate", "navigation").forEach { name ->
                substitute(module("com.coderwise.libs:$name")).using(project(":$name"))
            }
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
