package com.example.myapp.libs.version

/**
 * jpackage (which Compose Desktop uses to build native distributions) sets
 * `jpackage.app-path` for the apps it launches. Its absence means we're running
 * from a dev launch (`./gradlew run` or the IDE). An explicit `-Dmyapp.debug`
 * overrides either way.
 */
actual val isDebugBuild: Boolean =
    System.getProperty("myapp.debug")?.toBooleanStrictOrNull()
        ?: (System.getProperty("jpackage.app-path") == null)
