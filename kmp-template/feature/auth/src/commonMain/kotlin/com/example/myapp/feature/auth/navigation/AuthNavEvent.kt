package com.example.myapp.feature.auth.navigation

sealed interface AuthNavEvent {
    data object ToSignUp : AuthNavEvent
}
