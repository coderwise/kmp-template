package com.example.myapp.app.common.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.myapp.feature.auth.navigation.AuthNavDestination
import com.example.myapp.feature.home.navigation.HomeNavDestination
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

/**
 * Every [NavKey] the app puts on a back stack, so the back stacks survive process death.
 * Register each new destination here: saving a back stack that holds an unregistered key fails.
 */
internal val navigationSavedStateConfiguration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(AuthDestination::class, AuthDestination.serializer())
            subclass(HomeGroupDestination::class, HomeGroupDestination.serializer())
            subclass(EditItemDestination::class, EditItemDestination.serializer())
            subclass(AuthNavDestination.Login::class, AuthNavDestination.Login.serializer())
            subclass(AuthNavDestination.SignUp::class, AuthNavDestination.SignUp.serializer())
            subclass(HomeNavDestination.Root::class, HomeNavDestination.Root.serializer())
            subclass(HomeNavDestination.AddItem::class, HomeNavDestination.AddItem.serializer())
        }
    }
}
