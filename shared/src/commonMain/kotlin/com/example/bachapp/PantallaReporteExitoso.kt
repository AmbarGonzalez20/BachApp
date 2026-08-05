package com.example.bachapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddLocationAlt
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Logout
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val FondoExito = Color(0xFFF4F7F6)
private val AzulExito = Color(0xFF173B57)
private val VerdeExito = Color(0xFF2F6B55)
private val VerdeClaroExito = Color(0xFFE4F0EB)
private val GrisTextoExito = Color(0xFF5F6872)
private val GrisBordeExito = Color(0xFFD6DDDA)
private val RojoSesionExito = Color(0xFF9B2C2C)

@Composable
fun PantallaReporteExitoso(
    onVerReportes: () -> Unit,
    onRegistrarOtro: () -> Unit,
    onVolverInicio: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    Scaffold(
        containerColor = FondoExito
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(horizontal = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(50.dp))

            Box(
                modifier = Modifier
                    .size(112.dp)
                    .background(
                        color = VerdeClaroExito,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = VerdeExito,
                    modifier = Modifier.size(68.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Reporte registrado correctamente",
                fontSize = 27.sp,
                lineHeight = 34.sp,
                fontWeight = FontWeight.Bold,
                color = AzulExito,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Gracias por contribuir al mejoramiento de las vialidades de tu comunidad.",
                fontSize = 15.sp,
                lineHeight = 22.sp,
                color = GrisTextoExito,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(22.dp)
                ) {

                    Text(
                        text = "Tu reporte fue enviado",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulExito
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "La información del bache quedó registrada con su descripción, fotografía y ubicación.",
                        fontSize = 14.sp,
                        lineHeight = 21.sp,
                        color = GrisTextoExito
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onVerReportes,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VerdeExito,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ListAlt,
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.size(10.dp))

                        Text(
                            text = "Ver reportes",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(13.dp))

                    OutlinedButton(
                        onClick = onRegistrarOtro,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AddLocationAlt,
                            contentDescription = null,
                            tint = AzulExito
                        )

                        Spacer(modifier = Modifier.size(10.dp))

                        Text(
                            text = "Registrar otro bache",
                            color = AzulExito,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(13.dp))

                    OutlinedButton(
                        onClick = onVolverInicio,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Home,
                            contentDescription = null,
                            tint = AzulExito
                        )

                        Spacer(modifier = Modifier.size(10.dp))

                        Text(
                            text = "Volver al inicio",
                            color = AzulExito,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    HorizontalDivider(
                        color = GrisBordeExito
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedButton(
                        onClick = onCerrarSesion,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = RojoSesionExito
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Logout,
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.size(10.dp))

                        Text(
                            text = "Cerrar sesión",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = "BachApp · Sistema de reporte ciudadano",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = GrisTextoExito
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}