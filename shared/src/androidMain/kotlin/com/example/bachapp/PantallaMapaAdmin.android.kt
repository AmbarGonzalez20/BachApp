package com.example.bachapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.PriorityHigh
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

private val AmarilloMapa = Color(0xFFFFC107)
private val AzulProcesoMapa = Color(0xFF2F6FA3)
private val VerdeMapa = Color(0xFF2F6B55)
private val RojoMapa = Color(0xFFB3261E)
private val AzulUbicacionMapa = Color(0xFF173B57)
private val NegroMapa = Color(0xFF252525)
private val GrisMapa = Color(0xFF62676D)

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun PantallaMapaAdmin(
    baches: List<Bache>,
    conteoCercania: Map<Int, Int>,
    onVerDetalle: (Int) -> Unit
) {
    val context = LocalContext.current
    val onVerDetalleActual = rememberUpdatedState(onVerDetalle)

    var ubicacionAdmin by remember {
        mutableStateOf<Pair<Double, Double>?>(null)
    }

    var cargandoUbicacion by remember {
        mutableStateOf(true)
    }

    var webViewActual by remember {
        mutableStateOf<WebView?>(null)
    }

    fun obtenerUbicacionSiEsPosible() {
        obtenerUbicacionAdministrador(
            context = context,
            onResultado = { latitud, longitud ->
                ubicacionAdmin = latitud to longitud
                cargandoUbicacion = false
            },
            onError = {
                cargandoUbicacion = false
            }
        )
    }

    val permisoUbicacionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permisos ->
        val concedido =
            permisos[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permisos[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (concedido) {
            obtenerUbicacionSiEsPosible()
        } else {
            cargandoUbicacion = false
        }
    }

    LaunchedEffect(Unit) {
        val tienePermisoFino = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val tienePermisoAproximado = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (tienePermisoFino || tienePermisoAproximado) {
            obtenerUbicacionSiEsPosible()
        } else {
            permisoUbicacionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            webViewActual?.apply {
                stopLoading()
                removeJavascriptInterface("Android")
                destroy()
            }
            webViewActual = null
        }
    }

    val html = remember(
        baches,
        conteoCercania,
        ubicacionAdmin
    ) {
        construirHtmlMapaAdmin(
            baches = baches,
            conteoCercania = conteoCercania,
            ubicacionAdmin = ubicacionAdmin
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8EDF2))
    ) {
        AndroidView(
            factory = { ctx ->
                WebView(ctx).apply {
                    webViewActual = this

                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.allowFileAccess = false
                    settings.allowContentAccess = false
                    settings.setSupportZoom(true)
                    settings.builtInZoomControls = true
                    settings.displayZoomControls = false

                    webViewClient = WebViewClient()

                    addJavascriptInterface(
                        MapaAdminJavascriptBridge { id ->
                            onVerDetalleActual.value(id)
                        },
                        "Android"
                    )
                }
            },
            update = { webView ->
                val nuevaFirma = html.hashCode()

                if (webView.tag != nuevaFirma) {
                    webView.tag = nuevaFirma
                    webView.loadDataWithBaseURL(
                        "https://bachapp.local/",
                        html,
                        "text/html",
                        "UTF-8",
                        null
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        if (cargandoUbicacion) {
            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 14.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = 14.dp,
                        vertical = 9.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = AzulUbicacionMapa
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Obteniendo tu ubicación...",
                        fontSize = 12.sp,
                        color = GrisMapa
                    )
                }
            }
        }

        LeyendaMapaAdmin(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(12.dp)
        )
    }
}

@Composable
private fun LeyendaMapaAdmin(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.96f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 7.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "ESTADO DE LOS REPORTES",
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NegroMapa
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    ItemLeyendaMapa(
                        color = AmarilloMapa,
                        texto = "Pendiente",
                        icono = {
                            Icon(
                                imageVector = Icons.Outlined.WarningAmber,
                                contentDescription = null,
                                tint = NegroMapa,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    )

                    ItemLeyendaMapa(
                        color = AzulProcesoMapa,
                        texto = "En proceso",
                        icono = {
                            Icon(
                                imageVector = Icons.Outlined.Construction,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    )

                    ItemLeyendaMapa(
                        color = VerdeMapa,
                        texto = "Resuelto",
                        icono = {
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    ItemLeyendaMapa(
                        color = Color.White,
                        borde = RojoMapa,
                        texto = "Zona de alta prioridad",
                        icono = {
                            Icon(
                                imageVector = Icons.Outlined.PriorityHigh,
                                contentDescription = null,
                                tint = RojoMapa,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    )

                    ItemLeyendaMapa(
                        color = AzulUbicacionMapa,
                        texto = "Mi ubicación",
                        icono = {
                            Icon(
                                imageVector = Icons.Outlined.MyLocation,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ItemLeyendaMapa(
    color: Color,
    texto: String,
    borde: Color? = null,
    icono: @Composable () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    color = borde ?: color,
                    shape = CircleShape
                )
                .padding(if (borde != null) 3.dp else 0.dp)
                .background(
                    color = color,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            icono()
        }

        Spacer(modifier = Modifier.width(7.dp))

        Text(
            text = texto,
            fontSize = 11.sp,
            color = NegroMapa
        )
    }
}

@SuppressLint("MissingPermission")
private fun obtenerUbicacionAdministrador(
    context: Context,
    onResultado: (Double, Double) -> Unit,
    onError: () -> Unit
) {
    val fusedClient = LocationServices
        .getFusedLocationProviderClient(context)

    fusedClient
        .getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        )
        .addOnSuccessListener { ubicacion ->
            if (ubicacion != null) {
                onResultado(
                    ubicacion.latitude,
                    ubicacion.longitude
                )
            } else {
                fusedClient.lastLocation
                    .addOnSuccessListener { ultima ->
                        if (ultima != null) {
                            onResultado(
                                ultima.latitude,
                                ultima.longitude
                            )
                        } else {
                            onError()
                        }
                    }
                    .addOnFailureListener {
                        onError()
                    }
            }
        }
        .addOnFailureListener {
            onError()
        }
}

private class MapaAdminJavascriptBridge(
    private val onVerDetalle: (Int) -> Unit
) {
    @JavascriptInterface
    fun verDetalle(id: Int) {
        Handler(Looper.getMainLooper()).post {
            onVerDetalle(id)
        }
    }
}

private fun construirHtmlMapaAdmin(
    baches: List<Bache>,
    conteoCercania: Map<Int, Int>,
    ubicacionAdmin: Pair<Double, Double>?
): String {
    val reportesValidos = baches.filter {
        it.latitud in -90.0..90.0 &&
                it.longitud in -180.0..180.0 &&
                !(it.latitud == 0.0 && it.longitud == 0.0)
    }

    val centroInicial = when {
        ubicacionAdmin != null -> ubicacionAdmin
        reportesValidos.isNotEmpty() ->
            reportesValidos.first().latitud to
                    reportesValidos.first().longitud
        else -> 23.6345 to -102.5528
    }

    val marcadores = buildString {
        reportesValidos.forEach { bache ->
            val estado = normalizarEstadoMapa(bache.estado)
            val prioridad = (conteoCercania[bache.id] ?: 1) >= 2
            val cercanos = conteoCercania[bache.id] ?: 1

            val color = when (estado) {
                "en proceso" -> "#2F6FA3"
                "resuelto" -> "#2F6B55"
                else -> "#FFC107"
            }

            val claseIcono = when (estado) {
                "en proceso" -> "trabajo"
                "resuelto" -> "resuelto"
                else -> "pendiente"
            }

            val estadoTexto = when (estado) {
                "en proceso" -> "En proceso"
                "resuelto" -> "Resuelto"
                else -> "Pendiente"
            }

            val descripcion = escapeJavascript(
                bache.descripcion.take(120)
            )

            append(
                """
                addReportMarker(
                    ${bache.latitud},
                    ${bache.longitud},
                    ${bache.id},
                    '$color',
                    '$claseIcono',
                    ${if (prioridad) "true" else "false"},
                    '$estadoTexto',
                    '$descripcion',
                    $cercanos
                );
                """.trimIndent()
            )
            append('\n')
        }
    }

    val marcadorUsuario = if (ubicacionAdmin != null) {
        "addUserMarker(${ubicacionAdmin.first}, ${ubicacionAdmin.second});"
    } else {
        ""
    }

    return """
        <!DOCTYPE html>
        <html lang="es">
        <head>
            <meta charset="utf-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
            <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
            <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
            <style>
                html, body, #map {
                    width: 100%;
                    height: 100%;
                    margin: 0;
                    padding: 0;
                    background: #e8edf2;
                    font-family: Arial, sans-serif;
                }
                .bach-marker {
                    width: 40px;
                    height: 40px;
                    border-radius: 50%;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    box-sizing: border-box;
                    border: 3px solid #ffffff;
                    box-shadow: 0 2px 8px rgba(0,0,0,.28);
                }
                .bach-marker.priority {
                    outline: 5px solid #B3261E;
                    outline-offset: 1px;
                }
                .bach-marker svg {
                    width: 22px;
                    height: 22px;
                }
                .user-marker {
                    width: 34px;
                    height: 34px;
                    border-radius: 50%;
                    background: #173B57;
                    border: 4px solid #ffffff;
                    box-shadow: 0 2px 9px rgba(0,0,0,.32);
                    display: flex;
                    align-items: center;
                    justify-content: center;
                }
                .user-marker::after {
                    content: '';
                    width: 9px;
                    height: 9px;
                    border-radius: 50%;
                    background: #ffffff;
                }
                .popup-title {
                    font-size: 15px;
                    font-weight: 700;
                    color: #252525;
                    margin-bottom: 6px;
                }
                .popup-row {
                    font-size: 12px;
                    color: #62676D;
                    margin: 4px 0;
                }
                .detail-btn {
                    border: none;
                    background: #FFC107;
                    color: #252525;
                    font-weight: 700;
                    border-radius: 9px;
                    padding: 8px 12px;
                    margin-top: 8px;
                    width: 100%;
                }
            </style>
        </head>
        <body>
            <div id="map"></div>
            <script>
                const map = L.map('map', {
                    zoomControl: true
                }).setView([${centroInicial.first}, ${centroInicial.second}], 14);

                L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
                    maxZoom: 19,
                    attribution: '&copy; OpenStreetMap contributors'
                }).addTo(map);

                const allMarkers = [];

                function iconSvg(type) {
                    if (type === 'resuelto') {
                        return `<svg viewBox="0 0 24 24" fill="none" stroke="#ffffff" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"><path d="M5 12.5l4.2 4.2L19 7"/></svg>`;
                    }
                    if (type === 'trabajo') {
                        return `<svg viewBox="0 0 24 24" fill="none" stroke="#ffffff" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round"><path d="M14.5 4.5l5 5-2.2 2.2-1.4-1.4-6.4 6.4 1.5 1.5-2.2 2.2-5-5 2.2-2.2 1.5 1.5 6.4-6.4-1.4-1.4z"/></svg>`;
                    }
                    return `<svg viewBox="0 0 24 24" fill="none" stroke="#252525" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 3L2.8 20h18.4L12 3z"/><path d="M12 9v5"/><path d="M12 17.4h.01"/></svg>`;
                }

                function addReportMarker(lat, lon, id, color, type, priority, status, description, nearby) {
                    const html = `<div class="bach-marker ${'$'}{priority ? 'priority' : ''}" style="background:${'$'}{color}">${'$'}{iconSvg(type)}</div>`;
                    const icon = L.divIcon({
                        className: '',
                        html: html,
                        iconSize: [46, 46],
                        iconAnchor: [23, 23]
                    });

                    const marker = L.marker([lat, lon], {icon: icon}).addTo(map);
                    marker.bindPopup(`
                        <div class="popup-title">Reporte #${'$'}{id}</div>
                        <div class="popup-row"><strong>Estado:</strong> ${'$'}{status}</div>
                        <div class="popup-row"><strong>Reportes cercanos:</strong> ${'$'}{nearby}</div>
                        <div class="popup-row">${'$'}{description || 'Sin descripción'}</div>
                        <button class="detail-btn" onclick="Android.verDetalle(${'$'}{id})">Ver detalle</button>
                    `);
                    allMarkers.push(marker);
                }

                function addUserMarker(lat, lon) {
                    const icon = L.divIcon({
                        className: '',
                        html: '<div class="user-marker"></div>',
                        iconSize: [42, 42],
                        iconAnchor: [21, 21]
                    });
                    const marker = L.marker([lat, lon], {icon: icon}).addTo(map);
                    marker.bindPopup('<div class="popup-title">Mi ubicación</div>');
                    allMarkers.push(marker);
                }

                $marcadores
                $marcadorUsuario

                if (allMarkers.length > 1) {
                    const group = L.featureGroup(allMarkers);
                    map.fitBounds(group.getBounds().pad(0.18), {maxZoom: 16});
                }
            </script>
        </body>
        </html>
    """.trimIndent()
}

private fun normalizarEstadoMapa(
    estado: String
): String {
    return estado
        .trim()
        .lowercase()
        .replace("_", " ")
}

private fun escapeJavascript(
    texto: String
): String {
    return texto
        .replace("\\", "\\\\")
        .replace("'", "\\'")
        .replace("\r", " ")
        .replace("\n", " ")
        .replace("</", "<\\/")
}
