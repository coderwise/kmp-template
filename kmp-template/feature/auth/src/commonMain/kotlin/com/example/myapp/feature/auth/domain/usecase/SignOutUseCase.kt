package com.example.myapp.feature.auth.domain.usecase

import com.example.myapp.core.domain.arch.SuspendResultUseCase
import com.example.myapp.feature.auth.domain.repository.AuthRepository

class SignOutUseCase(
    private val repository: AuthRepository
) : SuspendResultUseCase<Unit>() {
    override suspend fun execute() = repository.signOut()
}
