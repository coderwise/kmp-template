package com.example.myapp.core.domain

import platform.Foundation.NSBundle

actual val appVersion: String by lazy {
    val bundle = NSBundle.mainBundle
    val versionName = bundle.infoDictionary?.get("CFBundleShortVersionString") as? String ?: "0.0.0"
    val versionCode = bundle.infoDictionary?.get("CFBundleVersion") as? String ?: "0"
    "$versionName ($versionCode)"
}
