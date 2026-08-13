package com.example.bachapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.PriorityHigh
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val FondoEstadisticas = Color(0xFFF5F6F8)
private val NegroEstadisticas = Color(0xFF252525)
private val GrisEstadisticas = Color(0xFF62676D)
private val AmarilloEstadisticas = Color(0xFFE5A900)
private val AmarilloClaroEstadisticas = Color(0xFFFFF4C7)
private val AzulEstadisticas = Color(0xFF2F6FA3)
private val AzulClaroEstadisticas = Color(0xFFE4F0FA)
private val VerdeEstadisticas = Color(0xFF2F6B55)
private val VerdeClaroEstadisticas = Color(0xFFE4F0EB)
private val RojoEstadisticas = Color(0xFFB3261E)
private val RojoClaroEstadisticas = Color(0xFFFFEFED)

@Composable
fun PantallaEstadisticasAdmin(
    totalReportes: Int,
    pendientes: Int,
    enProceso: Int,
    resueltos: Int,
    reportesEnZonaPrioritaria: Int,
    prioridadMaxima: Int
) {
    LazyColumn(
        modifier = Modifier
            .background(FondoEstadisticas)
            .navigationBarsPadding(),
        contentPadding = PaddingValues(
            horizontal = 18.dp,
            vertical = 22.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Outlined.Assessment,
                    contentDescription = null,
                    modifier = Modifier.size(38.dp),
                    tint = Color(0xFF173B57)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Estadísticas",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NegroEstadisticas,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Resumen general del estado de los reportes",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 13.sp,
                    color = GrisEstadisticas,
                    textAlign = TextAlign.Center
                )
            }
        }

        item {
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
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Distribución de reportes",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = NegroEstadisticas
                    )

                    BarraEstadistica(
                        titulo = "Pendientes",
                        valor = pendientes,
                        total = totalReportes,
                        color = AmarilloEstadisticas,
                        colorFondo = AmarilloClaroEstadisticas,
                        icono = {
                            Icon(
                                imageVector = Icons.Outlined.Schedule,
                                contentDescription = null,
                                tint = AmarilloEstadisticas
                            )
                        }
                    )

                    BarraEstadistica(
                        titulo = "En proceso",
                        valor = enProceso,
                        total = totalReportes,
                        color = AzulEstadisticas,
                        colorFondo = AzulClaroEstadisticas,
                        icono = {
                            Icon(
                                imageVector = Icons.Outlined.Construction,
                                contentDescription = null,
                                tint = AzulEstadisticas
                            )
                        }
                    )

                    BarraEstadistica(
                        titulo = "Resueltos",
                        valor = resueltos,
                        total = totalReportes,
                        color = VerdeEstadisticas,
                        colorFondo = VerdeClaroEstadisticas,
                        icono = {
                            Icon(
                                imageVector = Icons.Outlined.CheckCircle,
                                contentDescription = null,
                                tint = VerdeEstadisticas
                            )
                        }
                    )
                }
            }
        }

        item {
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
                                .size(46.dp)
                                .background(
                                    color = RojoClaroEstadisticas,
                                    shape = RoundedCornerShape(14.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.PriorityHigh,
                                contentDescription = null,
                                tint = RojoEstadisticas
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Zonas de alta prioridad",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = NegroEstadisticas
                            )

                            Text(
                                text = "Agrupaciones detectadas dentro de 100 metros",
                                fontSize = 12.sp,
                                color = GrisEstadisticas
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        MetricaPrioridad(
                            modifier = Modifier.weight(1f),
                            valor = reportesEnZonaPrioritaria.toString(),
                            etiqueta = "Reportes en zonas prioritarias"
                        )

                        MetricaPrioridad(
                            modifier = Modifier.weight(1f),
                            valor = prioridadMaxima.toString(),
                            etiqueta = "Máxima concentración cercana"
                        )
                    }

                    Text(
                        text = if (reportesEnZonaPrioritaria > 0) {
                            "Los reportes de alta prioridad son aquellos que tienen al menos otro reporte dentro del radio de 100 metros."
                        } else {
                            "Actualmente no se detectan agrupaciones de alta prioridad dentro del radio de 100 metros."
                        },
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        color = GrisEstadisticas
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun BarraEstadistica(
    titulo: String,
    valor: Int,
    total: Int,
    color: Color,
    colorFondo: Color,
    icono: @Composable () -> Unit
) {
    val porcentaje = if (total > 0) {
        (valor.toFloat() / total.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = colorFondo,
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                icono()
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = titulo,
                modifier = Modifier.weight(1f),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = NegroEstadisticas
            )

            Text(
                text = "$valor  (${(porcentaje * 100).toInt()}%)",
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
                .background(
                    color = colorFondo,
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            if (porcentaje > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(porcentaje)
                        .height(14.dp)
                        .background(
                            color = color,
                            shape = RoundedCornerShape(20.dp)
                        )
                )
            }
        }
    }
}

@Composable
private fun MetricaPrioridad(
    modifier: Modifier,
    valor: String,
    etiqueta: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = RojoClaroEstadisticas
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = valor,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = RojoEstadisticas
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = etiqueta,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 11.sp,
                lineHeight = 15.sp,
                color = GrisEstadisticas,
                textAlign = TextAlign.Center
            )
        }
    }
}
