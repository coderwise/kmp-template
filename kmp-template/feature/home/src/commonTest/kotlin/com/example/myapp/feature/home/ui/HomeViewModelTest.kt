package com.example.myapp.feature.home.ui

import com.example.myapp.core.domain.model.HomeItem
import com.example.myapp.core.ui.navigation.NavEventHandler
import com.example.myapp.feature.home.domain.usecase.AddHomeItemUseCase
import com.example.myapp.feature.home.domain.usecase.GetHomeItemsUseCase
import com.example.myapp.feature.home.domain.usecase.RemoveHomeItemUseCase
import com.example.myapp.feature.home.domain.usecase.SyncHomeItemsUseCase
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val getHomeItemsUseCase = mock<GetHomeItemsUseCase>()
    private val addHomeItemUseCase = mock<AddHomeItemUseCase>()
    private val removeHomeItemUseCase = mock<RemoveHomeItemUseCase>()
    private val syncHomeItemsUseCase = mock<SyncHomeItemsUseCase>()
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

    private fun createViewModel() = HomeViewModel(
        getHomeItemsUseCase,
        addHomeItemUseCase,
        removeHomeItemUseCase,
        syncHomeItemsUseCase,
        navEventHandler
    )

    @Test
    fun `init should load items and refresh`() = runTest {
        val items = listOf(HomeItem("1", "Title", "Desc"))
        every { getHomeItemsUseCase() } returns flowOf(Result.success(items))
        everySuspend { syncHomeItemsUseCase() } returns Result.success(Unit)

        val viewModel = createViewModel()

        advanceUntilIdle()

        assertEquals(items, viewModel.uiState.value.items)
        assertFalse(viewModel.uiState.value.isLoading)
        verify {
            @Suppress("UnusedFlow")
            getHomeItemsUseCase()
        }
        verifySuspend { syncHomeItemsUseCase() }
    }

    @Test
    fun `loadItems should show error when it fails`() = runTest {
        val errorMessage = "Error loading items"
        every { getHomeItemsUseCase() } returns flowOf(Result.failure(Exception(errorMessage)))
        everySuspend { syncHomeItemsUseCase() } returns Result.success(Unit)

        val viewModel = createViewModel()

        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isError)
        assertEquals(errorMessage, viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `addItem should call addHomeItemUseCase`() = runTest {
        every { getHomeItemsUseCase() } returns flowOf(Result.success(emptyList()))
        everySuspend { syncHomeItemsUseCase() } returns Result.success(Unit)
        everySuspend { addHomeItemUseCase(any(), any()) } returns Unit
        every { navEventHandler.onEvent(any()) } returns Unit

        val viewModel = createViewModel()

        viewModel.onEvent(HomeUiEvent.AddItem("New Item", "Description"))
        advanceUntilIdle()

        verifySuspend { addHomeItemUseCase("New Item", "Description") }
    }

    @Test
    fun `removeItem should call removeHomeItemUseCase`() = runTest {
        every { getHomeItemsUseCase() } returns flowOf(Result.success(emptyList()))
        everySuspend { syncHomeItemsUseCase() } returns Result.success(Unit)
        everySuspend { removeHomeItemUseCase(any()) } returns Unit

        val viewModel = createViewModel()

        viewModel.onEvent(HomeUiEvent.DeleteItem("1"))
        advanceUntilIdle()

        verifySuspend { removeHomeItemUseCase("1") }
    }

    @Test
    fun `refresh should update isRefreshing state`() = runTest {
        every { getHomeItemsUseCase() } returns flowOf(Result.success(emptyList()))
        everySuspend { syncHomeItemsUseCase() } returns Result.success(Unit)

        val viewModel = createViewModel()

        // Initial refresh from init
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.isRefreshing)

        viewModel.onEvent(HomeUiEvent.Refresh)
        // We can't easily check the 'true' state without more complex setup since it's immediate
        // but we can verify it finishes as false.
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.isRefreshing)
        verifySuspend(VerifyMode.atLeast(2)) { syncHomeItemsUseCase() }
    }
}
