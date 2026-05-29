package com.example.myapp.feature.auth.domain.usecase

import com.example.myapp.feature.auth.domain.repository.AuthRepository

class SignOutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.signOut()
}
