package com.example.myapp.core.ui.state

/** Shared defaults for `StateFlow` sharing across ViewModels. */
object StateConfig {
    /**
     * Keep upstream flows active for this long after the last subscriber
     * disappears, so brief UI teardown (e.g. config changes) doesn't restart them.
     */
    const val SubscriptionTimeoutMs = 5_000L
}
