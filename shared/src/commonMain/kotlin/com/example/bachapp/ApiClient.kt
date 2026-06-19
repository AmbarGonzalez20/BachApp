package com.example.bachapp

import com.example.bachapp.Bache
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * CONSUMO DE API
 * Cliente HTTP compartido que se comunica con el backend en Railway.
 * Usa Ktor Client con motor OkHttp para Android.
 */
object ApiClient {

    private const val BASE_URL = "https://backend-production-ad16.up.railway.app"

    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    // Prueba de conexión
    suspend fun obtenerMensaje(): String =
        client.get(BASE_URL).body()

    // GET — obtener lista de baches
    suspend fun obtenerBaches(): List<Bache> =
        client.get("$BASE_URL/api/baches").body()

    // POST — reportar un bache
    suspend fun crearBache(bache: Bache): Bache =
        client.post("$BASE_URL/api/baches") {
            contentType(ContentType.Application.Json)
            setBody(bache)
        }.body()

    // GET /api/baches/{id}
    suspend fun obtenerBachePorId(id: Int): Bache =
        client.get("$BASE_URL/api/baches/$id").body()
}