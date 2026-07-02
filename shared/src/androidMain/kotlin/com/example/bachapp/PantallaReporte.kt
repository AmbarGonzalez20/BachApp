package com.example.bachapp

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch

@Composable
fun PantallaReporte(
    onVolver: () -> Unit
) {
    var descripcion by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    var exito by remember { mutableStateOf(false) }
    var mostrarCamara by remember { mutableStateOf(false) }
    var fotoUri by remember { mutableStateOf<Uri?>(null) }
    val scope = rememberCoroutineScope()
    var latitud by remember { mutableStateOf(0.0) }
    var longitud by remember { mutableStateOf(0.0) }

    if (mostrarCamara) {
        PantallaCamara(
            onFotoTomada = { uri ->
                fotoUri = uri
                mostrarCamara = false
            },
            onCancelar = { mostrarCamara = false }
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Reportar Bache",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
                        text = "Nuevo Reporte",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.White
                    )
                    Text(
                        text = "Ayuda a mejorar las calles de tu ciudad",
                        fontSize = 13.sp,
                        color = Color(0xFFE3F2FD)
                    )
                }
            }

            // Foto
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Foto del bache",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = Color(0xFF333333)
                    )

                    if (fotoUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(fotoUri),
                            contentDescription = "Foto del bache",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                        OutlinedButton(
                            onClick = { mostrarCamara = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = CafeBoton
                            )
                        ) {
                            Text("📷 Tomar otra foto")
                        }
                    } else {
                        Button(
                            onClick = { mostrarCamara = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CafeBoton
                            )
                        ) {
                            Text(
                                text = "📷 Tomar foto",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
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
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Descripcion del bache",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = Color(0xFF333333)
                    )
                    // GPS
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Ubicacion del bache",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                color = Color(0xFF333333)
                            )

                            BotonObtenerUbicacion(
                                onUbicacionObtenida = { lat, lon ->
                                    latitud = lat
                                    longitud = lon
                                }
                            )

                            if (latitud != 0.0 || longitud != 0.0) {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFFE8F5E9)
                                    )
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = "✅ Ubicacion obtenida",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = Color(0xFF2E7D32)
                                        )
                                        Text(
                                            text = "Lat: $latitud",
                                            fontSize = 12.sp,
                                            color = Color(0xFF555555)
                                        )
                                        Text(
                                            text = "Lon: $longitud",
                                            fontSize = 12.sp,
                                            color = Color(0xFF555555)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        placeholder = { Text("Describe la ubicacion y el estado del bache...") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AzulClaro,
                            unfocusedBorderColor = Color(0xFFCCCCCC)
                        )
                    )
                }
            }

            // Error
            if (error.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                ) {
                    Text(
                        text = "⚠️ $error",
                        color = Color(0xFFB71C1C),
                        modifier = Modifier.padding(12.dp),
                        fontSize = 14.sp
                    )
                }
            }

            // Exito
            if (exito) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Text(
                        text = "✅ Bache reportado exitosamente",
                        color = Color(0xFF2E7D32),
                        modifier = Modifier.padding(12.dp),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Botones
            if (cargando) {
                CircularProgressIndicator(color = AzulClaro)
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
                                                latitud = latitud,
                                                longitud = longitud,
                                                fotoUrl = fotoUri?.toString() ?: ""
                                            )
                                        )
                                        exito = true
                                        cargando = false
                                        descripcion = ""
                                        fotoUri = null
                                    } catch (e: Exception) {
                                        error = "Error al enviar: ${e.message}"
                                        cargando = false
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CafeBoton
                    )
                ) {
                    Text(
                        text = "📤 Enviar Reporte",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedButton(
                    onClick = onVolver,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = CafeBoton
                    )
                ) {
                    Text(
                        text = "📋 Ver reportes creados",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}