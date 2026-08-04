package com.example.bachapp

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import org.jetbrains.compose.resources.painterResource
import bachapp.shared.generated.resources.Res
import bachapp.shared.generated.resources.ubi
import kotlinx.coroutines.launch

private val FondoReporte = Color(0xFFF5F6F8)
private val AmarilloPrincipal = Color(0xFFFFC107)
private val AmarilloOscuro = Color(0xFFE5A900)
private val AmarilloClaro = Color(0xFFFFF4C7)
private val AmarilloMuyClaro = Color(0xFFFFFAE8)
private val NegroReporte = Color(0xFF252525)
private val GrisReporte = Color(0xFF62676D)
private val GrisBordeReporte = Color(0xFFD7D9DC)
private val RojoReporte = Color(0xFFB3261E)
private val RojoClaroReporte = Color(0xFFFFEFED)
private val VerdeExitoReporte = Color(0xFF2F6B55)
private val VerdeClaroReporte = Color(0xFFE4F0EB)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaReporte(
    usuarioActualId: Int,
    onVolver: () -> Unit,
    onVerReportes: () -> Unit,
    onReporteExitoso: () -> Unit,
    onCerrarSesion: () -> Unit
) {

    var descripcion by remember {
        mutableStateOf("")
    }

    var cargando by remember {
        mutableStateOf(false)
    }

    var error by remember {
        mutableStateOf("")
    }

    var mostrarCamara by remember {
        mutableStateOf(false)
    }

    var fotoUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var latitud by remember {
        mutableStateOf(0.0)
    }

    var longitud by remember {
        mutableStateOf(0.0)
    }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

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
        containerColor = FondoReporte,

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Reportar bache",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },

                navigationIcon = {
                    IconButton(
                        onClick = onVolver
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Regresar",
                            tint = NegroReporte
                        )
                    }
                },

                actions = {
                    TextButton(
                        onClick = onCerrarSesion
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Logout,
                            contentDescription = null,
                            tint = NegroReporte,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(
                            modifier = Modifier.width(6.dp)
                        )

                        Text(
                            text = "Salir",
                            color = NegroReporte,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AmarilloPrincipal,
                    titleContentColor = NegroReporte
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 16.dp,
                    vertical = 18.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            EncabezadoNuevoReporte()

            SeccionFotoReporte(
                fotoUri = fotoUri,
                onTomarFoto = {
                    mostrarCamara = true
                }
            )

            SeccionUbicacionReporte(
                latitud = latitud,
                longitud = longitud,
                onUbicacionObtenida = { lat, lon ->
                    latitud = lat
                    longitud = lon
                }
            )

            SeccionDescripcionReporte(
                descripcion = descripcion,
                onDescripcionCambiada = {
                    descripcion = it
                }
            )

            if (error.isNotBlank()) {
                MensajeErrorReporte(
                    mensaje = error
                )
            }

            if (cargando) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = AmarilloOscuro
                    )
                }
            } else {
                Button(
                    onClick = {
                        when {
                            fotoUri == null -> {
                                error = "Debes tomar una fotografía del bache."
                            }

                            latitud == 0.0 && longitud == 0.0 -> {
                                error = "Debes obtener la ubicación del bache."
                            }

                            descripcion.isBlank() -> {
                                error = "La descripción es obligatoria."
                            }

                            else -> {
                                error = ""
                                cargando = true

                                scope.launch {
                                    try {
                                        var urlFoto = ""

                                        val uriSeleccionada = fotoUri

                                        if (uriSeleccionada != null) {
                                            val bytes = context
                                                .contentResolver
                                                .openInputStream(
                                                    uriSeleccionada
                                                )
                                                ?.use { inputStream ->
                                                    inputStream.readBytes()
                                                }

                                            if (bytes == null) {
                                                throw Exception(
                                                    "No fue posible leer la fotografía."
                                                )
                                            }

                                            urlFoto = ApiClient.subirFoto(
                                                bytes = bytes,
                                                nombreArchivo =
                                                    "bache_${System.currentTimeMillis()}.jpg"
                                            )
                                        }

                                        ApiClient.crearBache(
                                            Bache(
                                                descripcion = descripcion.trim(),
                                                latitud = latitud,
                                                longitud = longitud,
                                                fotoUrl = urlFoto,
                                                usuarioId = usuarioActualId
                                            )
                                        )

                                        cargando = false
                                        descripcion = ""
                                        fotoUri = null
                                        latitud = 0.0
                                        longitud = 0.0

                                        onReporteExitoso()

                                    } catch (e: Exception) {
                                        error = "No fue posible enviar el reporte: ${
                                            e.message ?: "Error desconocido"
                                        }"

                                        cargando = false
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmarilloPrincipal,
                        contentColor = NegroReporte
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Send,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(9.dp)
                    )

                    Text(
                        text = "Enviar reporte",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onVerReportes,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = NegroReporte
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.5.dp,
                        color = AmarilloOscuro
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ListAlt,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(9.dp)
                    )

                    Text(
                        text = "Ver reportes creados",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )
        }
    }
}

@Composable
private fun EncabezadoNuevoReporte() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(
                    resource = Res.drawable.ubi
                ),
                contentDescription = "Mapa para ubicar reportes de baches",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 24.dp,
                            topEnd = 24.dp
                        )
                    ),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 22.dp,
                        vertical = 22.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "NUEVO REPORTE",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 27.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NegroReporte,
                    textAlign = TextAlign.Center
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Registra un bache para ayudar a mejorar la seguridad vial de tu comunidad.",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = GrisReporte,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun SeccionFotoReporte(
    fotoUri: Uri?,
    onTomarFoto: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            TituloSeccionReporte(
                titulo = "Fotografía del bache",
                icono = {
                    Icon(
                        imageVector = Icons.Outlined.CameraAlt,
                        contentDescription = null,
                        tint = AmarilloOscuro
                    )
                }
            )

            if (fotoUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = fotoUri
                    ),
                    contentDescription = "Fotografía tomada",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                        .clip(RoundedCornerShape(15.dp)),
                    contentScale = ContentScale.Crop
                )

                OutlinedButton(
                    onClick = onTomarFoto,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(13.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = NegroReporte
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.5.dp,
                        color = AmarilloOscuro
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CameraAlt,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Text(
                        text = "Tomar otra fotografía",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(145.dp)
                        .background(
                            color = AmarilloMuyClaro,
                            shape = RoundedCornerShape(15.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CameraAlt,
                            contentDescription = null,
                            tint = AmarilloOscuro,
                            modifier = Modifier.size(42.dp)
                        )

                        Spacer(
                            modifier = Modifier.height(9.dp)
                        )

                        Text(
                            text = "Aún no has tomado una fotografía",
                            color = GrisReporte,
                            fontSize = 13.sp
                        )
                    }
                }

                Button(
                    onClick = onTomarFoto,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(13.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmarilloPrincipal,
                        contentColor = NegroReporte
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CameraAlt,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Text(
                        text = "Tomar fotografía",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun SeccionUbicacionReporte(
    latitud: Double,
    longitud: Double,
    onUbicacionObtenida: (Double, Double) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            TituloSeccionReporte(
                titulo = "Ubicación del bache",
                icono = {
                    Icon(
                        imageVector = Icons.Outlined.MyLocation,
                        contentDescription = null,
                        tint = AmarilloOscuro
                    )
                }
            )

            /*
             * Este es el componente de ubicación que ya tienes creado.
             */
            BotonObtenerUbicacion(
                onUbicacionObtenida = onUbicacionObtenida
            )

            if (latitud != 0.0 || longitud != 0.0) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = VerdeClaroReporte
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CheckCircle,
                                contentDescription = null,
                                tint = VerdeExitoReporte
                            )

                            Spacer(
                                modifier = Modifier.width(8.dp)
                            )

                            Text(
                                text = "Ubicación obtenida",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = VerdeExitoReporte
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(9.dp)
                        )

                        Text(
                            text = "Latitud: $latitud",
                            fontSize = 13.sp,
                            color = GrisReporte
                        )

                        Text(
                            text = "Longitud: $longitud",
                            fontSize = 13.sp,
                            color = GrisReporte
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SeccionDescripcionReporte(
    descripcion: String,
    onDescripcionCambiada: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            TituloSeccionReporte(
                titulo = "Descripción del bache",
                icono = {
                    Icon(
                        imageVector = Icons.Outlined.Description,
                        contentDescription = null,
                        tint = AmarilloOscuro
                    )
                }
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = onDescripcionCambiada,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Describe la ubicación, el tamaño y el estado del bache...",
                        color = GrisReporte
                    )
                },
                minLines = 4,
                maxLines = 7,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AmarilloOscuro,
                    unfocusedBorderColor = GrisBordeReporte,
                    cursorColor = AmarilloOscuro,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Text(
                text = "${descripcion.length} caracteres",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                fontSize = 11.sp,
                color = GrisReporte
            )
        }
    }
}

@Composable
private fun TituloSeccionReporte(
    titulo: String,
    icono: @Composable () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(
                    color = AmarilloMuyClaro,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            icono()
        }

        Spacer(
            modifier = Modifier.width(11.dp)
        )

        Text(
            text = titulo,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = NegroReporte
        )
    }

    HorizontalDivider(
        color = GrisBordeReporte
    )
}

@Composable
private fun MensajeErrorReporte(
    mensaje: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = RojoClaroReporte
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.ErrorOutline,
                contentDescription = null,
                tint = RojoReporte
            )

            Spacer(
                modifier = Modifier.width(9.dp)
            )

            Text(
                text = mensaje,
                color = RojoReporte,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}