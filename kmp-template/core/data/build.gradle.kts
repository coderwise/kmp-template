plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kmp.android.library)
}

kotlin {
    android {
        namespace = "com.example.myapp.core.data"
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
            implementation(project(":core:domain"))
            implementation(project(":core:api"))
            implementation(project(":libs:network"))
            implementation(project(":libs:database"))
            implementation(project(":libs:settings"))

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
            implementation(libs.sqldelight.coroutines)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.resources)
        }
    }
}
