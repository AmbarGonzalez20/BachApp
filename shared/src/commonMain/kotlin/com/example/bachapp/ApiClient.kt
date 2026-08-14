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


    const val BASE_URL =
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

    fun urlCompleta(ruta: String): String {
        val valor = ruta.trim()

        return when {
            valor.isBlank() -> ""

            valor.startsWith(
                "http://",
                ignoreCase = true
            ) -> valor

            valor.startsWith(
                "https://",
                ignoreCase = true
            ) -> valor

            valor.startsWith("/") ->
                "$BASE_URL$valor"

            else ->
                "$BASE_URL/$valor"
        }
    }

    private suspend fun validarRespuesta(
        codigo: Int,
        detalle: String,
        accion: String
    ) {
        if (codigo !in 200..299) {
            throw IllegalStateException(
                "$accion. Código $codigo. $detalle"
            )
        }
    }

    suspend fun obtenerMensaje(): String {
        val respuesta = client.get(BASE_URL)
        val texto = respuesta.bodyAsText()

        validarRespuesta(
            codigo = respuesta.status.value,
            detalle = texto,
            accion = "No fue posible conectar con el servidor"
        )

        return texto
    }

    suspend fun obtenerBaches(): List<Bache> {
        val respuesta = client.get(
            "$BASE_URL/api/baches"
        )

        if (respuesta.status.value !in 200..299) {
            val detalle = respuesta.bodyAsText()
            throw IllegalStateException(
                "No fue posible obtener los reportes. " +
                    "Código ${respuesta.status.value}. $detalle"
            )
        }

        return respuesta.body()
    }

    suspend fun crearBache(
        bache: Bache
    ): Bache {
        val respuesta = client.post(
            "$BASE_URL/api/baches"
        ) {
            contentType(
                ContentType.Application.Json
            )

            setBody(
                CrearBacheRequest(
                    descripcion =
                        bache.descripcion,
                    latitud =
                        bache.latitud,
                    longitud =
                        bache.longitud,
                    fotoUrl =
                        bache.fotoUrl,
                    usuarioId =
                        bache.usuarioId
                )
            )
        }

        if (respuesta.status.value !in 200..299) {
            val detalle =
                respuesta.bodyAsText()

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
        val respuesta = client.get(
            "$BASE_URL/api/baches/$id"
        )

        if (respuesta.status.value !in 200..299) {
            val detalle =
                respuesta.bodyAsText()

            throw IllegalStateException(
                "No fue posible obtener el reporte. " +
                    "Código ${respuesta.status.value}. $detalle"
            )
        }

        return respuesta.body()
    }

    suspend fun eliminarBache(
        id: Int
    ) {
        val respuesta = client.delete(
            "$BASE_URL/api/baches/$id"
        )

        if (respuesta.status.value !in 200..299) {
            val detalle =
                respuesta.bodyAsText()

            throw IllegalStateException(
                "No se pudo eliminar el reporte. " +
                    "Código ${respuesta.status.value}. $detalle"
            )
        }
    }

    suspend fun registrarUsuario(
        usuario: Usuario
    ): LoginResponse {
        val respuesta = client.post(
            "$BASE_URL/api/registro"
        ) {
            contentType(
                ContentType.Application.Json
            )
            setBody(usuario)
        }

        if (respuesta.status.value !in 200..299) {
            val detalle =
                respuesta.bodyAsText()

            throw IllegalStateException(
                "No fue posible registrar al usuario. " +
                    "Código ${respuesta.status.value}. $detalle"
            )
        }

        return respuesta.body()
    }

    suspend fun login(
        email: String,
        password: String
    ): LoginResponse {
        val respuesta = client.post(
            "$BASE_URL/api/login"
        ) {
            contentType(
                ContentType.Application.Json
            )

            setBody(
                LoginRequest(
                    email = email.trim(),
                    password = password
                )
            )
        }

        if (respuesta.status.value == 401) {
            throw IllegalArgumentException(
                "Correo o contraseña incorrectos."
            )
        }

        if (respuesta.status.value !in 200..299) {
            val detalle = respuesta.bodyAsText()

            throw IllegalStateException(
                "No fue posible iniciar sesión. " +
                    "Código ${respuesta.status.value}. $detalle"
            )
        }

        return respuesta.body()
    }

    /*
     * Estados compatibles con el backend:
     * pendiente
     * en_proceso
     * resuelto
     */
    suspend fun actualizarEstado(
        id: Int,
        estado: String
    ) {
        val estadoNormalizado = when (
            estado
                .trim()
                .lowercase()
                .replace(" ", "_")
        ) {
            "pendiente" ->
                "pendiente"

            "en_proceso" ->
                "en_proceso"

            "resuelto" ->
                "resuelto"

            else ->
                throw IllegalArgumentException(
                    "Estado no válido: $estado"
                )
        }

        val respuesta = client.put(
            "$BASE_URL/api/baches/$id/estado"
        ) {
            contentType(
                ContentType.Application.Json
            )

            setBody(
                ActualizarEstadoRequest(
                    estado =
                        estadoNormalizado
                )
            )
        }

        if (respuesta.status.value !in 200..299) {
            val detalle =
                respuesta.bodyAsText()

            throw IllegalStateException(
                "El servidor no guardó el estado. " +
                    "Código ${respuesta.status.value}. $detalle"
            )
        }
    }

    /*
     * NUEVO:
     * Guarda en PostgreSQL la evidencia de reparación
     * y el backend cambia el estado a "resuelto".
     */
    suspend fun resolverBache(
        id: Int,
        fotoResolucionUrl: String,
        comentarioResolucion: String
    ): Bache {
        if (fotoResolucionUrl.isBlank()) {
            throw IllegalArgumentException(
                "La fotografía de resolución es obligatoria."
            )
        }

        val respuesta = client.put(
            "$BASE_URL/api/baches/$id/resolver"
        ) {
            contentType(
                ContentType.Application.Json
            )

            setBody(
                ResolverBacheRequest(
                    fotoResolucionUrl =
                        fotoResolucionUrl,
                    comentarioResolucion =
                        comentarioResolucion.trim()
                )
            )
        }

        if (respuesta.status.value !in 200..299) {
            val detalle =
                respuesta.bodyAsText()

            throw IllegalStateException(
                "No se pudo guardar la resolución. " +
                    "Código ${respuesta.status.value}. $detalle"
            )
        }

        val bacheActualizado: Bache =
            respuesta.body()

        val estadoGuardado =
            bacheActualizado.estado
                .trim()
                .lowercase()
                .replace(" ", "_")

        if (estadoGuardado != "resuelto") {
            throw IllegalStateException(
                "El servidor respondió, pero el reporte " +
                    "no quedó guardado como resuelto."
            )
        }

        return bacheActualizado
    }

    /*
     * Compatible con el POST /api/upload que tu compañera
     * corrigió para Ktor 3.x usando readPart()/provider().
     */
    suspend fun subirFoto(
        bytes: ByteArray,
        nombreArchivo: String
    ): String {
        val respuesta = client.post(
            "$BASE_URL/api/upload"
        ) {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append(
                            key = "foto",
                            value = bytes,
                            headers =
                                Headers.build {
                                    append(
                                        HttpHeaders.ContentType,
                                        ContentType.Image.JPEG
                                            .toString()
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

        if (respuesta.status.value !in 200..299) {
            val detalle =
                respuesta.bodyAsText()

            throw IllegalStateException(
                "No fue posible subir la fotografía. " +
                    "Código ${respuesta.status.value}. $detalle"
            )
        }

        val datos: UploadResponse =
            respuesta.body()

        if (datos.url.isBlank()) {
            throw IllegalStateException(
                "El servidor no devolvió la URL de la fotografía."
            )
        }

        return datos.url
    }
}
