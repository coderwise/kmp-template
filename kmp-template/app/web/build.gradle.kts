plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    js {
        browser {
            commonWebpackConfig {
                // The production bundle is large; generating its source map dominates the build.
                sourceMaps = false
            }
        }
        binaries.executable()
    }
    sourceSets {
        getByName("jsMain").dependencies {
            implementation(project(":app:common"))
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.koin.core)
        }
    }
}
