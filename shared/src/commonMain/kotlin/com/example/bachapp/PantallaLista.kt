package com.example.bachapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaLista(
    onReportar: () -> Unit,
    onVerDetalle: (Int) -> Unit
) {
    var baches by remember { mutableStateOf<List<Bache>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        try {
            baches = ApiClient.obtenerBaches()
        } catch (e: Exception) {
            error = "Error: ${e.message}"
        }
        cargando = false
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Baches Reportados") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onReportar) {
                Text("+")
            }
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
                Text(text = error, color = MaterialTheme.colorScheme.error)
            } else if (baches.isEmpty()) {
                Spacer(modifier = Modifier.height(32.dp))
                Text("No hay baches reportados aun.")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onReportar) {
                    Text("Reportar primer bache")
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(baches) { bache ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            onClick = { onVerDetalle(bache.id) }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Bache #${bache.id}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(text = bache.descripcion)
                                Text(
                                    text = "Lat: ${bache.latitud}  Lon: ${bache.longitud}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}