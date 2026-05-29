package com.example.myapp.feature.auth.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.core.domain.model.onError
import com.example.myapp.core.ui.navigation.GlobalNavEvent
import com.example.myapp.core.ui.navigation.NavEventHandler
import com.example.myapp.feature.auth.domain.usecase.SignUpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val navEventHandler: NavEventHandler,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    fun onEvent(event: SignUpUiEvent) {
        when (event) {
            is SignUpUiEvent.EmailChanged -> _uiState.update { it.copy(email = event.email, error = null) }
            is SignUpUiEvent.PasswordChanged -> _uiState.update { it.copy(password = event.password, error = null) }
            is SignUpUiEvent.ConfirmPasswordChanged -> _uiState.update { it.copy(confirmPassword = event.confirmPassword, error = null) }
            SignUpUiEvent.Submit -> signUp()
            SignUpUiEvent.NavigateBack -> navEventHandler.onEvent(GlobalNavEvent.Back)
        }
    }

    private fun signUp() {
        val state = _uiState.value
        if (state.password != state.confirmPassword) {
            _uiState.update { it.copy(error = "Passwords do not match") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            // On success the auth-state flow flips isAuthenticated; AppNavigation
            // observes it and switches the root. No navigation event needed here.
            signUpUseCase(state.email, state.password)
                .onError { _, message ->
                    _uiState.update { it.copy(isLoading = false, error = message) }
                }
        }
    }
}
