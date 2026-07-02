package com.example.bachapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val AzulClaro = Color(0xFF90CAF9)
val AzulOscuro = Color(0xFF1565C0)
val CafeBoton = Color(0xFF66BB6A)
val FondoPantalla = Color(0xFFF0F4FF)

@Composable
fun PantallaLogin(
    onLoginExitoso: (String) -> Unit,
    onIrARegistro: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    Scaffold(containerColor = FondoPantalla) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Logo
            Card(
                modifier = Modifier.size(100.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = AzulClaro)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🕳️", fontSize = 48.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "BachApp",
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = AzulOscuro
            )
            Text(
                text = "Sistema de reporte ciudadano",
                fontSize = 14.sp,
                color = Color(0xFF888888)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Formulario
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Iniciar sesion",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = AzulOscuro
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo electronico") },
                        placeholder = { Text("ejemplo@correo.com") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AzulClaro,
                            unfocusedBorderColor = Color(0xFFCCCCCC)
                        )
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contrasena") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AzulClaro,
                            unfocusedBorderColor = Color(0xFFCCCCCC)
                        )
                    )

                    if (error.isNotEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                        ) {
                            Text(
                                text = "⚠️ $error",
                                color = Color(0xFFB71C1C),
                                modifier = Modifier.padding(10.dp),
                                fontSize = 13.sp
                            )
                        }
                    }

                    if (cargando) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = AzulClaro)
                        }
                    } else {
                        Button(
                            onClick = {
                                when {
                                    email.isBlank() -> error = "El correo es obligatorio"
                                    password.isBlank() -> error = "La contrasena es obligatoria"
                                    else -> {
                                        error = ""
                                        cargando = true
                                        if (email == "admin@bachapp.com" && password == "admin123") {
                                            onLoginExitoso("administrador")
                                        } else if (email.isNotEmpty() && password.length >= 6) {
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
                                .height(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CafeBoton
                            )
                        ) {
                            Text(
                                text = "Iniciar sesion",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "¿No tienes cuenta?",
                    color = Color(0xFF888888),
                    fontSize = 14.sp
                )
                TextButton(onClick = onIrARegistro) {
                    Text(
                        text = "Registrate",
                        color = CafeBoton,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}