package com.example.myapp.feature.auth.data.repository

import com.example.myapp.core.domain.model.Result
import com.example.myapp.feature.auth.domain.model.AuthState
import com.example.myapp.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * In-memory auth repository for template purposes.
 * Replace with a real implementation (e.g. Firebase, Ktor + JWT) as needed.
 */
class AuthRepositoryImpl : AuthRepository {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)

    override fun observeAuthState(): Flow<AuthState> = _authState

    override suspend fun signIn(email: String, password: String): Result<Unit> {
        if (email.isBlank() || password.isBlank()) {
            return Result.Error(
                exception = IllegalArgumentException("Email and password are required"),
                message = "Email and password are required",
            )
        }
        _authState.value = AuthState.Authenticated
        return Result.Success(Unit)
    }

    override suspend fun signUp(email: String, password: String): Result<Unit> {
        if (email.isBlank() || password.isBlank()) {
            return Result.Error(
                exception = IllegalArgumentException("Email and password are required"),
                message = "Email and password are required",
            )
        }
        _authState.value = AuthState.Authenticated
        return Result.Success(Unit)
    }

    override suspend fun signOut() {
        _authState.value = AuthState.Unauthenticated
    }
}
