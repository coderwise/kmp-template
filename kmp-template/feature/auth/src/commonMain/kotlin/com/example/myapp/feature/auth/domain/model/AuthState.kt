package com.example.myapp.feature.auth.domain.model

sealed interface AuthState {
    data object Authenticated : AuthState
    data object Unauthenticated : AuthState
}
