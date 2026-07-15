package com.example.myapp.core.domain.arch

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

/**
 * Base class for suspending UseCases.
 * Handles execution on the provided [dispatcher].
 */
abstract class SuspendUseCase<in P, out R>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend operator fun invoke(params: P): R = withContext(dispatcher) {
        execute(params)
    }

    protected abstract suspend fun execute(params: P): R
}

/**
 * Base class for UseCases that return a Flow.
 * Automatically applies [flowOn] with the provided [dispatcher].
 */
abstract class FlowUseCase<in P, out R>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    operator fun invoke(params: P): Flow<R> = execute(params).flowOn(dispatcher)

    protected abstract fun execute(params: P): Flow<R>
}

/**
 * Base class for suspending UseCases that don't take any parameters.
 */
abstract class SuspendResultUseCase<out R>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend operator fun invoke(): R = withContext(dispatcher) {
        execute()
    }

    protected abstract suspend fun execute(): R
}

/**
 * Base class for UseCases that return a Flow and don't take any parameters.
 */
abstract class FlowResultUseCase<out R>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    operator fun invoke(): Flow<R> = execute().flowOn(dispatcher)

    protected abstract fun execute(): Flow<R>
}

/**
 * Base class for synchronous UseCases.
 */
abstract class UseCase<in P, out R> {
    abstract operator fun invoke(params: P): R
}

/**
 * Base class for synchronous UseCases with no parameters.
 */
abstract class ResultUseCase<out R> {
    abstract operator fun invoke(): R
}
