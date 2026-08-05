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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.PriorityHigh
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

private const val RADIO_PRIORIDAD_METROS = 100.0
private const val URL_SERVIDOR_ADMIN =
    "https://backend-production-ad16.up.railway.app"

private val FondoAdmin = Color(0xFFF5F6F8)
private val AzulBarraAdmin = Color(0xFF173B57)
private val AmarilloAdmin = Color(0xFFFFC107)
private val AmarilloOscuroAdmin = Color(0xFFE5A900)
private val AmarilloClaroAdmin = Color(0xFFFFF4C7)
private val AmarilloMuyClaroAdmin = Color(0xFFFFFAE8)
private val NegroAdmin = Color(0xFF252525)
private val GrisAdmin = Color(0xFF62676D)
private val GrisBordeAdmin = Color(0xFFE0E0E0)
private val RojoAdmin = Color(0xFFB3261E)
private val RojoClaroAdmin = Color(0xFFFFEFED)
private val AzulProcesoAdmin = Color(0xFF2F6FA3)
private val AzulProcesoClaroAdmin = Color(0xFFE4F0FA)
private val VerdeResueltoAdmin = Color(0xFF2F6B55)
private val VerdeResueltoClaroAdmin = Color(0xFFE4F0EB)

private enum class VistaAdmin {
    PRINCIPAL,
    IMPORTANTES,
    TODOS,
    PENDIENTES,
    EN_PROCESO,
    RESUELTOS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAdmin(
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

    var vistaActual by remember {
        mutableStateOf(VistaAdmin.PRINCIPAL)
    }

    var bacheIdSeleccionado by remember {
        mutableStateOf<Int?>(null)
    }

    var bacheAEliminar by remember {
        mutableStateOf<Bache?>(null)
    }

    var mostrarDialogoEliminar by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    fun cargarBaches() {
        scope.launch {
            cargando = true
            error = ""

            try {
                baches = ApiClient.obtenerBaches()
            } catch (e: Exception) {
                error = "No fue posible cargar los reportes: ${
                    e.message ?: "Error desconocido"
                }"
            } finally {
                cargando = false
            }
        }
    }

    LaunchedEffect(Unit) {
        cargarBaches()
    }

    if (bacheIdSeleccionado != null) {
        PantallaDetalleAdmin(
            bacheId = bacheIdSeleccionado!!,
            onVolver = {
                bacheIdSeleccionado = null
                cargarBaches()
            }
        )
        return
    }

    if (mostrarDialogoEliminar && bacheAEliminar != null) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogoEliminar = false
                bacheAEliminar = null
            },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.DeleteOutline,
                    contentDescription = null,
                    tint = RojoAdmin
                )
            },
            title = {
                Text(
                    text = "Eliminar reporte",
                    fontWeight = FontWeight.ExtraBold,
                    color = NegroAdmin
                )
            },
            text = {
                Text(
                    text = "¿Deseas eliminar este reporte? Esta acción no se puede deshacer.",
                    color = GrisAdmin
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val reporte = bacheAEliminar ?: return@Button

                        scope.launch {
                            try {
                                ApiClient.eliminarBache(reporte.id)
                                mostrarDialogoEliminar = false
                                bacheAEliminar = null
                                cargarBaches()
                            } catch (e: Exception) {
                                mostrarDialogoEliminar = false
                                bacheAEliminar = null
                                error = "No fue posible eliminar el reporte: ${
                                    e.message ?: "Error desconocido"
                                }"
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RojoAdmin,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Eliminar",
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        mostrarDialogoEliminar = false
                        bacheAEliminar = null
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Cancelar",
                        color = NegroAdmin
                    )
                }
            },
            containerColor = Color.White
        )
    }

    val totalPendientes = baches.count {
        normalizarEstadoAdmin(it.estado) == "pendiente"
    }

    val totalEnProceso = baches.count {
        normalizarEstadoAdmin(it.estado) == "en proceso"
    }

    val totalResueltos = baches.count {
        normalizarEstadoAdmin(it.estado) == "resuelto"
    }

    val conteoCercania = remember(baches) {
        calcularReportesCercanos(
            baches = baches,
            radioMetros = RADIO_PRIORIDAD_METROS
        )
    }

    val zonasPrioritarias = conteoCercania.values.count {
        it >= 2
    }

    val prioridadMaxima = conteoCercania.values.maxOrNull() ?: 0

    val reportesMostrados = remember(
        baches,
        vistaActual,
        conteoCercania
    ) {
        baches
            .filter { bache ->
                when (vistaActual) {
                    VistaAdmin.IMPORTANTES ->
                        (conteoCercania[bache.id] ?: 1) >= 2

                    VistaAdmin.PENDIENTES ->
                        normalizarEstadoAdmin(bache.estado) == "pendiente"

                    VistaAdmin.EN_PROCESO ->
                        normalizarEstadoAdmin(bache.estado) == "en proceso"

                    VistaAdmin.RESUELTOS ->
                        normalizarEstadoAdmin(bache.estado) == "resuelto"

                    else -> true
                }
            }
            .sortedWith(
                compareByDescending<Bache> { bache ->
                    conteoCercania[bache.id] ?: 1
                }.thenByDescending { bache ->
                    bache.id
                }
            )
    }

    Scaffold(
        containerColor = FondoAdmin,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Panel administrativo",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    if (vistaActual != VistaAdmin.PRINCIPAL) {
                        IconButton(
                            onClick = {
                                vistaActual = VistaAdmin.PRINCIPAL
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color.White
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            cargarBaches()
                        },
                        enabled = !cargando
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = "Actualizar",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = onCerrarSesion
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Logout,
                            contentDescription = "Cerrar sesión",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AzulBarraAdmin,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->

        when (vistaActual) {
            VistaAdmin.PRINCIPAL -> {
                PantallaPrincipalAdmin(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    importantes = conteoCercania.values.count { it >= 2 },
                    pendientes = totalPendientes,
                    enProceso = totalEnProceso,
                    resueltos = totalResueltos,
                    onImportantes = {
                        vistaActual = VistaAdmin.IMPORTANTES
                    },
                    onPendientes = {
                        vistaActual = VistaAdmin.PENDIENTES
                    },
                    onEnProceso = {
                        vistaActual = VistaAdmin.EN_PROCESO
                    },
                    onResueltos = {
                        vistaActual = VistaAdmin.RESUELTOS
                    },
                    onReportesRegistrados = {
                        vistaActual = VistaAdmin.TODOS
                    }
                )
            }

            else -> {
                PantallaListaAdmin(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    vistaActual = vistaActual,
                    reportes = reportesMostrados,
                    cargando = cargando,
                    error = error,
                    conteoCercania = conteoCercania,
                    totalReportes = baches.size,
                    totalPendientes = totalPendientes,
                    totalEnProceso = totalEnProceso,
                    totalResueltos = totalResueltos,
                    zonasPrioritarias = zonasPrioritarias,
                    prioridadMaxima = prioridadMaxima,
                    onReintentar = {
                        cargarBaches()
                    },
                    onVerDetalle = { id ->
                        bacheIdSeleccionado = id
                    },
                    onEliminar = { bache ->
                        bacheAEliminar = bache
                        mostrarDialogoEliminar = true
                    }
                )
            }
        }
    }
}

@Composable
private fun PantallaPrincipalAdmin(
    modifier: Modifier,
    importantes: Int,
    pendientes: Int,
    enProceso: Int,
    resueltos: Int,
    onImportantes: () -> Unit,
    onPendientes: () -> Unit,
    onEnProceso: () -> Unit,
    onResueltos: () -> Unit,
    onReportesRegistrados: () -> Unit
) {
    LazyColumn(
        modifier = modifier
            .navigationBarsPadding(),
        contentPadding = PaddingValues(
            horizontal = 18.dp,
            vertical = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Text(
                text = "BachApp",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NegroAdmin,
                textAlign = TextAlign.Center
            )
        }

        item {
            Text(
                text = "Gestión y seguimiento de reportes viales",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 14.sp,
                color = GrisAdmin,
                textAlign = TextAlign.Center
            )
        }

        item {
            Spacer(
                modifier = Modifier.height(4.dp)
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BotonResumenAdmin(
                    modifier = Modifier.weight(1f),
                    titulo = "Baches de mayor importancia",
                    valor = importantes,
                    icono = Icons.Outlined.PriorityHigh,
                    colorIcono = RojoAdmin,
                    colorFondoIcono = RojoClaroAdmin,
                    onClick = onImportantes
                )

                BotonResumenAdmin(
                    modifier = Modifier.weight(1f),
                    titulo = "Pendientes",
                    valor = pendientes,
                    icono = Icons.Outlined.Schedule,
                    colorIcono = AmarilloOscuroAdmin,
                    colorFondoIcono = AmarilloMuyClaroAdmin,
                    onClick = onPendientes
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BotonResumenAdmin(
                    modifier = Modifier.weight(1f),
                    titulo = "En proceso",
                    valor = enProceso,
                    icono = Icons.Outlined.Construction,
                    colorIcono = AzulProcesoAdmin,
                    colorFondoIcono = AzulProcesoClaroAdmin,
                    onClick = onEnProceso
                )

                BotonResumenAdmin(
                    modifier = Modifier.weight(1f),
                    titulo = "Resueltos",
                    valor = resueltos,
                    icono = Icons.Outlined.CheckCircle,
                    colorIcono = VerdeResueltoAdmin,
                    colorFondoIcono = VerdeResueltoClaroAdmin,
                    onClick = onResueltos
                )
            }
        }

        item {
            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }

        item {
            Button(
                onClick = onReportesRegistrados,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AmarilloAdmin,
                    contentColor = NegroAdmin
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Assignment,
                    contentDescription = null
                )

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                Text(
                    text = "Reportes registrados",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Composable
private fun BotonResumenAdmin(
    modifier: Modifier,
    titulo: String,
    valor: Int,
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    colorIcono: Color,
    colorFondoIcono: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        border = BorderStroke(
            width = 1.dp,
            color = GrisBordeAdmin
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(17.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(47.dp)
                    .background(
                        color = colorFondoIcono,
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = colorIcono
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = valor.toString(),
                fontSize = 25.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colorIcono
            )

            Text(
                text = titulo,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = NegroAdmin,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PantallaListaAdmin(
    modifier: Modifier,
    vistaActual: VistaAdmin,
    reportes: List<Bache>,
    cargando: Boolean,
    error: String,
    conteoCercania: Map<Int, Int>,
    totalReportes: Int,
    totalPendientes: Int,
    totalEnProceso: Int,
    totalResueltos: Int,
    zonasPrioritarias: Int,
    prioridadMaxima: Int,
    onReintentar: () -> Unit,
    onVerDetalle: (Int) -> Unit,
    onEliminar: (Bache) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .navigationBarsPadding(),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 18.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = tituloVistaAdmin(vistaActual),
                modifier = Modifier.fillMaxWidth(),
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NegroAdmin,
                textAlign = TextAlign.Center
            )
        }

        item {
            Text(
                text = "Los reportes con mayor concentración dentro de 100 metros aparecen primero.",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = GrisAdmin,
                textAlign = TextAlign.Center
            )
        }

        when {
            cargando -> {
                item {
                    EstadoCargandoAdmin()
                }
            }

            error.isNotBlank() -> {
                item {
                    EstadoErrorAdmin(
                        mensaje = error,
                        onReintentar = onReintentar
                    )
                }
            }

            reportes.isEmpty() -> {
                item {
                    EstadoVacioAdmin()
                }
            }

            else -> {
                items(
                    items = reportes,
                    key = { bache ->
                        bache.id
                    }
                ) { bache ->
                    CardReporteAdmin(
                        bache = bache,
                        cantidadReportesCercanos =
                            conteoCercania[bache.id] ?: 1,
                        onVerDetalle = {
                            onVerDetalle(bache.id)
                        },
                        onEliminar = {
                            onEliminar(bache)
                        }
                    )
                }
            }
        }

        item {
            ResumenAtencionAdmin(
                totalReportes = totalReportes,
                pendientes = totalPendientes,
                enProceso = totalEnProceso,
                resueltos = totalResueltos,
                zonasPrioritarias = zonasPrioritarias,
                prioridadMaxima = prioridadMaxima
            )
        }

        item {
            Spacer(
                modifier = Modifier.height(12.dp)
            )
        }
    }
}

@Composable
private fun CardReporteAdmin(
    bache: Bache,
    cantidadReportesCercanos: Int,
    onVerDetalle: () -> Unit,
    onEliminar: () -> Unit
) {
    val estado = normalizarEstadoAdmin(
        bache.estado
    )

    val textoEstado = when (estado) {
        "pendiente" -> "Pendiente"
        "en proceso" -> "En proceso"
        "resuelto" -> "Resuelto"
        else -> "Sin estado"
    }

    val colorEstado = when (estado) {
        "pendiente" -> AmarilloOscuroAdmin
        "en proceso" -> AzulProcesoAdmin
        "resuelto" -> VerdeResueltoAdmin
        else -> GrisAdmin
    }

    val fondoEstado = when (estado) {
        "pendiente" -> AmarilloMuyClaroAdmin
        "en proceso" -> AzulProcesoClaroAdmin
        "resuelto" -> VerdeResueltoClaroAdmin
        else -> Color(0xFFF0F1F2)
    }

    val urlFoto = obtenerUrlFotoAdmin(
        bache.fotoUrl
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onVerDetalle,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column {
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
                        contentDescription = "Foto del bache",
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
                                AmarilloMuyClaroAdmin
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = null,
                            tint = AmarilloOscuroAdmin,
                            modifier = Modifier.size(52.dp)
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
                        color = colorEstado,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Column(
                modifier = Modifier.padding(18.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Reporte #${bache.id}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = NegroAdmin
                        )

                        Spacer(
                            modifier = Modifier.height(5.dp)
                        )

                        Text(
                            text = bache.descripcion.ifBlank {
                                "El reporte no contiene una descripción."
                            },
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = GrisAdmin,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Icon(
                        imageVector = Icons.Outlined.ChevronRight,
                        contentDescription = "Ver detalle",
                        tint = GrisAdmin
                    )
                }

                if (cantidadReportesCercanos >= 2) {
                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )

                    Row(
                        modifier = Modifier
                            .background(
                                color = RojoClaroAdmin,
                                shape = RoundedCornerShape(50.dp)
                            )
                            .padding(
                                horizontal = 11.dp,
                                vertical = 7.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.PriorityHigh,
                            contentDescription = null,
                            tint = RojoAdmin,
                            modifier = Modifier.size(17.dp)
                        )

                        Spacer(
                            modifier = Modifier.width(6.dp)
                        )

                        Text(
                            text = "$cantidadReportesCercanos reportes cercanos",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = RojoAdmin
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(15.dp)
                )

                HorizontalDivider(
                    color = GrisBordeAdmin
                )

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                Text(
                    text = "Latitud: ${bache.latitud}",
                    fontSize = 12.sp,
                    color = NegroAdmin
                )

                Text(
                    text = "Longitud: ${bache.longitud}",
                    fontSize = 12.sp,
                    color = NegroAdmin
                )

                Spacer(
                    modifier = Modifier.height(15.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onVerDetalle
                    ) {
                        Text(
                            text = "Ver detalle",
                            color = NegroAdmin,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(
                        modifier = Modifier.width(4.dp)
                    )

                    TextButton(
                        onClick = onEliminar,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = RojoAdmin
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DeleteOutline,
                            contentDescription = null,
                            modifier = Modifier.size(19.dp)
                        )

                        Spacer(
                            modifier = Modifier.width(5.dp)
                        )

                        Text(
                            text = "Eliminar",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ResumenAtencionAdmin(
    totalReportes: Int,
    pendientes: Int,
    enProceso: Int,
    resueltos: Int,
    zonasPrioritarias: Int,
    prioridadMaxima: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = AmarilloClaroAdmin
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Resumen de atención",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NegroAdmin,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            DatoResumenAdmin(
                etiqueta = "Reportes registrados",
                valor = totalReportes.toString()
            )

            DatoResumenAdmin(
                etiqueta = "Pendientes",
                valor = pendientes.toString()
            )

            DatoResumenAdmin(
                etiqueta = "En proceso",
                valor = enProceso.toString()
            )

            DatoResumenAdmin(
                etiqueta = "Resueltos",
                valor = resueltos.toString()
            )

            DatoResumenAdmin(
                etiqueta = "Zonas prioritarias",
                valor = zonasPrioritarias.toString()
            )

            DatoResumenAdmin(
                etiqueta = "Mayor concentración",
                valor = if (prioridadMaxima >= 2) {
                    "$prioridadMaxima reportes cercanos"
                } else {
                    "Sin coincidencias"
                }
            )
        }
    }
}

@Composable
private fun DatoResumenAdmin(
    etiqueta: String,
    valor: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = etiqueta,
            fontSize = 14.sp,
            color = GrisAdmin
        )

        Text(
            text = valor,
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            color = NegroAdmin
        )
    }
}

@Composable
private fun EstadoCargandoAdmin() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = AmarilloOscuroAdmin
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Cargando reportes...",
                color = GrisAdmin
            )
        }
    }
}

@Composable
private fun EstadoErrorAdmin(
    mensaje: String,
    onReintentar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = RojoClaroAdmin
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.ErrorOutline,
                contentDescription = null,
                tint = RojoAdmin,
                modifier = Modifier.size(40.dp)
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = mensaje,
                modifier = Modifier.fillMaxWidth(),
                color = RojoAdmin,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(13.dp)
            )

            OutlinedButton(
                onClick = onReintentar,
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Refresh,
                    contentDescription = null,
                    tint = NegroAdmin
                )

                Spacer(
                    modifier = Modifier.width(7.dp)
                )

                Text(
                    text = "Reintentar",
                    color = NegroAdmin
                )
            }
        }
    }
}

@Composable
private fun EstadoVacioAdmin() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Assignment,
                contentDescription = null,
                tint = AmarilloOscuroAdmin,
                modifier = Modifier.size(52.dp)
            )

            Spacer(
                modifier = Modifier.height(13.dp)
            )

            Text(
                text = "No hay reportes disponibles",
                fontWeight = FontWeight.ExtraBold,
                color = NegroAdmin
            )
        }
    }
}

private fun tituloVistaAdmin(
    vista: VistaAdmin
): String {
    return when (vista) {
        VistaAdmin.IMPORTANTES -> "Baches de mayor importancia"
        VistaAdmin.PENDIENTES -> "Reportes pendientes"
        VistaAdmin.EN_PROCESO -> "Reportes en proceso"
        VistaAdmin.RESUELTOS -> "Reportes resueltos"
        else -> "Reportes registrados"
    }
}

private fun normalizarEstadoAdmin(
    estado: String
): String {
    return estado
        .trim()
        .lowercase()
        .replace("_", " ")
}

private fun obtenerUrlFotoAdmin(
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
            "$URL_SERVIDOR_ADMIN$ruta"

        else ->
            "$URL_SERVIDOR_ADMIN/$ruta"
    }
}

private fun calcularReportesCercanos(
    baches: List<Bache>,
    radioMetros: Double
): Map<Int, Int> {
    val resultado = mutableMapOf<Int, Int>()

    baches.forEach { origen ->
        if (
            origen.latitud == 0.0 &&
            origen.longitud == 0.0
        ) {
            resultado[origen.id] = 1
            return@forEach
        }

        val cantidad = baches.count { destino ->
            if (
                destino.latitud == 0.0 &&
                destino.longitud == 0.0
            ) {
                false
            } else {
                distanciaMetrosAdmin(
                    latitud1 = origen.latitud,
                    longitud1 = origen.longitud,
                    latitud2 = destino.latitud,
                    longitud2 = destino.longitud
                ) <= radioMetros
            }
        }

        resultado[origen.id] = cantidad.coerceAtLeast(1)
    }

    return resultado
}

private fun distanciaMetrosAdmin(
    latitud1: Double,
    longitud1: Double,
    latitud2: Double,
    longitud2: Double
): Double {
    val radioTierraMetros = 6_371_000.0

    val latitud1Rad = Math.toRadians(latitud1)
    val latitud2Rad = Math.toRadians(latitud2)
    val diferenciaLatitud =
        Math.toRadians(latitud2 - latitud1)
    val diferenciaLongitud =
        Math.toRadians(longitud2 - longitud1)

    val a =
        sin(diferenciaLatitud / 2) *
                sin(diferenciaLatitud / 2) +
                cos(latitud1Rad) *
                cos(latitud2Rad) *
                sin(diferenciaLongitud / 2) *
                sin(diferenciaLongitud / 2)

    val c = 2 * atan2(
        sqrt(a),
        sqrt(1 - a)
    )

    return radioTierraMetros * c
}