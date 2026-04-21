plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kmp.android.library)
    alias(libs.plugins.sqldelight)
}

kotlin {
    android {
        namespace = "com.example.myapp.libs.database"
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
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines)
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            implementation(libs.sqldelight.android)
            implementation(libs.koin.android)
        }
        iosMain.dependencies { implementation(libs.sqldelight.native) }
        jsMain.dependencies { }
        val desktopMain by getting { dependencies { implementation(libs.sqldelight.sqlite) } }
    }
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.example.myapp.libs.database")
        }
    }
}
