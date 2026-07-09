package com.example.bachapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun PantallaDetalleAdmin(
    bacheId: Int,
    onVolver: () -> Unit
) {
    var bache by remember { mutableStateOf<Bache?>(null) }
    var cargando by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf("") }
    var mensajeExito by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(bacheId) {
        try {
            bache = ApiClient.obtenerBachePorId(bacheId)
        } catch (e: Exception) {
            error = "Error: ${e.message}"
        }
        cargando = false
    }

    fun cambiarEstado(nuevoEstado: String) {
        scope.launch {
            try {
                ApiClient.actualizarEstado(bacheId, nuevoEstado)
                bache = bache?.copy(estado = nuevoEstado)
                mensajeExito = "Estado actualizado a: $nuevoEstado"
                error = ""
            } catch (e: Exception) {
                error = "Error: ${e.message}"
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detalle del Reporte",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onVolver) {
                        Text(text = "‹", color = Color.White, fontSize = 28.sp)
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (cargando) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = AzulClaro
                )
            } else if (bache == null) {
                Text(
                    text = "Bache no encontrado",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Encabezado
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = AzulClaro)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "🕳️", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Bache #${bache!!.id}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = Color.White
                            )
                            // Estado actual
                            val colorEstado = when (bache!!.estado) {
                                "pendiente" -> Color(0xFFFFB300)
                                "en proceso" -> Color(0xFF42A5F5)
                                "resuelto" -> Color(0xFF66BB6A)
                                else -> Color.White
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Card(
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White.copy(alpha = 0.2f)
                                )
                            ) {
                                Text(
                                    text = bache!!.estado.uppercase(),
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp, vertical = 6.dp
                                    )
                                )
                            }
                        }
                    }

                    // Descripcion
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "📝 Descripcion",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = AzulOscuro
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = bache!!.descripcion,
                                fontSize = 15.sp,
                                color = Color(0xFF333333)
                            )
                        }
                    }

                    // Ubicacion
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "📍 Ubicacion",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = AzulOscuro
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Latitud: ${bache!!.latitud}",
                                fontSize = 14.sp,
                                color = Color(0xFF555555)
                            )
                            Text(
                                text = "Longitud: ${bache!!.longitud}",
                                fontSize = 14.sp,
                                color = Color(0xFF555555)
                            )
                        }
                    }

                    // Mensajes
                    if (error.isNotEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFEBEE)
                            )
                        ) {
                            Text(
                                text = "⚠️ $error",
                                color = Color(0xFFB71C1C),
                                modifier = Modifier.padding(12.dp),
                                fontSize = 14.sp
                            )
                        }
                    }

                    if (mensajeExito.isNotEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE8F5E9)
                            )
                        ) {
                            Text(
                                text = "✅ $mensajeExito",
                                color = Color(0xFF2E7D32),
                                modifier = Modifier.padding(12.dp),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Botones de estado
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Actualizar estado del bache:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF333333)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            // Pendiente
                            OutlinedButton(
                                onClick = { cambiarEstado("pendiente") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (bache!!.estado == "pendiente")
                                        Color(0xFFFFB300)
                                    else Color.Transparent,
                                    contentColor = if (bache!!.estado == "pendiente")
                                        Color.White
                                    else Color(0xFF888888)
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (bache!!.estado == "pendiente")
                                        Color(0xFFFFB300)
                                    else Color(0xFFCCCCCC)
                                )
                            ) {
                                Text(
                                    text = "⏳ Pendiente",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // En proceso
                            OutlinedButton(
                                onClick = { cambiarEstado("en proceso") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (bache!!.estado == "en proceso")
                                        Color(0xFF42A5F5)
                                    else Color.Transparent,
                                    contentColor = if (bache!!.estado == "en proceso")
                                        Color.White
                                    else Color(0xFF888888)
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (bache!!.estado == "en proceso")
                                        Color(0xFF42A5F5)
                                    else Color(0xFFCCCCCC)
                                )
                            ) {
                                Text(
                                    text = "🔧 En proceso",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Resuelto
                            OutlinedButton(
                                onClick = { cambiarEstado("resuelto") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (bache!!.estado == "resuelto")
                                        Color(0xFF66BB6A)
                                    else Color.Transparent,
                                    contentColor = if (bache!!.estado == "resuelto")
                                        Color.White
                                    else Color(0xFF888888)
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (bache!!.estado == "resuelto")
                                        Color(0xFF66BB6A)
                                    else Color(0xFFCCCCCC)
                                )
                            ) {
                                Text(
                                    text = "✅ Resuelto",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = onVolver,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CafeBoton
                        )
                    ) {
                        Text(
                            text = "‹ Volver al panel",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}