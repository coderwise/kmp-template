plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kmp.android.library)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "com.example.myapp.libs.settings"
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
        applyDefaultHierarchyTemplate()
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
        }

        val nonJsMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation(libs.androidx.datastore.core)
                implementation(libs.androidx.datastore.core.okio)
                implementation(libs.okio)
            }
        }

        androidMain.get().dependsOn(nonJsMain)
        iosMain.get().dependsOn(nonJsMain)
        val desktopMain by getting {
            dependsOn(nonJsMain)
        }

        jsMain.dependencies {
        }
    }
}
