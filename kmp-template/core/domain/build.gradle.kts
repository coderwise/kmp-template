plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kmp.android.library)
    alias(libs.plugins.kotlin.serialization)
}

val appVersionName: String = property("app.versionName") as String

kotlin {
    android {
        namespace = "com.example.myapp.core.domain"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()
    }
    iosArm64()
    iosSimulatorArm64()
    js(IR) { browser() }
    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
            implementation(libs.navigation3.runtime)
        }
    }
}

// Generate version info for each platform
val generateAndroidVersionSource by tasks.registering {
    val outputDir = layout.buildDirectory.dir("generated/version/androidMain/kotlin")
    outputs.dir(outputDir)
    doLast {
        val dir = outputDir.get().asFile.resolve("com/example/myapp/core/domain")
        dir.mkdirs()
        dir.resolve("GeneratedVersion.kt").writeText(
            """
            |package com.example.myapp.core.domain
            |
            |internal const val GENERATED_VERSION_NAME = "$appVersionName"
            |internal const val GENERATED_VERSION_CODE = ${getGitCommitCount()}
            """.trimMargin()
        )
    }
}

val generateDesktopVersionResource by tasks.registering {
    val outputDir = layout.buildDirectory.dir("generated/version/desktopMain/resources")
    outputs.dir(outputDir)
    doLast {
        val dir = outputDir.get().asFile
        dir.mkdirs()
        dir.resolve("version.properties").writeText("versionName=$appVersionName\nversionCode=${getGitCommitCount()}\n")
    }
}

val generateJsVersionSource by tasks.registering {
    val outputDir = layout.buildDirectory.dir("generated/version/jsMain/kotlin")
    outputs.dir(outputDir)
    doLast {
        val dir = outputDir.get().asFile.resolve("com/example/myapp/core/domain")
        dir.mkdirs()
        dir.resolve("GeneratedVersion.kt").writeText(
            """
            |package com.example.myapp.core.domain
            |
            |internal const val GENERATED_VERSION_NAME = "$appVersionName"
            |internal const val GENERATED_VERSION_CODE = ${getGitCommitCount()}
            """.trimMargin()
        )
    }
}

kotlin.sourceSets.getByName("androidMain").kotlin.srcDir(generateAndroidVersionSource.map { it.outputs.files.singleFile })
kotlin.sourceSets.getByName("desktopMain").resources.srcDir(generateDesktopVersionResource.map { it.outputs.files.singleFile })
kotlin.sourceSets.getByName("jsMain").kotlin.srcDir(generateJsVersionSource.map { it.outputs.files.singleFile })

fun getGitCommitCount(): Int {
    return try {
        val process = ProcessBuilder("git", "rev-list", "--count", "HEAD").start()
        val count = process.inputStream.bufferedReader().readText().trim().toInt()
        process.waitFor()
        count
    } catch (e: Exception) {
        1
    }
}
