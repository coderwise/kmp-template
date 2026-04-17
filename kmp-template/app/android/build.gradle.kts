import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    // NO kotlin.android here — AGP 9+ has built-in Kotlin support
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    //alias(libs.plugins.googleServices)
    alias(libs.plugins.crashlytics)
}

val keystorePropertiesFile: File = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
try {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
} catch (e: Exception) {
    println("Warning: Could not load keystore properties. Error: ${e.message}")
}

android {
    namespace = "com.example.myapp.android"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.myapp.android"
        minSdk = libs.versions.minSdk.get().toInt()
        versionCode = getGitCommitCount()
        versionName = property("app.versionName") as String
    }

    signingConfigs {
        create("release") {
            storeFile = file(System.getenv("SIGNING_STORE_FILE") ?: "upload.keystore")
            storePassword = System.getenv("SIGNING_STORE_PASSWORD")
                ?: keystoreProperties.getProperty("storePassword")
            keyPassword = System.getenv("SIGNING_STORE_PASSWORD")
                ?: keystoreProperties.getProperty("storePassword")
            keyAlias = System.getenv("SIGNING_KEY_ALIAS")
                ?: keystoreProperties.getProperty("keyAlias")
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
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

    // crashlytics
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
}

fun getGitCommitCount(): Int {
    return try {
        val process = ProcessBuilder("git", "rev-list", "--count", "HEAD")
            .start()
        val count = process.inputStream.bufferedReader().readText().trim().toInt()
        process.waitFor()
        count
    } catch (_: Exception) {
        1
    }
}
