package com.example.myapp.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class HomeItem(
    val id: String,
    val title: String,
    val description: String
)
