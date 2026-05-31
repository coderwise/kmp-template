package com.example.myapp.feature.auth.domain.repository

import com.example.myapp.feature.auth.domain.model.AuthState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun observeAuthState(): Flow<AuthState>
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signUp(email: String, password: String): Result<Unit>
    suspend fun signOut()

    /**
     * Development-only: authenticate without credentials. Kept separate from
     * [signIn] so the login debug bypass doesn't depend on credential validation
     * behavior. A production implementation should make this fail (or be removed).
     */
    suspend fun signInAsDebugUser(): Result<Unit>
}
