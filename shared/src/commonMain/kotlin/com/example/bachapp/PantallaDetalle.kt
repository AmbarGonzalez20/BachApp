package com.example.bachapp

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
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.ImageNotSupported
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

/*
 * Colores de temática vial.
 */
private val FondoDetalle = Color(0xFFF5F6F8)
private val AmarilloDetalle = Color(0xFFFFC107)
private val AmarilloOscuroDetalle = Color(0xFFE5A900)
private val AmarilloClaroDetalle = Color(0xFFFFF4C7)
private val AmarilloMuyClaroDetalle = Color(0xFFFFFAE8)
private val NegroDetalle = Color(0xFF252525)
private val GrisDetalle = Color(0xFF62676D)
private val GrisBordeDetalle = Color(0xFFE0E0E0)
private val RojoDetalle = Color(0xFFB3261E)
private val VerdeEstadoDetalle = Color(0xFF2F6B55)
private val VerdeClaroDetalle = Color(0xFFE4F0EB)

private const val URL_SERVIDOR =
    "https://backend-production-ad16.up.railway.app"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalle(
    bacheId: Int,
    usuarioActualId: Int,
    onVolver: () -> Unit
) {

    var bache by remember {
        mutableStateOf<Bache?>(null)
    }

    var cargando by remember {
        mutableStateOf(true)
    }

    var error by remember {
        mutableStateOf("")
    }

    val evidenciaLocal = recordarEvidenciaResolucionLocal(
        bacheId = bacheId
    )

    LaunchedEffect(bacheId) {
        cargando = true
        error = ""

        try {
            bache = ApiClient.obtenerBachePorId(bacheId)
        } catch (e: Exception) {
            error = "No fue posible cargar el reporte: ${
                e.message ?: "Error desconocido"
            }"
        } finally {
            cargando = false
        }
    }

    Scaffold(
        containerColor = FondoDetalle,

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detalle del reporte",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp
                    )
                },

                navigationIcon = {
                    IconButton(
                        onClick = onVolver
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Regresar",
                            tint = NegroDetalle
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AmarilloDetalle,
                    titleContentColor = NegroDetalle,
                    navigationIconContentColor = NegroDetalle
                )
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            when {

                cargando -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = AmarilloOscuroDetalle
                    )
                }

                error.isNotBlank() -> {
                    Card(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(20.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFEFED)
                        )
                    ) {
                        Text(
                            text = error,
                            color = RojoDetalle,
                            modifier = Modifier.padding(18.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                bache == null -> {
                    Text(
                        text = "Reporte no encontrado",
                        modifier = Modifier.align(Alignment.Center),
                        color = GrisDetalle,
                        fontSize = 16.sp
                    )
                }

                else -> {
                    val reporte = bache!!

                    /*
                     * Permite mostrar una URL completa o una ruta relativa
                     * enviada por Railway.
                     */
                    val urlFoto = obtenerUrlFotoDetalle(
                        reporte.fotoUrl
                    )

                    val fotoResolucion = obtenerUrlFotoDetalle(
                        reporte.fotoResolucionUrl.ifBlank {
                            evidenciaLocal?.fotoUrl.orEmpty()
                        }
                    )

                    val comentarioResolucion =
                        reporte.comentarioResolucion.ifBlank {
                            evidenciaLocal?.comentario.orEmpty()
                        }

                    val fechaResolucion =
                        reporte.fechaResolucion.ifBlank {
                            evidenciaLocal?.fecha.orEmpty()
                        }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .navigationBarsPadding()
                            .verticalScroll(rememberScrollState())
                            .padding(bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        /*
                         * Fotografía grande.
                         *
                         * No tiene padding lateral para ocupar prácticamente
                         * todo el ancho de la pantalla.
                         */
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 12.dp,
                                    end = 12.dp,
                                    top = 14.dp
                                ),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 6.dp
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(390.dp)
                            ) {

                                if (urlFoto.isNotBlank()) {
                                    Image(
                                        painter = rememberAsyncImagePainter(
                                            model = urlFoto
                                        ),
                                        contentDescription =
                                            "Fotografía grande del bache",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(
                                                RoundedCornerShape(24.dp)
                                            ),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                AmarilloMuyClaroDetalle
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment =
                                                Alignment.CenterHorizontally
                                        ) {
                                            Icon(
                                                imageVector =
                                                    Icons.Outlined.ImageNotSupported,
                                                contentDescription = null,
                                                tint = AmarilloOscuroDetalle,
                                                modifier = Modifier.size(56.dp)
                                            )

                                            Spacer(
                                                modifier = Modifier.height(12.dp)
                                            )

                                            Text(
                                                text =
                                                    "No hay fotografía disponible",
                                                color = GrisDetalle,
                                                fontSize = 15.sp
                                            )
                                        }
                                    }
                                }

                                /*
                                 * Etiqueta sobre la fotografía.
                                 */
                                Card(
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(14.dp),
                                    shape = RoundedCornerShape(50.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor =
                                            AmarilloDetalle.copy(
                                                alpha = 0.94f
                                            )
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(
                                            horizontal = 14.dp,
                                            vertical = 8.dp
                                        ),
                                        verticalAlignment =
                                            Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector =
                                                Icons.Outlined.Construction,
                                            contentDescription = null,
                                            tint = NegroDetalle,
                                            modifier = Modifier.size(20.dp)
                                        )

                                        Spacer(
                                            modifier = Modifier.width(7.dp)
                                        )

                                        Text(
                                            text = "REPORTE VIAL",
                                            color = NegroDetalle,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }

                        /*
                         * Contenido inferior.
                         */
                        Column(
                            modifier = Modifier.padding(
                                horizontal = 16.dp
                            ),
                            verticalArrangement =
                                Arrangement.spacedBy(15.dp)
                        ) {

                            /*
                             * Estado del reporte.
                             */
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = AmarilloClaroDetalle
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment =
                                        Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(46.dp)
                                            .background(
                                                color = AmarilloDetalle,
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector =
                                                Icons.Outlined.Construction,
                                            contentDescription = null,
                                            tint = NegroDetalle
                                        )
                                    }

                                    Spacer(
                                        modifier = Modifier.width(12.dp)
                                    )

                                    Column {
                                        Text(
                                            text = "Estado del reporte",
                                            fontSize = 12.sp,
                                            color = GrisDetalle
                                        )

                                        Spacer(
                                            modifier = Modifier.height(3.dp)
                                        )

                                        Text(
                                            text = formatearEstado(
                                                reporte.estado
                                            ),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = NegroDetalle
                                        )
                                    }
                                }
                            }
                            /*
                             * Seguimiento visual del reporte.
                             * Reportado -> En proceso -> Resuelto.
                             */
                            SeguimientoReporte(
                                estado = reporte.estado
                            )

                            if (
                                reporte.estado
                                    .trim()
                                    .lowercase()
                                    .replace("_", " ") == "resuelto" &&
                                fotoResolucion.isNotBlank()
                            ) {
                                CardEvidenciaResolucionDetalle(
                                    fotoAntes = urlFoto,
                                    fotoDespues = fotoResolucion,
                                    comentario = comentarioResolucion,
                                    fecha = fechaResolucion
                                )
                            }

                            /*
                             * Descripción.
                             */
                            CardInformacionVial(

                                titulo = "Descripción",
                                icono = {
                                    Icon(
                                        imageVector =
                                            Icons.Outlined.Description,
                                        contentDescription = null,
                                        tint = NegroDetalle
                                    )
                                }
                            ) {
                                Text(
                                    text = reporte.descripcion.ifBlank {
                                        "No se agregó una descripción."
                                    },
                                    fontSize = 15.sp,
                                    lineHeight = 22.sp,
                                    color = NegroDetalle
                                )
                            }

                            /*
                             * Ubicación.
                             */
                            CardInformacionVial(
                                titulo = "Ubicación",
                                icono = {
                                    Icon(
                                        imageVector =
                                            Icons.Outlined.LocationOn,
                                        contentDescription = null,
                                        tint = NegroDetalle
                                    )
                                }
                            ) {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor =
                                            AmarilloMuyClaroDetalle
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.padding(14.dp)
                                    ) {
                                        Text(
                                            text = "Latitud",
                                            fontSize = 12.sp,
                                            color = GrisDetalle
                                        )

                                        Text(
                                            text = reporte.latitud.toString(),
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = NegroDetalle
                                        )

                                        Spacer(
                                            modifier = Modifier.height(12.dp)
                                        )

                                        HorizontalDivider(
                                            color = GrisBordeDetalle
                                        )

                                        Spacer(
                                            modifier = Modifier.height(12.dp)
                                        )

                                        Text(
                                            text = "Longitud",
                                            fontSize = 12.sp,
                                            color = GrisDetalle
                                        )

                                        Text(
                                            text = reporte.longitud.toString(),
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = NegroDetalle
                                        )
                                    }
                                }
                            }

                            /*
                             * Fecha.
                             */
                            if (reporte.fechaReporte.isNotBlank()) {
                                CardInformacionVial(
                                    titulo = "Fecha del reporte",
                                    icono = {
                                        Icon(
                                            imageVector =
                                                Icons.Outlined.CalendarMonth,
                                            contentDescription = null,
                                            tint = NegroDetalle
                                        )
                                    }
                                ) {
                                    Text(
                                        text = reporte.fechaReporte,
                                        fontSize = 15.sp,
                                        color = NegroDetalle
                                    )
                                }
                            }

                            /*
                             * Botón regresar.
                             */
                            Button(
                                onClick = onVolver,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(58.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AmarilloDetalle,
                                    contentColor = NegroDetalle
                                )
                            ) {
                                Icon(
                                    imageVector =
                                        Icons.Outlined.ArrowBack,
                                    contentDescription = null
                                )

                                Spacer(
                                    modifier = Modifier.width(9.dp)
                                )

                                Text(
                                    text = "Regresar a mis reportes",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun CardEvidenciaResolucionDetalle(
    fotoAntes: String,
    fotoDespues: String,
    comentario: String,
    fecha: String
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            color = VerdeClaroDetalle,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CheckCircle,
                        contentDescription = null,
                        tint = VerdeEstadoDetalle
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "Evidencia de resolución",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = NegroDetalle
                    )

                    Text(
                        text = "Comparación antes y después",
                        fontSize = 12.sp,
                        color = GrisDetalle
                    )
                }
            }

            FotoComparacionDetalle(
                etiqueta = "ANTES",
                fotoUrl = fotoAntes
            )

            FotoComparacionDetalle(
                etiqueta = "DESPUÉS",
                fotoUrl = fotoDespues
            )

            if (comentario.isNotBlank()) {
                Column {
                    Text(
                        text = "Observación de atención",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = GrisDetalle
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = comentario,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = NegroDetalle
                    )
                }
            }

            if (fecha.isNotBlank()) {
                Text(
                    text = "Fecha de resolución: $fecha",
                    fontSize = 12.sp,
                    color = GrisDetalle
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = VerdeClaroDetalle
                )
            ) {
                Text(
                    text = "Tu reporte fue atendido correctamente. Gracias por contribuir a mejorar las vialidades de tu comunidad.",
                    modifier = Modifier.padding(14.dp),
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = VerdeEstadoDetalle,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun FotoComparacionDetalle(
    etiqueta: String,
    fotoUrl: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = etiqueta,
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            color = GrisDetalle
        )

        Spacer(modifier = Modifier.height(6.dp))

        if (fotoUrl.isNotBlank()) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = fotoUrl
                ),
                contentDescription = "Fotografía $etiqueta del reporte",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .clip(RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(
                        color = Color(0xFFF0F2F4),
                        shape = RoundedCornerShape(15.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.ImageNotSupported,
                    contentDescription = null,
                    tint = GrisDetalle
                )
            }
        }
    }
}

@Composable
private fun SeguimientoReporte(
    estado: String
) {
    val estadoNormalizado = estado
        .trim()
        .lowercase()
        .replace("_", " ")

    val pasoActual = when (estadoNormalizado) {
        "resuelto" -> 3
        "en proceso" -> 2
        else -> 1
    }

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
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = "Seguimiento del reporte",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NegroDetalle,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = when (pasoActual) {
                    1 -> "Tu reporte fue recibido y está pendiente de atención."
                    2 -> "Tu reporte ya está siendo atendido."
                    else -> "Tu reporte fue atendido y marcado como resuelto."
                },
                modifier = Modifier.fillMaxWidth(),
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = GrisDetalle,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                PasoSeguimiento(
                    modifier = Modifier.weight(1f),
                    numero = 1,
                    titulo = "Reportado",
                    activo = pasoActual >= 1,
                    completado = pasoActual > 1
                )

                LineaSeguimiento(
                    activa = pasoActual >= 2,
                    modifier = Modifier
                        .weight(0.55f)
                        .padding(top = 17.dp)
                )

                PasoSeguimiento(
                    modifier = Modifier.weight(1f),
                    numero = 2,
                    titulo = "En proceso",
                    activo = pasoActual >= 2,
                    completado = pasoActual > 2
                )

                LineaSeguimiento(
                    activa = pasoActual >= 3,
                    modifier = Modifier
                        .weight(0.55f)
                        .padding(top = 17.dp)
                )

                PasoSeguimiento(
                    modifier = Modifier.weight(1f),
                    numero = 3,
                    titulo = "Resuelto",
                    activo = pasoActual >= 3,
                    completado = pasoActual >= 3
                )
            }

            if (pasoActual == 3) {
                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = VerdeClaroDetalle
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint = VerdeEstadoDetalle,
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(
                            modifier = Modifier.width(10.dp)
                        )

                        Column {
                            Text(
                                text = "Reporte atendido",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = VerdeEstadoDetalle
                            )

                            Text(
                                text = "Gracias por contribuir a mejorar las vialidades de tu comunidad.",
                                fontSize = 12.sp,
                                lineHeight = 17.sp,
                                color = GrisDetalle
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PasoSeguimiento(
    modifier: Modifier,
    numero: Int,
    titulo: String,
    activo: Boolean,
    completado: Boolean
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    color = if (activo) {
                        if (completado) {
                            VerdeEstadoDetalle
                        } else {
                            AmarilloDetalle
                        }
                    } else {
                        Color(0xFFE5E7E9)
                    },
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (completado) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            } else {
                Text(
                    text = numero.toString(),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (activo) {
                        NegroDetalle
                    } else {
                        GrisDetalle
                    }
                )
            }
        }

        Spacer(
            modifier = Modifier.height(7.dp)
        )

        Text(
            text = titulo,
            modifier = Modifier.fillMaxWidth(),
            fontSize = 11.sp,
            fontWeight = if (activo) {
                FontWeight.ExtraBold
            } else {
                FontWeight.Medium
            },
            color = if (activo) {
                NegroDetalle
            } else {
                GrisDetalle
            },
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LineaSeguimiento(
    activa: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(4.dp)
            .background(
                color = if (activa) {
                    VerdeEstadoDetalle
                } else {
                    Color(0xFFE5E7E9)
                },
                shape = RoundedCornerShape(50.dp)
            )
    )
}

@Composable
private fun CardInformacionVial(
    titulo: String,
    icono: @Composable () -> Unit,
    contenido: @Composable () -> Unit
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
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(43.dp)
                        .background(
                            color = AmarilloDetalle,
                            shape = RoundedCornerShape(13.dp)
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
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = NegroDetalle
                )
            }

            Spacer(
                modifier = Modifier.height(13.dp)
            )

            HorizontalDivider(
                color = GrisBordeDetalle
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            contenido()
        }
    }
}

/*
 * Convierte la ruta de la fotografía en una URL completa.
 */
private fun obtenerUrlFotoDetalle(
    fotoUrl: String
): String {

    if (fotoUrl.isBlank()) {
        return ""
    }

    return if (
        fotoUrl.startsWith(
            "http",
            ignoreCase = true
        )
    ) {
        fotoUrl
    } else {
        val ruta = if (fotoUrl.startsWith("/")) {
            fotoUrl
        } else {
            "/$fotoUrl"
        }

        "$URL_SERVIDOR$ruta"
    }
}

private fun formatearEstado(
    estado: String
): String {
    return when (
        estado.lowercase()
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