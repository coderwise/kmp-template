package com.example.myapp.feature.auth.domain.usecase

import com.example.myapp.core.domain.arch.FlowResultUseCase
import com.example.myapp.feature.auth.domain.model.AuthState
import com.example.myapp.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class ObserveAuthStateUseCase(
    private val repository: AuthRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) : FlowResultUseCase<AuthState>(dispatcher) {
    override fun execute(): Flow<AuthState> = repository.observeAuthState()
}
