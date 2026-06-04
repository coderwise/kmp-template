plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kmp.android.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "com.example.myapp.app.common"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()
        androidResources.enable = true
    }
    listOf(iosArm64(), iosSimulatorArm64()).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
            linkerOpts("-lsqlite3")
        }
    }
    js { browser() }
    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:domain"))
            implementation(project(":core:ui"))
            implementation(project(":core:data"))
            implementation(project(":feature:auth"))
            implementation(project(":feature:home"))
            implementation(project(":feature:weather"))
            implementation(project(":feature:settings"))
            implementation(project(":libs:settings"))
            implementation(project(":libs:location"))
            implementation(project(":libs:utils"))

            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.material3.adaptive)
            implementation(libs.compose.material3.adaptive.layout)
            implementation(libs.compose.material3.adaptive.navigation)
            implementation(libs.compose.material3.adaptive.navigation.suite)
            implementation(libs.compose.icons.extended)
            implementation(libs.compose.components.resources)

            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.navigation3.runtime)
            implementation(libs.navigation3.ui)
            implementation(libs.lifecycle.viewmodel.navigation3)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}

compose.resources {
    packageOfResClass = "com.example.myapp.app.common"
    publicResClass = true
}
