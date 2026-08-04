package com.example.bachapp

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.ImageNotSupported
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch

private val FondoDetalleAdmin = Color(0xFFF4F6F8)
private val AzulDetalleAdmin = Color(0xFF173B57)
private val VerdeDetalleAdmin = Color(0xFF2F6B55)
private val GrisDetalleAdmin = Color(0xFF5F6872)
private val AmarilloDetalleAdmin = Color(0xFFE6A700)
private val AzulProcesoDetalleAdmin = Color(0xFF2F6FA3)
private val FondoFotoAdmin = Color(0xFFE8EEF5)
private val RojoDetalleAdmin = Color(0xFFB3261E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleAdmin(
    bacheId: Int,
    onVolver: () -> Unit
) {
    var bache by remember {
        mutableStateOf<Bache?>(null)
    }

    var cargando by remember {
        mutableStateOf(true)
    }

    var actualizandoEstado by remember {
        mutableStateOf(false)
    }

    var error by remember {
        mutableStateOf("")
    }

    var mensajeExito by remember {
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()

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

    fun cambiarEstado(nuevoEstado: String) {
        if (actualizandoEstado) {
            return
        }

        scope.launch {
            actualizandoEstado = true
            error = ""
            mensajeExito = ""

            try {
                ApiClient.actualizarEstado(
                    id = bacheId,
                    estado = nuevoEstado
                )

                /*
                 * Volvemos a consultar el reporte desde Railway.
                 * Así la pantalla solo muestra el estado que realmente
                 * quedó guardado en la base de datos.
                 */
                val reporteGuardado =
                    ApiClient.obtenerBachePorId(bacheId)

                val estadoEsperado = nuevoEstado
                    .trim()
                    .lowercase()
                    .replace("_", " ")

                val estadoRecibido = reporteGuardado.estado
                    .trim()
                    .lowercase()
                    .replace("_", " ")

                if (estadoRecibido != estadoEsperado) {
                    throw IllegalStateException(
                        "El servidor respondió correctamente, pero " +
                                "el estado no quedó guardado."
                    )
                }

                bache = reporteGuardado

                mensajeExito =
                    "El estado se guardó correctamente."

            } catch (e: Exception) {
                error = "No fue posible actualizar el estado: ${
                    e.message ?: "Error desconocido"
                }"
            } finally {
                actualizandoEstado = false
            }
        }
    }

    Scaffold(
        containerColor = FondoDetalleAdmin,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detalle del reporte",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onVolver,
                        enabled = !actualizandoEstado
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AzulDetalleAdmin,
                    titleContentColor = Color.White
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
                        color = VerdeDetalleAdmin
                    )
                }

                bache == null && error.isNotEmpty() -> {
                    Text(
                        text = error,
                        color = RojoDetalleAdmin,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(20.dp)
                    )
                }

                bache == null -> {
                    Text(
                        text = "Reporte no encontrado",
                        modifier = Modifier.align(Alignment.Center),
                        color = GrisDetalleAdmin
                    )
                }

                else -> {
                    val reporte = bache!!

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(
                                horizontal = 16.dp,
                                vertical = 18.dp
                            ),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {

                        /*
                         * FOTOGRAFÍA REAL DEL REPORTE
                         *
                         * El administrador ve exactamente la
                         * fotografía tomada por el ciudadano.
                         */
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
                            if (reporte.fotoUrl.isNotBlank()) {
                                Image(
                                    painter = rememberAsyncImagePainter(
                                        model = reporte.fotoUrl
                                    ),
                                    contentDescription =
                                        "Fotografía del bache reportado",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(280.dp)
                                        .clip(
                                            RoundedCornerShape(20.dp)
                                        ),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(280.dp)
                                        .background(FondoFotoAdmin),
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
                                            tint = GrisDetalleAdmin
                                        )

                                        Spacer(
                                            modifier = Modifier.height(8.dp)
                                        )

                                        Text(
                                            text =
                                                "No hay fotografía disponible",
                                            color = GrisDetalleAdmin,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }

                        EstadoActualAdmin(
                            estado = reporte.estado
                        )

                        CardInformacionAdmin(
                            titulo = "Descripción",
                            icono = {
                                Icon(
                                    imageVector =
                                        Icons.Outlined.Description,
                                    contentDescription = null,
                                    tint = VerdeDetalleAdmin
                                )
                            }
                        ) {
                            Text(
                                text = reporte.descripcion,
                                fontSize = 15.sp,
                                color = Color(0xFF333333)
                            )
                        }

                        CardInformacionAdmin(
                            titulo = "Ubicación",
                            icono = {
                                Icon(
                                    imageVector =
                                        Icons.Outlined.LocationOn,
                                    contentDescription = null,
                                    tint = VerdeDetalleAdmin
                                )
                            }
                        ) {
                            Text(
                                text = "Latitud: ${reporte.latitud}",
                                fontSize = 14.sp,
                                color = GrisDetalleAdmin
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text = "Longitud: ${reporte.longitud}",
                                fontSize = 14.sp,
                                color = GrisDetalleAdmin
                            )
                        }

                        if (error.isNotEmpty()) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFFFEFED)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment =
                                        Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector =
                                            Icons.Outlined.ErrorOutline,
                                        contentDescription = null,
                                        tint = RojoDetalleAdmin
                                    )

                                    Spacer(
                                        modifier = Modifier.padding(
                                            horizontal = 5.dp
                                        )
                                    )

                                    Text(
                                        text = error,
                                        color = RojoDetalleAdmin,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }

                        if (mensajeExito.isNotEmpty()) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFE4F0EB)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment =
                                        Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector =
                                            Icons.Outlined.CheckCircle,
                                        contentDescription = null,
                                        tint = VerdeDetalleAdmin
                                    )

                                    Spacer(
                                        modifier = Modifier.padding(
                                            horizontal = 5.dp
                                        )
                                    )

                                    Text(
                                        text = mensajeExito,
                                        color = VerdeDetalleAdmin,
                                        fontSize = 14.sp,
                                        fontWeight =
                                            FontWeight.Medium
                                    )
                                }
                            }
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 4.dp
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(17.dp)
                            ) {
                                Text(
                                    text = "Actualizar estado del reporte",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = AzulDetalleAdmin
                                )

                                Spacer(
                                    modifier = Modifier.height(14.dp)
                                )

                                BotonEstadoAdmin(
                                    texto = "Pendiente",
                                    seleccionado =
                                        reporte.estado
                                            .trim()
                                            .lowercase() == "pendiente",
                                    colorSeleccionado =
                                        AmarilloDetalleAdmin,
                                    icono = {
                                        Icon(
                                            imageVector =
                                                Icons.Outlined.Schedule,
                                            contentDescription = null
                                        )
                                    },
                                    habilitado = !actualizandoEstado,
                                    onClick = {
                                        cambiarEstado("pendiente")
                                    }
                                )

                                Spacer(
                                    modifier = Modifier.height(10.dp)
                                )

                                BotonEstadoAdmin(
                                    texto = "En proceso",
                                    seleccionado =
                                        reporte.estado
                                            .trim()
                                            .lowercase()
                                            .replace("_", " ") ==
                                                "en proceso",
                                    colorSeleccionado =
                                        AzulProcesoDetalleAdmin,
                                    icono = {
                                        Icon(
                                            imageVector =
                                                Icons.Outlined.Construction,
                                            contentDescription = null
                                        )
                                    },
                                    habilitado = !actualizandoEstado,
                                    onClick = {
                                        cambiarEstado("en proceso")
                                    }
                                )

                                Spacer(
                                    modifier = Modifier.height(10.dp)
                                )

                                BotonEstadoAdmin(
                                    texto = "Resuelto",
                                    seleccionado =
                                        reporte.estado
                                            .trim()
                                            .lowercase() == "resuelto",
                                    colorSeleccionado =
                                        VerdeDetalleAdmin,
                                    icono = {
                                        Icon(
                                            imageVector =
                                                Icons.Outlined.CheckCircle,
                                            contentDescription = null
                                        )
                                    },
                                    habilitado = !actualizandoEstado,
                                    onClick = {
                                        cambiarEstado("resuelto")
                                    }
                                )

                                if (actualizandoEstado) {
                                    Spacer(
                                        modifier = Modifier.height(14.dp)
                                    )

                                    Row(
                                        modifier =
                                            Modifier.fillMaxWidth(),
                                        horizontalArrangement =
                                            Arrangement.Center,
                                        verticalAlignment =
                                            Alignment.CenterVertically
                                    ) {
                                        CircularProgressIndicator(
                                            modifier =
                                                Modifier.height(22.dp),
                                            color = VerdeDetalleAdmin,
                                            strokeWidth = 2.dp
                                        )

                                        Spacer(
                                            modifier = Modifier.padding(
                                                horizontal = 6.dp
                                            )
                                        )

                                        Text(
                                            text =
                                                "Actualizando estado...",
                                            color = GrisDetalleAdmin,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }
                        }

                        Button(
                            onClick = onVolver,
                            enabled = !actualizandoEstado,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(15.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = VerdeDetalleAdmin,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = null
                            )

                            Spacer(
                                modifier = Modifier.padding(
                                    horizontal = 4.dp
                                )
                            )

                            Text(
                                text = "Volver al panel",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EstadoActualAdmin(
    estado: String
) {
    val estadoNormalizado = estado
        .trim()
        .lowercase()
        .replace("_", " ")

    val texto = when (estadoNormalizado) {
        "pendiente" -> "Pendiente"
        "en proceso" -> "En proceso"
        "resuelto" -> "Resuelto"
        else -> "Sin estado"
    }

    val color = when (estadoNormalizado) {
        "pendiente" -> AmarilloDetalleAdmin
        "en proceso" -> AzulProcesoDetalleAdmin
        "resuelto" -> VerdeDetalleAdmin
        else -> GrisDetalleAdmin
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.12f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (estadoNormalizado) {
                    "en proceso" ->
                        Icons.Outlined.Construction

                    "resuelto" ->
                        Icons.Outlined.CheckCircle

                    else ->
                        Icons.Outlined.Schedule
                },
                contentDescription = null,
                tint = color
            )

            Spacer(
                modifier = Modifier.padding(
                    horizontal = 5.dp
                )
            )

            Column {
                Text(
                    text = "Estado actual",
                    fontSize = 12.sp,
                    color = GrisDetalleAdmin
                )

                Text(
                    text = texto,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
        }
    }
}

@Composable
private fun CardInformacionAdmin(
    titulo: String,
    icono: @Composable () -> Unit,
    contenido: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(17.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                icono()

                Spacer(
                    modifier = Modifier.padding(
                        horizontal = 5.dp
                    )
                )

                Text(
                    text = titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = AzulDetalleAdmin
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            contenido()
        }
    }
}

@Composable
private fun BotonEstadoAdmin(
    texto: String,
    seleccionado: Boolean,
    colorSeleccionado: Color,
    icono: @Composable () -> Unit,
    habilitado: Boolean,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = habilitado,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(13.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (seleccionado) {
                colorSeleccionado
            } else {
                Color.Transparent
            },
            contentColor = if (seleccionado) {
                Color.White
            } else {
                GrisDetalleAdmin
            }
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (seleccionado) {
                colorSeleccionado
            } else {
                Color(0xFFCCCCCC)
            }
        )
    ) {
        icono()

        Spacer(
            modifier = Modifier.padding(
                horizontal = 5.dp
            )
        )

        Text(
            text = texto,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}