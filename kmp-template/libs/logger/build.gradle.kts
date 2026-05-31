plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kmp.android.library)
}

kotlin {
    android {
        namespace = "com.example.myapp.libs.logger"
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
            implementation(libs.kermit)
        }
    }
}
