package com.example.bachapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import bachapp.shared.generated.resources.Res
import bachapp.shared.generated.resources.login_banner

val AzulClaro = Color(0xFFE3F2FD)
val AzulOscuro = Color(0xFF1565C0)
val VerdeBoton = Color(0xFF43A047)
val CafeBoton = Color(0xFF66BB6A)
val FondoPantalla = Color(0xFFF5F7FA)

@Composable
fun PantallaLogin(
    onLoginExitoso: (String) -> Unit,
    onIrARegistro: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mostrarPassword by remember { mutableStateOf(false) }
    var cargando by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    Scaffold(
        containerColor = FondoPantalla
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

            Image(
                painter = painterResource(Res.drawable.login_banner),
                contentDescription = "Imagen principal BachApp",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 30.dp,
                            bottomEnd = 30.dp
                        )
                    ),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(18.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Iniciar sesión",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulOscuro
                    )

                    Spacer(modifier = Modifier.height(22.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        label = { Text("Correo electrónico") },
                        leadingIcon = {
                            Text(text = "📧", fontSize = 20.sp)
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AzulOscuro,
                            unfocusedBorderColor = Color.LightGray
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        label = { Text("Contraseña") },
                        leadingIcon = {
                            Text(text = "🔒", fontSize = 20.sp)
                        },
                        trailingIcon = {
                            Text(
                                text = if (mostrarPassword) "🙈" else "👁️",
                                fontSize = 18.sp,
                                modifier = Modifier.clickable {
                                    mostrarPassword = !mostrarPassword
                                }
                            )
                        },
                        visualTransformation =
                            if (mostrarPassword) VisualTransformation.None
                            else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AzulOscuro,
                            unfocusedBorderColor = Color.LightGray
                        )
                    )

                    if (error.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "⚠️ $error",
                            color = Color.Red,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))
                    if (cargando) {

                        CircularProgressIndicator(
                            color = AzulOscuro
                        )

                    } else {

                        Button(
                            onClick = {

                                when {

                                    email.isBlank() -> {
                                        error = "El correo es obligatorio"
                                    }

                                    password.isBlank() -> {
                                        error = "La contraseña es obligatoria"
                                    }

                                    else -> {

                                        error = ""
                                        cargando = true

                                        if (
                                            email == "admin@bachapp.com" &&
                                            password == "admin123"
                                        ) {

                                            onLoginExitoso("administrador")

                                        } else if (password.length >= 6) {

                                            onLoginExitoso("ciudadano")

                                        } else {

                                            error = "Credenciales incorrectas"
                                            cargando = false
                                        }
                                    }
                                }
                            },

                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),

                            shape = RoundedCornerShape(16.dp),

                            colors = ButtonDefaults.buttonColors(
                                containerColor = VerdeBoton
                            )

                        ) {

                            Text(
                                text = "Iniciar sesión",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "¿No tienes cuenta?",
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        Text(
                            text = "Regístrate",
                            color = VerdeBoton,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                onIrARegistro()
                            }
                        )

                    }

                }

            }

            Spacer(modifier = Modifier.height(30.dp))

        }

    }
}