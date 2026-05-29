package com.example.myapp.feature.auth.ui.signup

sealed interface SignUpUiEvent {
    data class EmailChanged(val email: String) : SignUpUiEvent
    data class PasswordChanged(val password: String) : SignUpUiEvent
    data class ConfirmPasswordChanged(val confirmPassword: String) : SignUpUiEvent
    data object Submit : SignUpUiEvent
    data object NavigateBack : SignUpUiEvent
}
