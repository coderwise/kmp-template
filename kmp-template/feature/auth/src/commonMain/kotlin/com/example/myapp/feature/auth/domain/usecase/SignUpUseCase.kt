package com.example.myapp.feature.auth.domain.usecase

import com.example.myapp.core.domain.arch.SuspendUseCase
import com.example.myapp.feature.auth.domain.repository.AuthRepository

class SignUpUseCase(
    private val repository: AuthRepository
) : SuspendUseCase<SignUpUseCase.Params, Result<Unit>>() {

    data class Params(val email: String, val password: String)

    suspend operator fun invoke(email: String, password: String) = invoke(Params(email, password))

    override suspend fun execute(params: Params): Result<Unit> =
        repository.signUp(params.email, params.password)
}
