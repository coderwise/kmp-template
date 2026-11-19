package com.example.myapp.feature.auth.domain.usecase

import com.coderwise.libs.arch.SuspendResultUseCase
import com.example.myapp.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class SignOutUseCase(
    private val repository: AuthRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) : SuspendResultUseCase<Unit>(dispatcher) {
    override suspend fun execute() = repository.signOut()
}
