package com.example.myapp.core.domain

import java.util.Properties

actual val appVersion: String by lazy {
    val props = Properties()
    val stream = Thread.currentThread().contextClassLoader.getResourceAsStream("version.properties")
    if (stream != null) {
        props.load(stream)
        val versionName = props.getProperty("versionName", "0.0.0")
        val versionCode = props.getProperty("versionCode", "0")
        "$versionName ($versionCode)"
    } else {
        "0.0.0"
    }
}
