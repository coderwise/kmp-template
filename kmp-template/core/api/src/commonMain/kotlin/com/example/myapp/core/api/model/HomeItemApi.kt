package com.example.myapp.core.api.model

import kotlinx.serialization.Serializable

@Serializable
data class HomeItemApi(
    val id: String,
    val title: String,
    val description: String
)
