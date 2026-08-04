package com.example.bachapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.outlined.AddLocationAlt
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import bachapp.shared.generated.resources.Res
import bachapp.shared.generated.resources.carretera

private val FondoInicio = Color(0xFFF5F6F8)
private val AmarilloInicio = Color(0xFFFFC107)
private val AmarilloOscuroInicio = Color(0xFFE5A900)
private val AmarilloClaroInicio = Color(0xFFFFF4C7)
private val NegroInicio = Color(0xFF252525)
private val GrisTextoInicio = Color(0xFF62676D)
private val GrisBordeInicio = Color(0xFFE0E0E0)
private val RojoInicio = Color(0xFF9B2C2C)

@Composable
fun PantallaInicioCiudadano(
    onRegistrarBache: () -> Unit,
    onVerReportes: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    Scaffold(
        containerColor = FondoInicio
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
        ) {

            EncabezadoBienvenida()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {

                Spacer(
                    modifier = Modifier.height(22.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        /*
                         * Imagen de carretera.
                         */
                        Image(
                            painter = painterResource(
                                resource = Res.drawable.carretera
                            ),
                            contentDescription = "Carretera",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(190.dp)
                                .clip(RoundedCornerShape(18.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(
                            modifier = Modifier.height(20.dp)
                        )

                        Text(
                            text = "Ayúdanos a mejorar las vialidades",
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 21.sp,
                            lineHeight = 27.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = NegroInicio,
                            textAlign = TextAlign.Center
                        )

                        Spacer(
                            modifier = Modifier.height(11.dp)
                        )

                        Text(
                            text = "Registra los baches de tu comunidad mediante una fotografía, descripción y ubicación.",
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 14.sp,
                            lineHeight = 21.sp,
                            color = GrisTextoInicio,
                            textAlign = TextAlign.Center
                        )

                        Spacer(
                            modifier = Modifier.height(22.dp)
                        )

                        HorizontalDivider(
                            color = GrisBordeInicio
                        )

                        Spacer(
                            modifier = Modifier.height(20.dp)
                        )

                        CaracteristicaInicio(
                            texto = "Agrega una fotografía del bache",
                            icono = {
                                Icon(
                                    imageVector = Icons.Outlined.PhotoCamera,
                                    contentDescription = null,
                                    tint = NegroInicio
                                )
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(13.dp)
                        )

                        CaracteristicaInicio(
                            texto = "Registra su ubicación",
                            icono = {
                                Icon(
                                    imageVector = Icons.Outlined.LocationOn,
                                    contentDescription = null,
                                    tint = NegroInicio
                                )
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(13.dp)
                        )

                        CaracteristicaInicio(
                            texto = "Consulta el avance de tus reportes",
                            icono = {
                                Icon(
                                    imageVector = Icons.Outlined.Assignment,
                                    contentDescription = null,
                                    tint = NegroInicio
                                )
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(13.dp)
                        )

                        CaracteristicaInicio(
                            texto = "Contribuye a una comunidad más segura",
                            icono = {
                                Icon(
                                    imageVector = Icons.Outlined.CheckCircle,
                                    contentDescription = null,
                                    tint = NegroInicio
                                )
                            }
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(28.dp)
                )

                Text(
                    text = "¿Qué deseas hacer?",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NegroInicio,
                    textAlign = TextAlign.Center
                )

                Spacer(
                    modifier = Modifier.height(17.dp)
                )

                Button(
                    onClick = onRegistrarBache,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmarilloInicio,
                        contentColor = NegroInicio
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AddLocationAlt,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(11.dp)
                    )

                    Text(
                        text = "Registrar un bache",
                        modifier = Modifier.weight(1f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Start
                    )

                    Icon(
                        imageVector = Icons.Outlined.ArrowForward,
                        contentDescription = null
                    )
                }

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                OutlinedButton(
                    onClick = onVerReportes,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(57.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = NegroInicio,
                        containerColor = Color.White
                    ),
                    border = BorderStroke(
                        width = 1.5.dp,
                        color = AmarilloOscuroInicio
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ListAlt,
                        contentDescription = null,
                        tint = AmarilloOscuroInicio
                    )

                    Spacer(
                        modifier = Modifier.width(11.dp)
                    )

                    Text(
                        text = "Ver mis reportes",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(
                    modifier = Modifier.height(27.dp)
                )

                HorizontalDivider(
                    color = GrisBordeInicio
                )

                Spacer(
                    modifier = Modifier.height(17.dp)
                )

                OutlinedButton(
                    onClick = onCerrarSesion,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = RojoInicio,
                        containerColor = Color.White
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = RojoInicio.copy(alpha = 0.55f)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Logout,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(10.dp)
                    )

                    Text(
                        text = "Cerrar sesión",
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(
                    modifier = Modifier.height(27.dp)
                )

                Text(
                    text = "BachApp · Sistema de reporte ciudadano",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = GrisTextoInicio
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )
            }
        }
    }
}

@Composable
private fun EncabezadoBienvenida() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = AmarilloInicio,
                shape = RoundedCornerShape(
                    bottomStart = 32.dp,
                    bottomEnd = 32.dp
                )
            )
            .padding(
                start = 22.dp,
                end = 22.dp,
                top = 45.dp,
                bottom = 36.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            /*
             * Se quitó el ícono de herramientas.
             */
            Text(
                text = "Bienvenida",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NegroInicio,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Sistema ciudadano de reporte de baches",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = NegroInicio.copy(alpha = 0.78f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun CaracteristicaInicio(
    texto: String,
    icono: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
                .background(
                    color = AmarilloClaroInicio,
                    shape = RoundedCornerShape(13.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            icono()
        }

        Spacer(
            modifier = Modifier.width(13.dp)
        )

        Text(
            text = texto,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Medium,
            color = NegroInicio
        )
    }
}