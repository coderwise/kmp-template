package com.example.myapp.feature.auth.domain.usecase

import com.example.myapp.feature.auth.domain.model.AuthState
import com.example.myapp.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class ObserveAuthStateUseCase(private val repository: AuthRepository) {
    operator fun invoke(): Flow<AuthState> = repository.observeAuthState()
}
