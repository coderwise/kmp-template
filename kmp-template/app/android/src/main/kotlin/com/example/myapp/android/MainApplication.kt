package com.example.myapp.android

import android.app.Application
import com.example.myapp.app.common.di.appModule
import com.example.myapp.libs.version.AndroidBuildInfo
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Surface the real build type to common code so debug-only affordances
        // (e.g. the login bypass) can never appear in a release build.
        AndroidBuildInfo.isDebug = BuildConfig.DEBUG
        startKoin {
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }
}
