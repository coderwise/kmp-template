plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kmp.android.library)
}

apply(from = "$rootDir/gradle/git-utils.gradle.kts")
@Suppress("UNCHECKED_CAST")
val getGitVersionName = extra["getGitVersionName"] as () -> String
@Suppress("UNCHECKED_CAST")
val getGitCommitCount = extra["getGitCommitCount"] as () -> Int

val appVersionName: String = getGitVersionName()

kotlin {
    android {
        namespace = "com.example.myapp.libs.version"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()
    }
    iosArm64(); iosSimulatorArm64()
    js {
        browser()
        nodejs()
    }
    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
        }
    }
}

// Generate version info for each platform
val generateAndroidVersionSource = tasks.register("generateAndroidVersionSource") {
    val outputDir = layout.buildDirectory.dir("generated/version/androidMain/kotlin")
    outputs.dir(outputDir)
    doLast {
        val dir = outputDir.get().asFile.resolve("com.example.myapp.libs.version")
        dir.mkdirs()
        dir.resolve("GeneratedVersion.kt").writeText(
            """
            |package com.example.myapp.libs.version
            |
            |internal const val GENERATED_VERSION_NAME = "$appVersionName"
            |internal const val GENERATED_VERSION_CODE = ${getGitCommitCount()}
            """.trimMargin()
        )
    }
}

val generateDesktopVersionResource = tasks.register("generateDesktopVersionResource") {
    val outputDir = layout.buildDirectory.dir("generated/version/desktopMain/resources")
    outputs.dir(outputDir)
    doLast {
        val dir = outputDir.get().asFile
        dir.mkdirs()
        dir.resolve("version.properties").writeText("versionName=$appVersionName\nversionCode=${getGitCommitCount()}\n")
    }
}

val generateJsVersionSource = tasks.register("generateJsVersionSource") {
    val outputDir = layout.buildDirectory.dir("generated/version/jsMain/kotlin")
    outputs.dir(outputDir)
    doLast {
        val dir = outputDir.get().asFile.resolve("com.example.myapp.libs.version")
        dir.mkdirs()
        dir.resolve("GeneratedVersion.kt").writeText(
            """
            |package com.example.myapp.libs.version
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
