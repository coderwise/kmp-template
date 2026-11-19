package com.example.myapp.feature.auth.domain.usecase

import com.coderwise.libs.arch.SuspendUseCase
import com.example.myapp.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class SignInUseCase(
    private val repository: AuthRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) : SuspendUseCase<SignInUseCase.Params, Result<Unit>>(dispatcher) {

    data class Params(val email: String, val password: String)

    suspend operator fun invoke(email: String, password: String) = invoke(Params(email, password))

    override suspend fun execute(params: Params): Result<Unit> =
        repository.signIn(params.email, params.password)
}
