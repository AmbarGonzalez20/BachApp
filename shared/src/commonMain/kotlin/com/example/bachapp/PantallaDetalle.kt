package com.example.bachapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaDetalle(
    bacheId: Int,
    onVolver: () -> Unit
) {
    var bache by remember { mutableStateOf<Bache?>(null) }
    var cargando by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf("") }

    LaunchedEffect(bacheId) {
        try {
            bache = ApiClient.obtenerBachePorId(bacheId)
        } catch (e: Exception) {
            error = "Error: ${e.message}"
        }
        cargando = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Bache") },
                navigationIcon = {
                    TextButton(onClick = onVolver) {
                        Text("< Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (cargando) {
                CircularProgressIndicator()
            } else if (error.isNotEmpty()) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            } else if (bache == null) {
                Text("Bache no encontrado")
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Bache #${bache!!.id}",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        HorizontalDivider()
                        Text(
                            text = "Descripcion:",
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(text = bache!!.descripcion)
                        HorizontalDivider()
                        Text(
                            text = "Ubicacion:",
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(text = "Latitud: ${bache!!.latitud}")
                        Text(text = "Longitud: ${bache!!.longitud}")
                        HorizontalDivider()
                        Text(
                            text = "Fecha: ${bache!!.fechaReporte}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onVolver,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver a la lista")
                }
            }
        }
    }
}