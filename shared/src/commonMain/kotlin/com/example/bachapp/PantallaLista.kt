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
            error = ""
            baches = emptyList()
        }
        cargando = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "🕳️ BachApp",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AzulClaro,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onReportar,
                containerColor = CafeBoton,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Text(text = "+", fontSize = 28.sp)
            }
        },
        containerColor = FondoPantalla
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            if (cargando) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AzulClaro)
                }
            } else if (baches.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🕳️", fontSize = 64.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No hay baches reportados",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF555555)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Toca el + para reportar uno",
                            fontSize = 14.sp,
                            color = Color(0xFF888888)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = onReportar,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CafeBoton
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Reportar primer bache", color = Color.White)
                        }
                    }
                }
            } else {
                Text(
                    text = "${baches.size} bache(s) reportado(s)",
                    fontSize = 13.sp,
                    color = Color(0xFF888888),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(baches) { bache ->
                        CardBache(bache = bache, onClick = { onVerDetalle(bache.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun CardBache(bache: Bache, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(AzulClaro.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🕳️", fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Bache #${bache.id}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = AzulOscuro
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = bache.descripcion,
                    fontSize = 14.sp,
                    color = Color(0xFF333333),
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "📍", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Lat: ${bache.latitud}  Lon: ${bache.longitud}",
                        fontSize = 12.sp,
                        color = Color(0xFF888888)
                    )
                }
            }

            Text(text = "›", fontSize = 24.sp, color = CafeBoton)
        }
    }
}