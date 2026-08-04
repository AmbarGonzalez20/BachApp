package com.example.bachapp

import kotlinx.serialization.Serializable

@Serializable
data class Bache(
    val id: Int = 0,
    val descripcion: String,
    val latitud: Double,
    val longitud: Double,
    val fotoUrl: String = "",
    val fechaReporte: String = "",
    val estado: String = "pendiente",

    // Usuario que creó el reporte
    val usuarioId: Int = 0
)

@Serializable
data class ActualizarEstadoRequest(
    val estado: String
)

@Serializable
data class ActualizarBacheRequest(
    val descripcion: String,
    val fotoUrl: String
)
@Serializable
data class ActualizarBacheRequestConUsuario(
    val descripcion: String,
    val fotoUrl: String,
    val usuarioId: Int
)
