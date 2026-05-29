package com.example.myapp.feature.auth.domain.usecase

import com.example.myapp.core.domain.model.Result
import com.example.myapp.feature.auth.domain.repository.AuthRepository

class SignInUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> =
        repository.signIn(email, password)
}
