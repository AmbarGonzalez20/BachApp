package com.example.bachapp

import androidx.compose.runtime.*

@Composable
fun App() {
    var pantallaActual by remember { mutableStateOf("login") }
    var bacheIdSeleccionado by remember { mutableStateOf(0) }
    var rolUsuario by remember { mutableStateOf("") }

    when (pantallaActual) {
        "login" -> PantallaLogin(
            onLoginExitoso = { rol ->
                rolUsuario = rol
                pantallaActual = if (rol == "administrador") "admin" else "lista"
            },
            onIrARegistro = { pantallaActual = "registro" }
        )
        "registro" -> PantallaRegistro(
            onRegistroExitoso = { pantallaActual = "login" },
            onIrALogin = { pantallaActual = "login" }
        )
        "lista" -> PantallaLista(
            onReportar = { pantallaActual = "reporte" },
            onVerDetalle = { id ->
                bacheIdSeleccionado = id
                pantallaActual = "detalle"
            }
        )
        "reporte" -> PantallaReporte(
            onVolver = { pantallaActual = "lista" }
        )
        "detalle" -> PantallaDetalle(
            bacheId = bacheIdSeleccionado,
            onVolver = { pantallaActual = "lista" }
        )
        "admin" -> PantallaAdmin(
            onCerrarSesion = { pantallaActual = "login" }
        )
    }
}