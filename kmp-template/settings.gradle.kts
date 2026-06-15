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
        mavenCentral()
        // Coderwise shared libraries (e.g. com.coderwise.libs:utils).
        // Resolves from the local Maven cache during development; the published
        // home is GitHub Packages (needs a read:packages token).
        mavenLocal()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/coderwise/maps-mobile")
            credentials {
                username = providers.gradleProperty("gpr.user").orNull ?: System.getenv("GITHUB_ACTOR")
                password = providers.gradleProperty("gpr.key").orNull ?: System.getenv("GITHUB_TOKEN")
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
