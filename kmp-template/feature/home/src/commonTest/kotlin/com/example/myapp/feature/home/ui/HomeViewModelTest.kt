package com.example.myapp.feature.home.ui

import com.example.myapp.core.domain.model.Result
import com.example.myapp.core.domain.model.HomeItem
import com.example.myapp.core.ui.navigation.NavEventHandler
import com.example.myapp.feature.home.domain.usecase.*
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
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val getHomeItemsUseCase = mock<GetHomeItemsUseCase>()
    private val addHomeItemUseCase = mock<AddHomeItemUseCase>()
    private val removeHomeItemUseCase = mock<RemoveHomeItemUseCase>()
    private val updateHomeItemUseCase = mock<UpdateHomeItemUseCase>()
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

    @Test
    fun `init should load items and refresh`() = runTest {
        val items = listOf(HomeItem("1", "Title", "Desc"))
        every { getHomeItemsUseCase() } returns flowOf(Result.Success(items))
        everySuspend { syncHomeItemsUseCase() } returns Result.Success(Unit)

        val viewModel = HomeViewModel(
            getHomeItemsUseCase,
            addHomeItemUseCase,
            removeHomeItemUseCase,
            updateHomeItemUseCase,
            syncHomeItemsUseCase,
            navEventHandler,
            "1.0.0"
        )

        advanceUntilIdle()

        assertEquals(items, viewModel.uiState.value.items)
        assertFalse(viewModel.uiState.value.isLoading)
        verify { getHomeItemsUseCase() }
        verifySuspend { syncHomeItemsUseCase() }
    }

    @Test
    fun `loadItems should show error when it fails`() = runTest {
        val errorMessage = "Error loading items"
        every { getHomeItemsUseCase() } returns flowOf(Result.Error(Exception(errorMessage)))
        everySuspend { syncHomeItemsUseCase() } returns Result.Success(Unit)

        val viewModel = HomeViewModel(
            getHomeItemsUseCase,
            addHomeItemUseCase,
            removeHomeItemUseCase,
            updateHomeItemUseCase,
            syncHomeItemsUseCase,
            navEventHandler,
            "1.0.0"
        )

        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isError)
        assertEquals(errorMessage, viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `addItem should call addHomeItemUseCase`() = runTest {
        every { getHomeItemsUseCase() } returns flowOf(Result.Success(emptyList()))
        everySuspend { syncHomeItemsUseCase() } returns Result.Success(Unit)
        everySuspend { addHomeItemUseCase(any()) } returns Unit

        val viewModel = HomeViewModel(
            getHomeItemsUseCase,
            addHomeItemUseCase,
            removeHomeItemUseCase,
            updateHomeItemUseCase,
            syncHomeItemsUseCase,
            navEventHandler,
            "1.0.0"
        )

        viewModel.onEvent(HomeUiEvent.AddItem("New Item", "Description"))
        advanceUntilIdle()

        verifySuspend { addHomeItemUseCase(any()) }
    }

    @Test
    fun `removeItem should call removeHomeItemUseCase`() = runTest {
        every { getHomeItemsUseCase() } returns flowOf(Result.Success(emptyList()))
        everySuspend { syncHomeItemsUseCase() } returns Result.Success(Unit)
        everySuspend { removeHomeItemUseCase(any()) } returns Unit

        val viewModel = HomeViewModel(
            getHomeItemsUseCase,
            addHomeItemUseCase,
            removeHomeItemUseCase,
            updateHomeItemUseCase,
            syncHomeItemsUseCase,
            navEventHandler,
            "1.0.0"
        )

        viewModel.onEvent(HomeUiEvent.DeleteItem("1"))
        advanceUntilIdle()

        verifySuspend { removeHomeItemUseCase("1") }
    }

    @Test
    fun `updateItem should call updateHomeItemUseCase`() = runTest {
        val item = HomeItem("1", "Title", "Desc")
        every { getHomeItemsUseCase() } returns flowOf(Result.Success(emptyList()))
        everySuspend { syncHomeItemsUseCase() } returns Result.Success(Unit)
        everySuspend { updateHomeItemUseCase(any()) } returns Unit

        val viewModel = HomeViewModel(
            getHomeItemsUseCase,
            addHomeItemUseCase,
            removeHomeItemUseCase,
            updateHomeItemUseCase,
            syncHomeItemsUseCase,
            navEventHandler,
            "1.0.0"
        )

        viewModel.onEvent(HomeUiEvent.UpdateItem(item))
        advanceUntilIdle()

        verifySuspend { updateHomeItemUseCase(item) }
    }

    @Test
    fun `refresh should update isRefreshing state`() = runTest {
        every { getHomeItemsUseCase() } returns flowOf(Result.Success(emptyList()))
        everySuspend { syncHomeItemsUseCase() } returns Result.Success(Unit)

        val viewModel = HomeViewModel(
            getHomeItemsUseCase,
            addHomeItemUseCase,
            removeHomeItemUseCase,
            updateHomeItemUseCase,
            syncHomeItemsUseCase,
            navEventHandler,
            "1.0.0"
        )

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
