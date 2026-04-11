package com.example.myapp.core.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PrefsDataSource {
    private val _data = MutableStateFlow("")
    val data: Flow<String> = _data.asStateFlow()

    fun edit(transform: (String) -> String) {
        _data.update(transform)
    }
}
