package com.example.myapp.feature.auth.domain.usecase

import com.example.myapp.core.domain.arch.SuspendResultUseCase
import com.example.myapp.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/** Development-only: authenticate without credentials (login debug bypass). */
class SignInAsDebugUserUseCase(
    private val repository: AuthRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) : SuspendResultUseCase<Result<Unit>>(dispatcher) {
    override suspend fun execute(): Result<Unit> = repository.signInAsDebugUser()
}
