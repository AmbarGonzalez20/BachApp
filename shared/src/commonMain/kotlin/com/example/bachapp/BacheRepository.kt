package com.example.bachapp

/**
 * ALMACENAMIENTO LOCAL + SERVICIO
 * Repositorio que combina:
 * - Datos remotos (backend Railway)
 * - Caché local en memoria
 */
class BacheRepository {

    // Almacenamiento local simple (caché en memoria)
    private val cacheBaches = mutableListOf<Bache>()

    /**
     * Obtiene baches del backend y los guarda localmente.
     * Si falla la red, devuelve el caché local.
     */
    suspend fun obtenerBaches(): List<Bache> {
        return try {
            val remotos = ApiClient.obtenerBaches()
            cacheBaches.clear()
            cacheBaches.addAll(remotos)
            remotos
        } catch (e: Exception) {
            // Sin internet → devuelve lo que hay en caché
            cacheBaches.toList()
        }
    }

    /**
     * Envía un reporte al backend.
     */
    suspend fun reportarBache(bache: Bache): Bache {
        val creado = ApiClient.crearBache(bache)
        cacheBaches.add(creado)
        return creado
    }

    /**
     * Devuelve el caché local sin llamar a la red.
     */
    fun obtenerCacheLocal(): List<Bache> = cacheBaches.toList()
}