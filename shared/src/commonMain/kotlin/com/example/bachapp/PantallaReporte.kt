package com.example.bachapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun PantallaReporte(
    onVolver: () -> Unit
) {
    var descripcion by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    var exito by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reportar Bache") },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Nuevo Reporte",
                style = MaterialTheme.typography.headlineSmall
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripcion del bache") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            if (error.isNotEmpty()) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }

            if (exito) {
                Text(
                    text = "Bache reportado exitosamente",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (cargando) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        when {
                            descripcion.isBlank() -> error = "La descripcion es obligatoria"
                            else -> {
                                error = ""
                                cargando = true
                                scope.launch {
                                    try {
                                        ApiClient.crearBache(
                                            Bache(
                                                descripcion = descripcion,
                                                latitud = 0.0,
                                                longitud = 0.0
                                            )
                                        )
                                        exito = true
                                        cargando = false
                                    } catch (e: Exception) {
                                        error = "Error: ${e.message}"
                                        cargando = false
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Enviar Reporte")
                }
            }
        }
    }
}