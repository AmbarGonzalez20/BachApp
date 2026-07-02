package com.example.bachapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

@Composable
fun BotonObtenerUbicacion(
    onUbicacionObtenida: (Double, Double) -> Unit
) {
    val context = LocalContext.current
    var cargandoUbicacion by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permisos ->
        val concedido = permisos[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permisos[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (concedido) {
            obtenerUbicacion(context) { lat, lon ->
                onUbicacionObtenida(lat, lon)
                cargandoUbicacion = false
            }
        } else {
            error = "Permiso de ubicacion denegado"
            cargandoUbicacion = false
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (cargandoUbicacion) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = AzulClaro,
                    strokeWidth = 2.dp
                )
                Text(
                    text = "Obteniendo ubicacion...",
                    fontSize = 13.sp,
                    color = Color(0xFF888888)
                )
            }
        } else {
            Button(
                onClick = {
                    cargandoUbicacion = true
                    error = ""
                    val tienePermiso = ContextCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED

                    if (tienePermiso) {
                        obtenerUbicacion(context) { lat, lon ->
                            onUbicacionObtenida(lat, lon)
                            cargandoUbicacion = false
                        }
                    } else {
                        launcher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulClaro
                )
            ) {
                Text(
                    text = "📍 Obtener ubicacion GPS",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        if (error.isNotEmpty()) {
            Text(
                text = "⚠️ $error",
                color = Color(0xFFB71C1C),
                fontSize = 13.sp
            )
        }
    }
}

@SuppressLint("MissingPermission")
fun obtenerUbicacion(
    context: Context,
    onResultado: (Double, Double) -> Unit
) {
    val fusedClient = LocationServices.getFusedLocationProviderClient(context)
    fusedClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
        .addOnSuccessListener { location ->
            if (location != null) {
                onResultado(location.latitude, location.longitude)
            } else {
                fusedClient.lastLocation.addOnSuccessListener { last ->
                    if (last != null) {
                        onResultado(last.latitude, last.longitude)
                    }
                }
            }
        }
        .addOnFailureListener {
            // Si falla devuelve 0.0
            onResultado(0.0, 0.0)
        }
}