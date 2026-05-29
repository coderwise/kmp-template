package com.example.myapp.feature.auth.domain.repository

import com.example.myapp.core.domain.model.Result
import com.example.myapp.feature.auth.domain.model.AuthState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun observeAuthState(): Flow<AuthState>
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signUp(email: String, password: String): Result<Unit>
    suspend fun signOut()
}
