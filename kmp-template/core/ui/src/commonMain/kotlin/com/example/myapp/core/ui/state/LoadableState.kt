package com.example.myapp.core.ui.state

/**
 * Shared loading/error contract for UI states. Implementing this lets a state
 * reuse the [loading] and [error] transitions instead of each ViewModel
 * re-declaring its own copies.
 */
interface LoadableState<T : LoadableState<T>> {
    val isLoading: Boolean
    val isError: Boolean
    val errorMessage: String?

    /** Returns a copy of this state with the loading fields replaced. */
    fun copyLoadState(
        isLoading: Boolean = this.isLoading,
        isError: Boolean = this.isError,
        errorMessage: String? = this.errorMessage,
    ): T
}

fun <T : LoadableState<T>> T.loading(): T =
    copyLoadState(isLoading = true, isError = false)

fun <T : LoadableState<T>> T.error(message: String?): T =
    copyLoadState(isLoading = false, isError = true, errorMessage = message)
