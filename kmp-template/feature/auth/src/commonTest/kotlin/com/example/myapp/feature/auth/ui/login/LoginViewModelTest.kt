package com.example.myapp.feature.auth.ui.login

import com.example.myapp.core.ui.navigation.NavEventHandler
import com.example.myapp.feature.auth.domain.repository.AuthRepository
import com.example.myapp.feature.auth.domain.usecase.SignInAsDebugUserUseCase
import com.example.myapp.feature.auth.domain.usecase.SignInUseCase
import com.example.myapp.libs.version.isDebugBuild
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val authRepository = mock<AuthRepository>()
    private val navEventHandler = mock<NavEventHandler>()

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Real use cases over a mocked AuthRepository.
    private fun createViewModel() = LoginViewModel(
        SignInUseCase(authRepository),
        SignInAsDebugUserUseCase(authRepository),
        navEventHandler,
    )

    @Test
    fun `submit signs in with entered credentials`() = runTest {
        everySuspend { authRepository.signIn(any(), any()) } returns Result.success(Unit)

        val viewModel = createViewModel()
        viewModel.onEvent(LoginUiEvent.EmailChanged("user@example.com"))
        viewModel.onEvent(LoginUiEvent.PasswordChanged("secret"))
        viewModel.onEvent(LoginUiEvent.Submit)
        advanceUntilIdle()

        verifySuspend { authRepository.signIn("user@example.com", "secret") }
    }

    @Test
    fun `debug bypass authenticates without credentials`() = runTest {
        // On the desktop test target isDebugBuild is true, so the bypass runs.
        assertEquals(true, isDebugBuild)
        everySuspend { authRepository.signInAsDebugUser() } returns Result.success(Unit)

        val viewModel = createViewModel()
        assertEquals(true, viewModel.uiState.value.isBypassEnabled)

        viewModel.onEvent(LoginUiEvent.DebugBypass)
        advanceUntilIdle()

        verifySuspend { authRepository.signInAsDebugUser() }
    }
}
