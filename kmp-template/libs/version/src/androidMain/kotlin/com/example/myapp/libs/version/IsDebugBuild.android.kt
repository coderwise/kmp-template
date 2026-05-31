package com.example.myapp.libs.version

/**
 * This KMP library module has no `BuildConfig` of its own, so the Android app
 * pushes its `BuildConfig.DEBUG` in once at startup (see `MainApplication`).
 * Defaults to `false` (release) until set, so the value is never accidentally on.
 */
object AndroidBuildInfo {
    var isDebug: Boolean = false
}

actual val isDebugBuild: Boolean get() = AndroidBuildInfo.isDebug
