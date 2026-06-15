plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kmp.android.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    android {
        namespace = "com.example.myapp.libs.permissions"
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
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
        }
        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.activity.compose)
            implementation(libs.lifecycle.runtime.compose)
        }
    }
}
