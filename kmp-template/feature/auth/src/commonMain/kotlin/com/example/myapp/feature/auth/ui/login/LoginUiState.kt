package com.example.myapp.feature.auth.ui.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isBypassEnabled: Boolean = false,
)
