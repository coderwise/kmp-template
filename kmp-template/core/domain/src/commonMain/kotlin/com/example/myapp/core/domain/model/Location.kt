package com.example.myapp.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String? = null,
    val admin1: String? = null
)
