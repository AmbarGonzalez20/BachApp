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
    val usuarioId: Int = 0,

    // Evidencia cuando el administrador resuelve el bache
    val fotoResolucionUrl: String = "",
    val comentarioResolucion: String = "",
    val fechaResolucion: String = ""
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

@Serializable
data class CrearBacheRequest(
    val descripcion: String,
    val latitud: Double,
    val longitud: Double,
    val fotoUrl: String = "",
    val usuarioId: Int = 0
)
