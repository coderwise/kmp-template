package com.example.myapp.feature.auth.domain.usecase

import com.example.myapp.feature.auth.domain.repository.AuthRepository

class SignUpUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> =
        repository.signUp(email, password)
}
