package com.example.myapp.feature.auth.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.example.myapp.core.ui.navigation.rememberChildNavigator
import com.example.myapp.core.ui.util.verticalSlideTransition
import com.example.myapp.feature.auth.ui.login.LoginScreen
import com.example.myapp.feature.auth.ui.signup.SignUpScreen

@Composable
fun AuthNavigation() {
    val navigator = rememberChildNavigator(AuthNavDestination.Login) { event ->
        when (event) {
            is AuthNavEvent.ToSignUp -> navigate(AuthNavDestination.SignUp)
        }
    }

    NavDisplay(
        backStack = navigator.currentBackStack,
        onBack = { navigator.navigateUp() },
        sceneStrategies = listOf(SinglePaneSceneStrategy()),
        entryProvider = entryProvider {
            entry<AuthNavDestination.Login> {
                LoginScreen()
            }
            entry<AuthNavDestination.SignUp>(
                metadata = verticalSlideTransition()
            ) {
                SignUpScreen()
            }
        }
    )
}
