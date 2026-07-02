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
                title = {
                    Text(
                        text = "Detalle del Bache",
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
            } else if (error.isNotEmpty()) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
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

                    // Fecha
                    if (bache!!.fechaReporte.isNotEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "📅 Fecha de reporte",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = AzulOscuro
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = bache!!.fechaReporte,
                                    fontSize = 14.sp,
                                    color = Color(0xFF555555)
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
                            text = "‹ Volver a la lista",
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