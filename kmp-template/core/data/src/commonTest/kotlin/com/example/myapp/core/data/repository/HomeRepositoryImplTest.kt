package com.example.myapp.core.data.repository

import com.example.myapp.core.data.datasource.HomeLocalDataSource
import com.example.myapp.core.data.datasource.HomeRemoteDataSource
import com.example.myapp.core.database.HomeItemEntity
import com.example.myapp.core.domain.model.HomeItem
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HomeRepositoryImplTest {

    private val local = mock<HomeLocalDataSource>()
    private val remote = mock<HomeRemoteDataSource>()
    private val repository = HomeRepositoryImpl(local, remote)

    @Test
    fun `getHomeItems maps entities to domain`() = runTest {
        every { local.observeItems() } returns flowOf(
            listOf(HomeItemEntity("1", "Title", "Desc"))
        )

        val result = repository.getHomeItems().first()

        assertEquals(Result.success(listOf(HomeItem("1", "Title", "Desc"))), result)
    }

    @Test
    fun `addHomeItem still writes locally when remote fails`() = runTest {
        everySuspend { remote.addItem(any()) } throws RuntimeException("offline")
        everySuspend { local.insert(any()) } returns Unit

        repository.addHomeItem(HomeItem("1", "Title", "Desc"))

        // Offline-first: local write must happen even though remote threw.
        verifySuspend { local.insert(any()) }
    }

    @Test
    fun `removeHomeItem deletes locally when remote confirms`() = runTest {
        everySuspend { remote.deleteItem("1") } returns true
        everySuspend { local.delete("1") } returns Unit

        repository.removeHomeItem("1")

        verifySuspend { local.delete("1") }
    }

    @Test
    fun `removeHomeItem deletes locally when remote throws`() = runTest {
        everySuspend { remote.deleteItem("1") } throws RuntimeException("offline")
        everySuspend { local.delete("1") } returns Unit

        repository.removeHomeItem("1")

        verifySuspend { local.delete("1") }
    }

    @Test
    fun `removeHomeItem does not delete locally when remote rejects`() = runTest {
        everySuspend { remote.deleteItem("1") } returns false

        repository.removeHomeItem("1")

        verifySuspend(VerifyMode.not) { local.delete(any()) }
    }

    @Test
    fun `syncHomeItems replaces local data and reports success`() = runTest {
        everySuspend { remote.fetchItems() } returns emptyList()
        everySuspend { local.replaceAll(any()) } returns Unit

        val result = repository.syncHomeItems()

        assertEquals(Result.success(Unit), result)
        verifySuspend { local.replaceAll(any()) }
    }

    @Test
    fun `syncHomeItems returns error when remote fails`() = runTest {
        everySuspend { remote.fetchItems() } throws RuntimeException("boom")

        val result = repository.syncHomeItems()

        assertTrue(result.isFailure)
    }
}
