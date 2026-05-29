package com.example.myapp.feature.auth.di

import com.example.myapp.feature.auth.data.repository.AuthRepositoryImpl
import com.example.myapp.feature.auth.domain.repository.AuthRepository
import com.example.myapp.feature.auth.domain.usecase.ObserveAuthStateUseCase
import com.example.myapp.feature.auth.domain.usecase.SignInUseCase
import com.example.myapp.feature.auth.domain.usecase.SignOutUseCase
import com.example.myapp.feature.auth.domain.usecase.SignUpUseCase
import com.example.myapp.feature.auth.ui.login.LoginViewModel
import com.example.myapp.feature.auth.ui.signup.SignUpViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authModule = module {
    single<AuthRepository> { AuthRepositoryImpl() }
    factory { ObserveAuthStateUseCase(get()) }
    factory { SignInUseCase(get()) }
    factory { SignUpUseCase(get()) }
    factory { SignOutUseCase(get()) }
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignUpViewModel)
}
