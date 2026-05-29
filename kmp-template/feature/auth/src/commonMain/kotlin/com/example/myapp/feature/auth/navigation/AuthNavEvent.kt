package com.example.myapp.feature.auth.navigation

sealed interface AuthNavEvent {
    data object Authenticated : AuthNavEvent
    data object SignedOut : AuthNavEvent
    data object ToSignUp : AuthNavEvent
}
