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

    var latitud by remember { mutableStateOf(0.0) }
    var longitud by remember { mutableStateOf(0.0) }

    val scope = rememberCoroutineScope()

    if (mostrarCamara) {
        PantallaCamara(
            onFotoTomada = { uri ->
                fotoUri = uri
                mostrarCamara = false
            },
            onCancelar = {
                mostrarCamara = false
            }
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Reportar Bache",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onVolver) {
                        Text(
                            text = "‹",
                            color = Color.White,
                            fontSize = 28.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AzulOscuro,
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
                .padding(18.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AzulClaro
                )
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🕳️",
                        fontSize = 50.sp
                    )

                    Text(
                        text = "Nuevo reporte",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulOscuro
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Ayuda a mejorar las calles de tu ciudad",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Text(
                        text = "📷 Foto del bache",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulOscuro
                    )

                    if (fotoUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(fotoUri),
                            contentDescription = "Foto del bache",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(210.dp)
                                .clip(RoundedCornerShape(18.dp)),
                            contentScale = ContentScale.Crop
                        )

                        OutlinedButton(
                            onClick = {
                                mostrarCamara = true
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Tomar otra foto")
                        }

                    } else {
                        Button(
                            onClick = {
                                mostrarCamara = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = VerdeBoton
                            )
                        ) {
                            Text(
                                text = "📷 Tomar foto",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Text(
                        text = "📍 Ubicación del bache",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulOscuro
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
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE8F5E9)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(
                                    text = "✅ Ubicación obtenida",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32)
                                )
                                Text("Latitud: $latitud")
                                Text("Longitud: $longitud")
                            }
                        }
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Text(
                        text = "📝 Descripción",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulOscuro
                    )

                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = {
                            descripcion = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4,
                        placeholder = {
                            Text("Describe el bache, calle o referencia...")
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AzulOscuro,
                            unfocusedBorderColor = Color.LightGray
                        )
                    )
                }
            }

            if (error.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFEBEE)
                    )
                ) {
                    Text(
                        text = "⚠️ $error",
                        color = Color(0xFFB71C1C),
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }

            if (exito) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE8F5E9)
                    )
                ) {
                    Text(
                        text = "✅ Bache reportado exitosamente",
                        color = Color(0xFF2E7D32),
                        modifier = Modifier.padding(14.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (cargando) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = AzulOscuro
                    )
                }
            } else {
                Button(
                    onClick = {
                        when {
                            descripcion.isBlank() -> {
                                error = "La descripción es obligatoria"
                            }

                            latitud == 0.0 && longitud == 0.0 -> {
                                error = "Debes obtener la ubicación del bache"
                            }

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
                                        latitud = 0.0
                                        longitud = 0.0

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
                        .height(58.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdeBoton
                    )
                ) {
                    Text(
                        text = "📤 Enviar reporte",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onVolver,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        text = "📋 Ver reportes",
                        fontWeight = FontWeight.Bold,
                        color = AzulOscuro
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}