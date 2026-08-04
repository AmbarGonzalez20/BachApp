package com.example.bachapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AddLocationAlt
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.ReportProblem
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

private val FondoLista = Color(0xFFF5F6F8)
private val AmarilloLista = Color(0xFFFFC107)
private val AmarilloOscuroLista = Color(0xFFE5A900)
private val AmarilloClaroLista = Color(0xFFFFF4C7)
private val AmarilloMuyClaroLista = Color(0xFFFFFAE8)
private val NegroLista = Color(0xFF252525)
private val GrisTextoLista = Color(0xFF62676D)
private val GrisBordeLista = Color(0xFFE0E0E0)
private val RojoLista = Color(0xFF9B2C2C)
private val RojoClaroLista = Color(0xFFFFECEA)
private val AzulProcesoLista = Color(0xFF2F6FA3)
private val AzulProcesoClaroLista = Color(0xFFE4F0FA)
private val VerdeResueltoLista = Color(0xFF2F6B55)
private val VerdeResueltoClaroLista = Color(0xFFE4F0EB)

@Composable
fun PantallaLista(
    onReportar: () -> Unit,
    onVerDetalle: (Int) -> Unit,
    onVolverInicio: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    var baches by remember {
        mutableStateOf<List<Bache>>(emptyList())
    }

    var cargando by remember {
        mutableStateOf(true)
    }

    var error by remember {
        mutableStateOf("")
    }

    var numeroActualizacion by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(numeroActualizacion) {
        cargando = true
        error = ""

        try {
            baches = ApiClient.obtenerBaches()
        } catch (e: Exception) {
            error = e.message
                ?: "No fue posible obtener los reportes."
        } finally {
            cargando = false
        }
    }

    Scaffold(
        containerColor = FondoLista,

        floatingActionButton = {
            FloatingActionButton(
                onClick = onReportar,
                containerColor = AmarilloLista,
                contentColor = NegroLista,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Registrar un nuevo bache",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            EncabezadoMisReportes(
                onVolverInicio = onVolverInicio,
                onActualizar = {
                    numeroActualizacion++
                },
                onCerrarSesion = onCerrarSesion,
                cargando = cargando
            )

            when {

                cargando -> {
                    EstadoCargandoLista(
                        modifier = Modifier.fillMaxSize()
                    )
                }

                error.isNotBlank() -> {
                    EstadoErrorLista(
                        mensaje = error,
                        onReintentar = {
                            numeroActualizacion++
                        },
                        onVolverInicio = onVolverInicio,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                baches.isEmpty() -> {
                    EstadoListaVacia(
                        onReportar = onReportar,
                        onVolverInicio = onVolverInicio,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .navigationBarsPadding(),
                        contentPadding = PaddingValues(
                            start = 18.dp,
                            end = 18.dp,
                            top = 19.dp,
                            bottom = 105.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        item {
                            ResumenLista(
                                totalReportes = baches.size,
                                onReportar = onReportar
                            )
                        }

                        items(
                            items = baches.sortedByDescending {
                                it.id
                            },
                            key = {
                                it.id
                            }
                        ) { bache ->

                            CardBache(
                                bache = bache,
                                onClick = {
                                    onVerDetalle(bache.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EncabezadoMisReportes(
    onVolverInicio: () -> Unit,
    onActualizar: () -> Unit,
    onCerrarSesion: () -> Unit,
    cargando: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = AmarilloLista,
                shape = RoundedCornerShape(
                    bottomStart = 32.dp,
                    bottomEnd = 32.dp
                )
            )
            .padding(
                start = 10.dp,
                end = 10.dp,
                top = 12.dp,
                bottom = 28.dp
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onVolverInicio
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Volver al inicio",
                    tint = NegroLista
                )
            }

            Row {
                IconButton(
                    onClick = onActualizar,
                    enabled = !cargando
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = "Actualizar reportes",
                        tint = NegroLista
                    )
                }

                IconButton(
                    onClick = onCerrarSesion
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Logout,
                        contentDescription = "Cerrar sesión",
                        tint = NegroLista
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 55.dp,
                    end = 55.dp,
                    top = 51.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector = Icons.Outlined.Assignment,
                contentDescription = null,
                tint = NegroLista,
                modifier = Modifier.size(34.dp)
            )

            Spacer(
                modifier = Modifier.height(9.dp)
            )

            Text(
                text = "Mis reportes",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NegroLista,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Consulta y da seguimiento a los baches registrados",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = NegroLista.copy(alpha = 0.78f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ResumenLista(
    totalReportes: Int,
    onReportar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = AmarilloClaroLista
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            /*
             * Se quitó el ícono de herramientas.
             */
            Text(
                text = if (totalReportes == 1) {
                    "1 reporte registrado"
                } else {
                    "$totalReportes reportes registrados"
                },
                modifier = Modifier.fillMaxWidth(),
                fontSize = 21.sp,
                lineHeight = 27.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NegroLista,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            Text(
                text = "Selecciona un reporte para consultar toda su información.",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = GrisTextoLista,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(19.dp)
            )

            Button(
                onClick = onReportar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NegroLista,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.AddLocationAlt,
                    contentDescription = null
                )

                Spacer(
                    modifier = Modifier.width(9.dp)
                )

                Text(
                    text = "Registrar un nuevo bache",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun CardBache(
    bache: Bache,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        onClick = onClick
    ) {
        Column {

            val urlFoto = obtenerUrlFotoLista(
                bache.fotoUrl
            )

            val estadoNormalizado = bache.estado
                .trim()
                .lowercase()
                .replace("_", " ")

            val textoEstado = when (estadoNormalizado) {
                "pendiente" -> "Pendiente"
                "en proceso" -> "En proceso"
                "resuelto" -> "Resuelto"
                else -> "Pendiente"
            }

            val colorEstado = when (estadoNormalizado) {
                "pendiente" -> AmarilloOscuroLista
                "en proceso" -> AzulProcesoLista
                "resuelto" -> VerdeResueltoLista
                else -> GrisTextoLista
            }

            val fondoEstado = when (estadoNormalizado) {
                "pendiente" -> AmarilloMuyClaroLista
                "en proceso" -> AzulProcesoClaroLista
                "resuelto" -> VerdeResueltoClaroLista
                else -> Color(0xFFF0F1F2)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(205.dp)
            ) {

                if (urlFoto.isNotBlank()) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = urlFoto
                        ),
                        contentDescription = "Fotografía del bache",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(
                                RoundedCornerShape(
                                    topStart = 22.dp,
                                    topEnd = 22.dp
                                )
                            ),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                AmarilloMuyClaroLista
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ReportProblem,
                            contentDescription = null,
                            tint = AmarilloOscuroLista,
                            modifier = Modifier.size(54.dp)
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(13.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = fondoEstado
                    )
                ) {
                    Text(
                        text = textoEstado,
                        modifier = Modifier.padding(
                            horizontal = 13.dp,
                            vertical = 7.dp
                        ),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = colorEstado
                    )
                }
            }

            Column(
                modifier = Modifier.padding(18.dp)
            ) {

                Text(
                    text = "Reporte #${bache.id}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 19.sp,
                    color = NegroLista
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = "Reporte ciudadano",
                    fontSize = 13.sp,
                    color = GrisTextoLista
                )

                Spacer(
                    modifier = Modifier.height(15.dp)
                )

                HorizontalDivider(
                    color = GrisBordeLista
                )

                Spacer(
                    modifier = Modifier.height(15.dp)
                )

                Text(
                    text = "Descripción",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = GrisTextoLista
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = bache.descripcion.ifBlank {
                        "El reporte no contiene una descripción."
                    },
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = NegroLista,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Row(
                    verticalAlignment = Alignment.Top
                ) {

                    Box(
                        modifier = Modifier
                            .size(39.dp)
                            .background(
                                color = AmarilloClaroLista,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = null,
                            tint = NegroLista,
                            modifier = Modifier.size(21.dp)
                        )
                    }

                    Spacer(
                        modifier = Modifier.width(11.dp)
                    )

                    Column {
                        Text(
                            text = "Ubicación registrada",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = NegroLista
                        )

                        Spacer(
                            modifier = Modifier.height(3.dp)
                        )

                        Text(
                            text = "Latitud: ${bache.latitud}",
                            fontSize = 12.sp,
                            color = GrisTextoLista
                        )

                        Text(
                            text = "Longitud: ${bache.longitud}",
                            fontSize = 12.sp,
                            color = GrisTextoLista
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                Button(
                    onClick = onClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(49.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmarilloLista,
                        contentColor = NegroLista
                    )
                ) {
                    Text(
                        text = "Ver detalle",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(
                        modifier = Modifier.width(7.dp)
                    )

                    Icon(
                        imageVector = Icons.Outlined.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun EstadoCargandoLista(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = AmarilloOscuroLista
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "Consultando reportes...",
                color = GrisTextoLista,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun EstadoListaVacia(
    onReportar: () -> Unit,
    onVolverInicio: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(23.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(23.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .background(
                            color = AmarilloClaroLista,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Assignment,
                        contentDescription = null,
                        tint = NegroLista,
                        modifier = Modifier.size(38.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.height(19.dp)
                )

                Text(
                    text = "Aún no hay reportes",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NegroLista,
                    textAlign = TextAlign.Center
                )

                Spacer(
                    modifier = Modifier.height(9.dp)
                )

                Text(
                    text = "Cuando registres un bache, podrás consultarlo y darle seguimiento desde esta sección.",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = GrisTextoLista,
                    textAlign = TextAlign.Center
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Button(
                    onClick = onReportar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(51.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmarilloLista,
                        contentColor = NegroLista
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AddLocationAlt,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(9.dp)
                    )

                    Text(
                        text = "Registrar el primer bache",
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                OutlinedButton(
                    onClick = onVolverInicio,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = GrisBordeLista
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = NegroLista
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Home,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(9.dp)
                    )

                    Text(
                        text = "Volver al inicio",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun EstadoErrorLista(
    mensaje: String,
    onReintentar: () -> Unit,
    onVolverInicio: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(23.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(23.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(
                            color = RojoClaroLista,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ReportProblem,
                        contentDescription = null,
                        tint = RojoLista,
                        modifier = Modifier.size(35.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                Text(
                    text = "No fue posible cargar los reportes",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 20.sp,
                    lineHeight = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NegroLista,
                    textAlign = TextAlign.Center
                )

                Spacer(
                    modifier = Modifier.height(9.dp)
                )

                Text(
                    text = mensaje,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = GrisTextoLista,
                    textAlign = TextAlign.Center
                )

                Spacer(
                    modifier = Modifier.height(22.dp)
                )

                Button(
                    onClick = onReintentar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(51.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmarilloLista,
                        contentColor = NegroLista
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(9.dp)
                    )

                    Text(
                        text = "Intentar nuevamente",
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(
                    modifier = Modifier.height(11.dp)
                )

                OutlinedButton(
                    onClick = onVolverInicio,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = GrisBordeLista
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = NegroLista
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Home,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(9.dp)
                    )

                    Text(
                        text = "Volver al inicio",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

private fun obtenerUrlFotoLista(
    fotoUrl: String
): String {
    val ruta = fotoUrl.trim()

    return when {

        ruta.isBlank() -> ""

        ruta.startsWith(
            "http://",
            ignoreCase = true
        ) -> ruta

        ruta.startsWith(
            "https://",
            ignoreCase = true
        ) -> ruta

        ruta.startsWith("/") ->
            "https://backend-production-ad16.up.railway.app$ruta"

        else ->
            "https://backend-production-ad16.up.railway.app/$ruta"
    }
}

private fun formatearEstadoLista(
    estado: String
): String {
    return when (
        estado.lowercase().trim()
    ) {
        "pendiente" -> "Pendiente"
        "en_proceso" -> "En proceso"
        "en proceso" -> "En proceso"
        "resuelto" -> "Resuelto"

        else -> estado.replaceFirstChar {
            it.uppercase()
        }
    }
}