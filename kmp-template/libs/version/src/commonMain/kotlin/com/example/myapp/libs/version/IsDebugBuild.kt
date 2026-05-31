package com.example.myapp.libs.version

/**
 * True only in development builds, false in release/production builds.
 *
 * Drives development-only affordances (e.g. the login debug bypass) so they
 * physically cannot appear in a shipped release. The signal is the most
 * reliable one each platform offers:
 *  - Native (iOS): the binary's debug flag.
 *  - Android: the app's `BuildConfig.DEBUG`, pushed in at startup.
 *  - Desktop (JVM): whether the app was launched from a packaged distribution.
 *  - Web (JS): the bundler's `NODE_ENV`.
 */
expect val isDebugBuild: Boolean
