import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kmp.android.library) apply false
    alias(libs.plugins.sqldelight) apply false
    alias(libs.plugins.mokkery) apply false
    alias(libs.plugins.googleServices) apply false
    alias(libs.plugins.crashlytics) apply false
    alias(libs.plugins.detekt) apply false
}

// Static analysis gate. Each module keeps its own detekt-baseline.xml so the
// gate passes on existing code; regenerate with `./gradlew detektBaseline`.
subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    extensions.configure<DetektExtension>("detekt") {
        buildUponDefaultConfig = true
        parallel = true
        source.setFrom(files("src"))
        baseline = file("detekt-baseline.xml")
    }

    tasks.withType<Detekt>().configureEach {
        reports {
            html.required.set(true)
            xml.required.set(false)
            sarif.required.set(false)
            txt.required.set(false)
            md.required.set(false)
        }
    }
}
