package com.example.myapp.core.ui.di

import com.example.myapp.core.ui.navigation.NavigationState
import com.example.myapp.core.ui.navigation.Navigator
import com.example.myapp.core.ui.navigation.NavEventHandler
import org.koin.dsl.module

val uiModule = module {
    single { NavigationState() }
    factory<Navigator> { get<NavigationState>() }
    factory<NavEventHandler> { get<NavigationState>() }
}
