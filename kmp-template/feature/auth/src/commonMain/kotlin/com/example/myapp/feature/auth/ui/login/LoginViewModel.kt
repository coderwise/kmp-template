package com.example.myapp.feature.auth.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.core.domain.model.onError
import com.example.myapp.core.ui.navigation.NavEventHandler
import com.example.myapp.feature.auth.domain.usecase.SignInUseCase
import com.example.myapp.feature.auth.navigation.AuthNavEvent
import com.example.myapp.libs.version.isDebugBuild
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val signInUseCase: SignInUseCase,
    private val navEventHandler: NavEventHandler,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        LoginUiState(isBypassEnabled = isDebugBuild)
    )
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.EmailChanged -> _uiState.update { it.copy(email = event.email, error = null) }
            is LoginUiEvent.PasswordChanged -> _uiState.update { it.copy(password = event.password, error = null) }
            LoginUiEvent.Submit -> signIn(_uiState.value.email, _uiState.value.password)
            LoginUiEvent.NavigateToSignUp -> navEventHandler.onEvent(AuthNavEvent.ToSignUp)
            LoginUiEvent.DebugBypass -> debugBypass()
        }
    }

    private fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            // On success the auth-state flow flips isAuthenticated; AppNavigation
            // observes it and switches the root. No navigation event needed here.
            signInUseCase(email, password)
                .onError { _, message ->
                    _uiState.update { it.copy(isLoading = false, error = message) }
                }
        }
    }

    private fun debugBypass() {
        // Defense-in-depth: the button is hidden in release builds, but never
        // honor the event unless this really is a debug build.
        if (!isDebugBuild) return
        signIn(DEBUG_EMAIL, DEBUG_PASSWORD)
    }

    private companion object {
        const val DEBUG_EMAIL = "debug@bypass.local"
        const val DEBUG_PASSWORD = "debug"
    }
}
