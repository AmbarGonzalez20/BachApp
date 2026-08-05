package com.example.bachapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Login
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bachapp.shared.generated.resources.Res
import bachapp.shared.generated.resources.login_banner
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

/*
 * Colores generales de BachApp.
 *
 * Estos nombres se conservan porque posiblemente también se utilizan
 * en otras pantallas de tu aplicación.
 */
val AzulClaro = Color(0xFFFFF4C7)
val AzulOscuro = Color(0xFF252525)
val VerdeBoton = Color(0xFFFFC107)
val CafeBoton = Color(0xFFE5A900)
val FondoPantalla = Color(0xFFF5F6F8)

/*
 * Colores específicos del login.
 */
private val AmarilloPrincipalLogin = Color(0xFFFFC107)
private val AmarilloOscuroLogin = Color(0xFFE5A900)
private val AmarilloClaroLogin = Color(0xFFFFF4C7)
private val NegroLogin = Color(0xFF252525)
private val GrisTextoLogin = Color(0xFF62676D)
private val GrisBordeLogin = Color(0xFFD1D5D9)
private val GrisDivisorLogin = Color(0xFFE4E7EB)
private val RojoErrorLogin = Color(0xFFB3261E)
private val FondoErrorLogin = Color(0xFFFFEFED)

@Composable
fun PantallaLogin(
    onLoginExitoso: (LoginResponse) -> Unit,
    onIrARegistro: () -> Unit
) {

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var mostrarPassword by remember {
        mutableStateOf(false)
    }

    var cargando by remember {
        mutableStateOf(false)
    }

    var error by remember {
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()

    /*
     * Se conserva el autenticador biométrico que ya tienes creado.
     */
    val biometricAuthenticator = rememberBiometricAuthenticator()

    Scaffold(
        containerColor = FondoPantalla
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {

            /*
             * Imagen superior.
             */
            Image(
                painter = painterResource(
                    Res.drawable.login_banner
                ),
                contentDescription = "Imagen principal de BachApp",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(275.dp)
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 30.dp,
                            bottomEnd = 30.dp
                        )
                    ),
                contentScale = ContentScale.Crop
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = 22.dp,
                        vertical = 28.dp
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    /*
                     * Etiqueta amarilla superior.
                     */
                    Card(
                        shape = RoundedCornerShape(50.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = AmarilloClaroLogin
                        )
                    ) {
                        Text(
                            text = "ACCESO A BACHAPP",
                            modifier = Modifier.padding(
                                horizontal = 18.dp,
                                vertical = 8.dp
                            ),
                            color = NegroLogin,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(18.dp)
                    )

                    Text(
                        text = "Bienvenido",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = NegroLogin
                    )

                    Spacer(
                        modifier = Modifier.height(7.dp)
                    )

                    Text(
                        text = "Ingresa tus datos para consultar o registrar reportes de baches.",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = GrisTextoLogin
                    )

                    Spacer(
                        modifier = Modifier.height(27.dp)
                    )

                    /*
                     * Correo electrónico.
                     */
                    OutlinedTextField(
                        value = email,
                        onValueChange = { nuevoEmail ->
                            email = nuevoEmail.trim()
                            error = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        label = {
                            Text("Correo electrónico")
                        },
                        placeholder = {
                            Text("nombre@correo.com")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Email,
                                contentDescription = null,
                                tint = AmarilloOscuroLogin
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        singleLine = true,
                        enabled = !cargando,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AmarilloOscuroLogin,
                            unfocusedBorderColor = GrisBordeLogin,
                            focusedLabelColor = AmarilloOscuroLogin,
                            unfocusedLabelColor = GrisTextoLogin,
                            cursorColor = AmarilloOscuroLogin,
                            focusedLeadingIconColor = AmarilloOscuroLogin,
                            unfocusedLeadingIconColor = GrisTextoLogin,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White
                        )
                    )

                    Spacer(
                        modifier = Modifier.height(18.dp)
                    )

                    /*
                     * Contraseña.
                     */
                    OutlinedTextField(
                        value = password,
                        onValueChange = { nuevaPassword ->
                            password = nuevaPassword
                            error = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        label = {
                            Text("Contraseña")
                        },
                        placeholder = {
                            Text("Ingresa tu contraseña")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = null,
                                tint = AmarilloOscuroLogin
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    mostrarPassword = !mostrarPassword
                                },
                                enabled = !cargando
                            ) {
                                Icon(
                                    imageVector = if (mostrarPassword) {
                                        Icons.Outlined.VisibilityOff
                                    } else {
                                        Icons.Outlined.Visibility
                                    },
                                    contentDescription = if (mostrarPassword) {
                                        "Ocultar contraseña"
                                    } else {
                                        "Mostrar contraseña"
                                    },
                                    tint = GrisTextoLogin
                                )
                            }
                        },
                        visualTransformation = if (mostrarPassword) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        singleLine = true,
                        enabled = !cargando,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AmarilloOscuroLogin,
                            unfocusedBorderColor = GrisBordeLogin,
                            focusedLabelColor = AmarilloOscuroLogin,
                            unfocusedLabelColor = GrisTextoLogin,
                            cursorColor = AmarilloOscuroLogin,
                            focusedLeadingIconColor = AmarilloOscuroLogin,
                            unfocusedLeadingIconColor = GrisTextoLogin,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White
                        )
                    )

                    /*
                     * Mensaje de error.
                     */
                    if (error.isNotBlank()) {
                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = FondoErrorLogin
                            )
                        ) {
                            Text(
                                text = error,
                                modifier = Modifier.padding(
                                    horizontal = 14.dp,
                                    vertical = 12.dp
                                ),
                                color = RojoErrorLogin,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(25.dp)
                    )

                    /*
                     * Botón para iniciar sesión.
                     */
                    Button(
                        onClick = {

                            when {
                                email.isBlank() -> {
                                    error =
                                        "El correo electrónico es obligatorio."
                                }

                                !email.contains("@") ||
                                        !email.contains(".") -> {
                                    error =
                                        "Ingresa un correo electrónico válido."
                                }

                                password.isBlank() -> {
                                    error =
                                        "La contraseña es obligatoria."
                                }

                                password.length < 6 -> {
                                    error =
                                        "La contraseña debe tener al menos 6 caracteres."
                                }

                                else -> {
                                    cargando = true
                                    error = ""

                                    /*
                                     * Primero comprobamos el usuario con el backend.
                                     */
                                    scope.launch {
                                        try {
                                            cargando = true
                                            error = ""

                                            val esAdministrador =
                                                email.equals(
                                                    "admin@bachapp.com",
                                                    ignoreCase = true
                                                ) &&
                                                        password == "admin123"

                                            if (esAdministrador) {

                                                val administrador = LoginResponse(
                                                    id = -1,
                                                    nombre = "Administrador",
                                                    email = "admin@bachapp.com",
                                                    rol = "administrador",
                                                    mensaje = "Acceso correcto"
                                                )

                                                biometricAuthenticator.autenticar(
                                                    rol = "administrador",
                                                    onExito = {
                                                        cargando = false
                                                        onLoginExitoso(administrador)
                                                    },
                                                    onError = { mensaje ->
                                                        cargando = false
                                                        error = mensaje
                                                    }
                                                )

                                            } else {

                                                val respuesta = ApiClient.login(
                                                    email = email,
                                                    password = password
                                                )

                                                biometricAuthenticator.autenticar(
                                                    rol = respuesta.rol,
                                                    onExito = {
                                                        cargando = false
                                                        onLoginExitoso(respuesta)
                                                    },
                                                    onError = { mensaje ->
                                                        cargando = false
                                                        error = mensaje
                                                    }
                                                )
                                            }

                                        } catch (e: Exception) {
                                            cargando = false
                                            error = "Correo o contraseña incorrectos."
                                        }
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(15.dp),
                        enabled = !cargando,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AmarilloPrincipalLogin,
                            contentColor = NegroLogin,
                            disabledContainerColor =
                                AmarilloPrincipalLogin.copy(
                                    alpha = 0.60f
                                ),
                            disabledContentColor =
                                NegroLogin.copy(
                                    alpha = 0.70f
                                )
                        )
                    ) {

                        if (cargando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(23.dp),
                                color = NegroLogin,
                                strokeWidth = 2.5.dp
                            )

                            Spacer(
                                modifier = Modifier.width(11.dp)
                            )

                            Text(
                                text = "Verificando identidad",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Outlined.Login,
                                contentDescription = null
                            )

                            Spacer(
                                modifier = Modifier.width(9.dp)
                            )

                            Text(
                                text = "Iniciar sesión",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(23.dp)
                    )

                    HorizontalDivider(
                        color = GrisDivisorLogin
                    )

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    /*
                     * Registro de nuevos usuarios.
                     */
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "¿No tienes una cuenta?",
                            color = GrisTextoLogin,
                            fontSize = 14.sp
                        )

                        Spacer(
                            modifier = Modifier.width(6.dp)
                        )

                        Text(
                            text = "Regístrate",
                            color = AmarilloOscuroLogin,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable(
                                enabled = !cargando
                            ) {
                                onIrARegistro()
                            }
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(17.dp)
                    )

                    Text(
                        text = "Después de verificar tus credenciales, la aplicación solicitará la autenticación biométrica disponible en tu dispositivo.",
                        modifier = Modifier.fillMaxWidth(),
                        color = GrisTextoLogin,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(32.dp)
            )
        }
    }
}