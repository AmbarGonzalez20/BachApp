package com.example.bachapp

import androidx.compose.runtime.*
import kotlinx.coroutines.launch

@Composable
fun App() {
    var pantallaActual by remember { mutableStateOf("lista") }
    var bacheIdSeleccionado by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    when (pantallaActual) {
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
    }
}