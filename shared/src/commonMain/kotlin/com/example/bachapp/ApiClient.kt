package com.example.bachapp

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object ApiClient {

    private const val BASE_URL = "https://backend-production-ad16.up.railway.app"

    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun obtenerMensaje(): String =
        client.get(BASE_URL).body()

    suspend fun obtenerBaches(): List<Bache> =
        client.get("$BASE_URL/api/baches").body()

    suspend fun crearBache(bache: Bache): Bache =
        client.post("$BASE_URL/api/baches") {
            contentType(ContentType.Application.Json)
            setBody(bache)
        }.body()

    suspend fun obtenerBachePorId(id: Int): Bache =
        client.get("$BASE_URL/api/baches/$id").body()

    suspend fun eliminarBache(id: Int) {
        client.delete("$BASE_URL/api/baches/$id")
    }

    suspend fun registrarUsuario(usuario: Usuario): LoginResponse =
        client.post("$BASE_URL/api/registro") {
            contentType(ContentType.Application.Json)
            setBody(usuario)
        }.body()

    suspend fun login(email: String, password: String): LoginResponse =
        client.post("$BASE_URL/api/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email = email, password = password))
        }.body()
}