package com.example.myapp.feature.auth.domain.usecase

import com.example.myapp.core.domain.arch.FlowResultUseCase
import com.example.myapp.feature.auth.domain.model.AuthState
import com.example.myapp.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class ObserveAuthStateUseCase(
    private val repository: AuthRepository
) : FlowResultUseCase<AuthState>() {
    override fun execute(): Flow<AuthState> = repository.observeAuthState()
}
