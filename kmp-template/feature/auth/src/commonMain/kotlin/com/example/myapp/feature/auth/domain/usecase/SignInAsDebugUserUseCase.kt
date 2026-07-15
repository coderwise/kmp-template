package com.example.myapp.feature.auth.domain.usecase

import com.example.myapp.core.domain.arch.SuspendResultUseCase
import com.example.myapp.feature.auth.domain.repository.AuthRepository

/** Development-only: authenticate without credentials (login debug bypass). */
class SignInAsDebugUserUseCase(
    private val repository: AuthRepository
) : SuspendResultUseCase<Result<Unit>>() {
    override suspend fun execute(): Result<Unit> = repository.signInAsDebugUser()
}
