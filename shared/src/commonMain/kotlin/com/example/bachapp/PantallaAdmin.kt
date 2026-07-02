package com.example.bachapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun PantallaAdmin(
    onCerrarSesion: () -> Unit
) {
    var baches by remember { mutableStateOf<List<Bache>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf("") }
    var mostrarDialogo by remember { mutableStateOf(false) }
    var bacheAEliminar by remember { mutableStateOf<Bache?>(null) }
    val scope = rememberCoroutineScope()

    fun cargarBaches() {
        scope.launch {
            cargando = true
            try {
                baches = ApiClient.obtenerBaches()
            } catch (e: Exception) {
                error = "Error: ${e.message}"
            }
            cargando = false
        }
    }

    LaunchedEffect(Unit) {
        cargarBaches()
    }

    if (mostrarDialogo && bacheAEliminar != null) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = {
                Text(
                    text = "Eliminar bache",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFB71C1C)
                )
            },
            text = {
                Text("¿Estas seguro de eliminar el Bache #${bacheAEliminar!!.id}?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            try {
                                ApiClient.eliminarBache(bacheAEliminar!!.id)
                                cargarBaches()
                            } catch (e: Exception) {
                                error = "Error al eliminar: ${e.message}"
                            }
                            mostrarDialogo = false
                            bacheAEliminar = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFB71C1C)
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { mostrarDialogo = false },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = CafeBoton
                    )
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "👑 Panel Admin",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                actions = {
                    TextButton(onClick = onCerrarSesion) {
                        Text(
                            text = "Salir",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AzulClaro,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = FondoPantalla
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Estadisticas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CardEstadistica(
                    modifier = Modifier.weight(1f),
                    titulo = "Total",
                    valor = "${baches.size}",
                    emoji = "🕳️",
                    color = AzulOscuro
                )
                CardEstadistica(
                    modifier = Modifier.weight(1f),
                    titulo = "Pendientes",
                    valor = "${baches.count { it.fechaReporte.isEmpty() }}",
                    emoji = "⏳",
                    color = Color(0xFFE65100)
                )
                CardEstadistica(
                    modifier = Modifier.weight(1f),
                    titulo = "Resueltos",
                    valor = "${baches.count { it.fechaReporte.isNotEmpty() }}",
                    emoji = "✅",
                    color = Color(0xFF2E7D32)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Reportes de baches",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF333333)
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (cargando) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AzulClaro)
                }
            } else if (error.isNotEmpty()) {
                Text(text = error, color = MaterialTheme.colorScheme.error)
            } else if (baches.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🕳️", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No hay baches reportados",
                            color = Color(0xFF888888)
                        )
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(baches) { bache ->
                        CardBacheAdmin(
                            bache = bache,
                            onEliminar = {
                                bacheAEliminar = bache
                                mostrarDialogo = true
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CardEstadistica(
    modifier: Modifier = Modifier,
    titulo: String,
    valor: String,
    emoji: String,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 24.sp)
            Text(
                text = valor,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = color
            )
            Text(
                text = titulo,
                fontSize = 12.sp,
                color = Color(0xFF888888)
            )
        }
    }
}

@Composable
fun CardBacheAdmin(
    bache: Bache,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(AzulClaro.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🕳️", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Bache #${bache.id}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = AzulOscuro
                    )
                    Text(
                        text = bache.descripcion,
                        fontSize = 13.sp,
                        color = Color(0xFF555555),
                        maxLines = 2
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onEliminar,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFB71C1C)
                )
            ) {
                Text(text = "🗑️ Eliminar", fontSize = 13.sp)
            }
        }
    }
}