plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kmp.android.library)
}

kotlin {
    android {
        namespace = "com.example.myapp.core.datastore"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()
    }
    iosArm64(); iosSimulatorArm64()
    js(IR) {
        browser()
        nodejs()
    }
    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:domain"))
            implementation(libs.koin.core)
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.okio)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}
