package com.example.myapp.feature.auth.data.repository

import com.example.myapp.feature.auth.domain.model.AuthState
import com.example.myapp.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * In-memory auth repository for template purposes.
 *
 * TODO(production): This is a placeholder — it has no real authentication,
 * token storage, or persistence, and accepts ANY non-blank credentials. Replace
 * with a real implementation (e.g. Firebase Auth, Ktor + JWT) before shipping.
 * The login debug bypass relies on this accept-anything behavior, so revisit
 * [signIn] / the bypass together when you do.
 */
class AuthRepositoryImpl : AuthRepository {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)

    override fun observeAuthState(): Flow<AuthState> = _authState

    override suspend fun signIn(email: String, password: String): Result<Unit> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Email and password are required"))
        }
        _authState.value = AuthState.Authenticated
        return Result.success(Unit)
    }

    override suspend fun signUp(email: String, password: String): Result<Unit> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Email and password are required"))
        }
        _authState.value = AuthState.Authenticated
        return Result.success(Unit)
    }

    override suspend fun signOut() {
        _authState.value = AuthState.Unauthenticated
    }

    override suspend fun signInAsDebugUser(): Result<Unit> {
        // TODO(production): make this fail or remove it once a real auth backend
        // is in place. Callers already gate on isDebugBuild.
        _authState.value = AuthState.Authenticated
        return Result.success(Unit)
    }
}
