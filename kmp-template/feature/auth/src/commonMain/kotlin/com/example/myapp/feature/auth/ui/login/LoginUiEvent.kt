package com.example.myapp.feature.auth.ui.login

sealed interface LoginUiEvent {
    data class EmailChanged(val email: String) : LoginUiEvent
    data class PasswordChanged(val password: String) : LoginUiEvent
    data object Submit : LoginUiEvent
    data object NavigateToSignUp : LoginUiEvent
}
