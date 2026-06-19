package com.example.bachapp

class Greeting {
    suspend fun obtenerMensajeBackend(): String {
        return try {
            ApiClient.obtenerMensaje()
        } catch (e: Exception) {
            "Error de conexión: ${e.message}"
        }
    }
}