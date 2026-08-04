package com.example.bachapp



import kotlinx.serialization.Serializable

@Serializable
data class UploadResponse(
    val url: String = ""
)