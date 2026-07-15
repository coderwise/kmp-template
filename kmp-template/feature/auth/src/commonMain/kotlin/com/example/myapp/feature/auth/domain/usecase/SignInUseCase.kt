package com.example.myapp.feature.auth.domain.usecase

import com.example.myapp.core.domain.arch.SuspendUseCase
import com.example.myapp.feature.auth.domain.repository.AuthRepository

class SignInUseCase(
    private val repository: AuthRepository
) : SuspendUseCase<SignInUseCase.Params, Result<Unit>>() {

    data class Params(val email: String, val password: String)

    suspend operator fun invoke(email: String, password: String) = invoke(Params(email, password))

    override suspend fun execute(params: Params): Result<Unit> =
        repository.signIn(params.email, params.password)
}
