plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kmp.android.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.mokkery)
}

kotlin {
    android {
        namespace = "com.example.myapp.feature.home"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()
        androidResources.enable = true
    }
    iosArm64(); iosSimulatorArm64()
    js(IR) { browser() }
    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:domain"))
            implementation(project(":core:api"))
            implementation(project(":core:ui"))
            implementation(project(":core:network"))
            implementation(project(":core:database"))
            implementation(project(":core:version"))

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.lifecycle.viewmodel)
            implementation(libs.lifecycle.viewmodel.compose)
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.icons.extended)
            implementation(libs.navigation3.runtime)
            implementation(libs.compose.preview)
            implementation(libs.compose.components.resources)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

dependencies {
    "androidRuntimeClasspath"(libs.compose.tooling)
}
