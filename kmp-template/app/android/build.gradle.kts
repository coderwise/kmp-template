plugins {
    alias(libs.plugins.android.application)
    // NO kotlin.android here — AGP 9+ has built-in Kotlin support
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.myapp.android"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.myapp.android"
        minSdk = libs.versions.minSdk.get().toInt()
        versionCode = getGitCommitCount()
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":app:common"))
    implementation(libs.androidx.activity.compose)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
}

fun getGitCommitCount(): Int {
    return try {
        val process = ProcessBuilder("git", "rev-list", "--count", "HEAD")
            .start()
        val count = process.inputStream.bufferedReader().readText().trim().toInt()
        process.waitFor()
        count
    } catch (e: Exception) {
        1
    }
}
