package com.example.bachapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun App() {

    var pantallaActual by remember {
        mutableStateOf("login")
    }

    var bacheIdSeleccionado by remember {
        mutableStateOf(0)
    }

    /*
     * Datos del usuario que inició sesión.
     */
    var usuarioActualId by remember {
        mutableStateOf(0)
    }

    var nombreUsuario by remember {
        mutableStateOf("")
    }

    var correoUsuario by remember {
        mutableStateOf("")
    }

    var rolUsuario by remember {
        mutableStateOf("")
    }

    /*
     * Limpia los datos del usuario y vuelve al login.
     */
    fun cerrarSesion() {
        usuarioActualId = 0
        nombreUsuario = ""
        correoUsuario = ""
        rolUsuario = ""
        bacheIdSeleccionado = 0
        pantallaActual = "login"
    }

    when (pantallaActual) {

        "login" -> PantallaLogin(
            onLoginExitoso = { respuesta ->

                /*
                 * Guardamos todos los datos que regresó el backend.
                 */
                usuarioActualId = respuesta.id
                nombreUsuario = respuesta.nombre
                correoUsuario = respuesta.email
                rolUsuario = respuesta.rol

                pantallaActual = if (
                    respuesta.rol.equals(
                        "administrador",
                        ignoreCase = true
                    )
                ) {
                    "admin"
                } else {
                    "inicioCiudadano"
                }
            },
            onIrARegistro = {
                pantallaActual = "registro"
            }
        )

        "registro" -> PantallaRegistro(
            onRegistroExitoso = {
                pantallaActual = "login"
            },
            onIrALogin = {
                pantallaActual = "login"
            }
        )

        "inicioCiudadano" -> PantallaInicioCiudadano(
            onRegistrarBache = {
                pantallaActual = "reporte"
            },
            onVerReportes = {
                pantallaActual = "lista"
            },
            onCerrarSesion = {
                cerrarSesion()
            }
        )

        "lista" -> PantallaLista(
            onReportar = {
                pantallaActual = "reporte"
            },
            onVerDetalle = { id ->
                bacheIdSeleccionado = id
                pantallaActual = "detalle"
            },
            onVolverInicio = {
                pantallaActual = "inicioCiudadano"
            },
            onCerrarSesion = {
                cerrarSesion()
            }
        )

        "reporte" -> PantallaReporte(
            usuarioActualId = usuarioActualId,
            onVolver = {
                pantallaActual = "inicioCiudadano"
            },
            onVerReportes = {
                pantallaActual = "lista"
            },
            onReporteExitoso = {
                pantallaActual = "reporteExitoso"
            },
            onCerrarSesion = {
                cerrarSesion()
            }
        )

        "reporteExitoso" -> PantallaReporteExitoso(
            onVerReportes = {
                pantallaActual = "lista"
            },
            onRegistrarOtro = {
                pantallaActual = "reporte"
            },
            onVolverInicio = {
                pantallaActual = "inicioCiudadano"
            },
            onCerrarSesion = {
                cerrarSesion()
            }
        )

        "detalle" -> PantallaDetalle(
            bacheId = bacheIdSeleccionado,
            usuarioActualId = usuarioActualId,
            onVolver = {
                pantallaActual = "lista"
            }
        )

        "admin" -> PantallaAdmin(
            onCerrarSesion = {
                cerrarSesion()
            }
        )

        else -> {
            pantallaActual = "login"
        }
    }
}