package com.example.myapp.feature.auth.domain.usecase

import com.example.myapp.feature.auth.domain.repository.AuthRepository

/** Development-only: authenticate without credentials (login debug bypass). */
class SignInAsDebugUserUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): Result<Unit> = repository.signInAsDebugUser()
}
