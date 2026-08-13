package com.example.bachapp

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object ApiClient {

    private const val BASE_URL =
        "https://backend-production-ad16.up.railway.app"

    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true
                }
            )
        }
    }

    suspend fun obtenerMensaje(): String {
        return client
            .get(BASE_URL)
            .body()
    }

    suspend fun obtenerBaches(): List<Bache> {
        return client
            .get("$BASE_URL/api/baches")
            .body()
    }

    suspend fun crearBache(
        bache: Bache
    ): Bache {
        val respuesta = client.post(
            "$BASE_URL/api/baches"
        ) {
            contentType(ContentType.Application.Json)
            setBody(
                CrearBacheRequest(
                    descripcion = bache.descripcion,
                    latitud = bache.latitud,
                    longitud = bache.longitud,
                    fotoUrl = bache.fotoUrl,
                    usuarioId = bache.usuarioId
                )
            )
        }

        if (respuesta.status.value !in 200..299) {
            val detalle = respuesta.bodyAsText()

            throw IllegalStateException(
                "No se pudo registrar el bache. " +
                        "Código ${respuesta.status.value}. $detalle"
            )
        }

        return respuesta.body()
    }

    suspend fun obtenerBachePorId(
        id: Int
    ): Bache {
        return client
            .get("$BASE_URL/api/baches/$id")
            .body()
    }

    suspend fun eliminarBache(
        id: Int
    ) {
        val respuesta = client.delete(
            "$BASE_URL/api/baches/$id"
        )

        if (respuesta.status.value !in 200..299) {
            val detalle = respuesta.bodyAsText()

            throw IllegalStateException(
                "No se pudo eliminar el reporte. " +
                        "Código ${respuesta.status.value}. $detalle"
            )
        }
    }

    suspend fun registrarUsuario(
        usuario: Usuario
    ): LoginResponse {
        return client
            .post("$BASE_URL/api/registro") {
                contentType(ContentType.Application.Json)
                setBody(usuario)
            }
            .body()
    }

    suspend fun login(
        email: String,
        password: String
    ): LoginResponse {
        return client
            .post("$BASE_URL/api/login") {
                contentType(ContentType.Application.Json)

                setBody(
                    LoginRequest(
                        email = email,
                        password = password
                    )
                )
            }
            .body()
    }

    /*
     * Guarda el estado en Railway/MySQL.
     *
     * Esta función no modifica solamente la interfaz:
     * comprueba que el servidor responda con un código exitoso.
     */
    suspend fun actualizarEstado(
        id: Int,
        estado: String
    ) {
        val estadoNormalizado = when (
            estado.trim().lowercase().replace("_", " ")
        ) {
            "pendiente" -> "pendiente"
            "en proceso" -> "en_proceso"
            "resuelto" -> "resuelto"

            else -> throw IllegalArgumentException(
                "Estado no válido: $estado"
            )
        }

        val respuesta = client.put(
            "$BASE_URL/api/baches/$id/estado"
        ) {
            contentType(ContentType.Application.Json)

            setBody(
                ActualizarEstadoRequest(
                    estado = estadoNormalizado
                )
            )
        }

        if (respuesta.status.value !in 200..299) {
            val detalle = respuesta.bodyAsText()

            throw IllegalStateException(
                "El servidor no guardó el estado. " +
                        "Código ${respuesta.status.value}. $detalle"
            )
        }
    }


    suspend fun subirFoto(
        bytes: ByteArray,
        nombreArchivo: String
    ): String {
        val respuesta: UploadResponse =
            client
                .post("$BASE_URL/api/upload") {
                    setBody(
                        MultiPartFormDataContent(
                            formData {
                                append(
                                    key = "foto",
                                    value = bytes,
                                    headers = Headers.build {
                                        append(
                                            HttpHeaders.ContentType,
                                            ContentType.Image.JPEG.toString()
                                        )

                                        append(
                                            HttpHeaders.ContentDisposition,
                                            "filename=\"$nombreArchivo\""
                                        )
                                    }
                                )
                            }
                        )
                    )
                }
                .body()

        if (respuesta.url.isBlank()) {
            throw IllegalStateException(
                "El servidor no devolvió la URL de la fotografía."
            )
        }

        return respuesta.url
    }
}