package com.example.myapp.libs.version

// Webpack replaces `process.env.NODE_ENV` at bundle time: "production" for
// production builds, "development" otherwise. The `typeof` guard keeps this safe
// if `process` is undefined (falls back to release).
actual val isDebugBuild: Boolean =
    js("typeof process !== 'undefined' && process.env != null && process.env.NODE_ENV !== 'production'")
        .unsafeCast<Boolean>()
