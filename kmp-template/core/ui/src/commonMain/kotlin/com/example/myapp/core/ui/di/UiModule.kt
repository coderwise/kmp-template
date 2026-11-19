package com.example.myapp.core.ui.di

import com.coderwise.libs.navigation.di.navigationModule
import org.koin.dsl.module

val uiModule = module {
    includes(navigationModule)
}
