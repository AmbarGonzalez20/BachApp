package com.example.bachapp

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.ImageNotSupported
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch

private val FondoResolver = Color(0xFFF5F6F8)
private val AmarilloResolver = Color(0xFFFFC107)
private val AmarilloOscuroResolver = Color(0xFFE5A900)
private val NegroResolver = Color(0xFF252525)
private val GrisResolver = Color(0xFF62676D)
private val VerdeResolver = Color(0xFF2F6B55)
private val VerdeClaroResolver = Color(0xFFE4F0EB)
private val RojoResolver = Color(0xFFB3261E)
private val RojoClaroResolver = Color(0xFFFFEFED)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun PantallaResolverBache(
    bacheId: Int,
    onVolver: () -> Unit,
    onResolucionExitosa: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var reporte by remember {
        mutableStateOf<Bache?>(null)
    }

    var mostrarCamara by remember {
        mutableStateOf(false)
    }

    var fotoUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var comentario by remember {
        mutableStateOf("")
    }

    var guardando by remember {
        mutableStateOf(false)
    }

    var error by remember {
        mutableStateOf("")
    }

    LaunchedEffect(bacheId) {
        try {
            reporte =
                ApiClient.obtenerBachePorId(
                    bacheId
                )
        } catch (e: Exception) {
            error =
                "No fue posible cargar el reporte: " +
                    (e.message ?: "Error desconocido")
        }
    }

    fun abrirCamara() {
        mostrarCamara = true
        error = ""
    }

    val permisoCamaraLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts
                    .RequestPermission()
        ) { concedido ->

            if (concedido) {
                abrirCamara()
            } else {
                error =
                    "Se necesita permiso de cámara " +
                        "para guardar la evidencia."
            }
        }

    fun abrirCamaraConPermiso() {
        val tienePermiso =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) ==
                PackageManager.PERMISSION_GRANTED

        if (tienePermiso) {
            abrirCamara()
        } else {
            permisoCamaraLauncher.launch(
                Manifest.permission.CAMERA
            )
        }
    }

    if (mostrarCamara) {
        PantallaCamara(
            onFotoTomada = { uri ->
                fotoUri = uri
                mostrarCamara = false
                error = ""
            },
            onCancelar = {
                mostrarCamara = false
            }
        )

        return
    }

    Scaffold(
        containerColor = FondoResolver,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text =
                            "Resolver reporte",
                        fontWeight =
                            FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onVolver,
                        enabled = !guardando
                    ) {
                        Icon(
                            imageVector =
                                Icons.Outlined
                                    .ArrowBack,
                            contentDescription =
                                "Regresar",
                            tint =
                                NegroResolver
                        )
                    }
                },
                colors =
                    TopAppBarDefaults
                        .topAppBarColors(
                            containerColor =
                                AmarilloResolver,
                            titleContentColor =
                                NegroResolver
                        )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .navigationBarsPadding()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(16.dp),
            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {

            Card(
                modifier =
                    Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(20.dp),
                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            VerdeClaroResolver
                    )
            ) {
                Column(
                    modifier =
                        Modifier.padding(18.dp)
                ) {
                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector =
                                Icons.Outlined
                                    .CheckCircle,
                            contentDescription =
                                null,
                            tint =
                                VerdeResolver
                        )

                        Spacer(
                            modifier =
                                Modifier.width(10.dp)
                        )

                        Text(
                            text =
                                "Evidencia de resolución",
                            fontSize = 18.sp,
                            fontWeight =
                                FontWeight.ExtraBold,
                            color =
                                NegroResolver
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    Text(
                        text =
                            "Toma una fotografía del bache reparado. " +
                                "La evidencia se guardará en el servidor.",
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color =
                            GrisResolver
                    )
                }
            }

            Card(
                modifier =
                    Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(20.dp),
                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color.White
                    ),
                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation =
                            4.dp
                    )
            ) {
                Column(
                    modifier =
                        Modifier.padding(18.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text =
                            "Fotografía original",
                        fontSize = 16.sp,
                        fontWeight =
                            FontWeight.Bold,
                        color =
                            NegroResolver
                    )

                    val fotoOriginal =
                        reporte
                            ?.fotoUrl
                            .orEmpty()

                    if (
                        fotoOriginal
                            .isNotBlank()
                    ) {
                        Image(
                            painter =
                                rememberAsyncImagePainter(
                                    model =
                                        ApiClient
                                            .urlCompleta(
                                                fotoOriginal
                                            )
                                ),
                            contentDescription =
                                "Fotografía original",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(210.dp)
                                .clip(
                                    RoundedCornerShape(
                                        16.dp
                                    )
                                ),
                            contentScale =
                                ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .background(
                                    color =
                                        Color(
                                            0xFFF0F2F4
                                        ),
                                    shape =
                                        RoundedCornerShape(
                                            16.dp
                                        )
                                ),
                            contentAlignment =
                                Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment =
                                    Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector =
                                        Icons.Outlined
                                            .ImageNotSupported,
                                    contentDescription =
                                        null,
                                    tint =
                                        GrisResolver
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(
                                            6.dp
                                        )
                                )

                                Text(
                                    text =
                                        "Sin fotografía original",
                                    color =
                                        GrisResolver,
                                    fontSize =
                                        13.sp
                                )
                            }
                        }
                    }
                }
            }

            Card(
                modifier =
                    Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(20.dp),
                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color.White
                    ),
                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation =
                            4.dp
                    )
            ) {
                Column(
                    modifier =
                        Modifier.padding(18.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(13.dp)
                ) {

                    Text(
                        text =
                            "Fotografía del bache reparado",
                        fontSize = 16.sp,
                        fontWeight =
                            FontWeight.Bold,
                        color =
                            NegroResolver
                    )

                    if (fotoUri != null) {
                        Image(
                            painter =
                                rememberAsyncImagePainter(
                                    model =
                                        fotoUri
                                ),
                            contentDescription =
                                "Evidencia de resolución",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .clip(
                                    RoundedCornerShape(
                                        16.dp
                                    )
                                ),
                            contentScale =
                                ContentScale.Crop
                        )

                        OutlinedButton(
                            onClick = {
                                abrirCamaraConPermiso()
                            },
                            modifier =
                                Modifier.fillMaxWidth(),
                            enabled =
                                !guardando,
                            shape =
                                RoundedCornerShape(
                                    14.dp
                                )
                        ) {
                            Icon(
                                imageVector =
                                    Icons.Outlined
                                        .CameraAlt,
                                contentDescription =
                                    null,
                                tint =
                                    NegroResolver
                            )

                            Spacer(
                                modifier =
                                    Modifier.width(
                                        8.dp
                                    )
                            )

                            Text(
                                text =
                                    "Tomar otra fotografía",
                                color =
                                    NegroResolver,
                                fontWeight =
                                    FontWeight.SemiBold
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .background(
                                    color =
                                        Color(
                                            0xFFFFFAE8
                                        ),
                                    shape =
                                        RoundedCornerShape(
                                            16.dp
                                        )
                                ),
                            contentAlignment =
                                Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment =
                                    Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector =
                                        Icons.Outlined
                                            .CameraAlt,
                                    contentDescription =
                                        null,
                                    tint =
                                        AmarilloOscuroResolver,
                                    modifier =
                                        Modifier.size(
                                            42.dp
                                        )
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(
                                            8.dp
                                        )
                                )

                                Text(
                                    text =
                                        "La evidencia fotográfica es obligatoria",
                                    color =
                                        GrisResolver,
                                    fontSize =
                                        13.sp,
                                    textAlign =
                                        TextAlign.Center
                                )
                            }
                        }

                        Button(
                            onClick = {
                                abrirCamaraConPermiso()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            enabled =
                                !guardando,
                            shape =
                                RoundedCornerShape(
                                    14.dp
                                ),
                            colors =
                                ButtonDefaults
                                    .buttonColors(
                                        containerColor =
                                            AmarilloResolver,
                                        contentColor =
                                            NegroResolver
                                    )
                        ) {
                            Icon(
                                imageVector =
                                    Icons.Outlined
                                        .CameraAlt,
                                contentDescription =
                                    null
                            )

                            Spacer(
                                modifier =
                                    Modifier.width(
                                        8.dp
                                    )
                            )

                            Text(
                                text =
                                    "Tomar fotografía",
                                fontWeight =
                                    FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Card(
                modifier =
                    Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(20.dp),
                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color.White
                    ),
                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation =
                            4.dp
                    )
            ) {
                Column(
                    modifier =
                        Modifier.padding(18.dp)
                ) {
                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector =
                                Icons.Outlined
                                    .Description,
                            contentDescription =
                                null,
                            tint =
                                AmarilloOscuroResolver
                        )

                        Spacer(
                            modifier =
                                Modifier.width(8.dp)
                        )

                        Text(
                            text =
                                "Observación",
                            fontWeight =
                                FontWeight.Bold,
                            color =
                                NegroResolver
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    OutlinedTextField(
                        value =
                            comentario,
                        onValueChange = {
                            comentario = it
                            error = ""
                        },
                        modifier =
                            Modifier.fillMaxWidth(),
                        minLines = 4,
                        maxLines = 6,
                        placeholder = {
                            Text(
                                text =
                                    "Ejemplo: Se reparó la superficie y la vialidad quedó nivelada."
                            )
                        },
                        enabled =
                            !guardando,
                        shape =
                            RoundedCornerShape(
                                14.dp
                            ),
                        colors =
                            OutlinedTextFieldDefaults
                                .colors(
                                    focusedBorderColor =
                                        AmarilloOscuroResolver
                                )
                    )

                    Spacer(
                        modifier =
                            Modifier.height(6.dp)
                    )

                    Text(
                        text =
                            "La observación es opcional.",
                        fontSize =
                            12.sp,
                        color =
                            GrisResolver
                    )
                }
            }

            if (error.isNotBlank()) {
                Card(
                    modifier =
                        Modifier.fillMaxWidth(),
                    shape =
                        RoundedCornerShape(14.dp),
                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                RojoClaroResolver
                        )
                ) {
                    Row(
                        modifier =
                            Modifier.padding(14.dp),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector =
                                Icons.Outlined
                                    .ErrorOutline,
                            contentDescription =
                                null,
                            tint =
                                RojoResolver
                        )

                        Spacer(
                            modifier =
                                Modifier.width(8.dp)
                        )

                        Text(
                            text =
                                error,
                            color =
                                RojoResolver,
                            fontSize =
                                13.sp
                        )
                    }
                }
            }

            if (guardando) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 12.dp
                        ),
                    contentAlignment =
                        Alignment.Center
                ) {
                    Column(
                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color =
                                VerdeResolver
                        )

                        Spacer(
                            modifier =
                                Modifier.height(
                                    10.dp
                                )
                        )

                        Text(
                            text =
                                "Guardando evidencia...",
                            color =
                                GrisResolver
                        )
                    }
                }
            } else {
                Button(
                    onClick = {
                        val uri =
                            fotoUri

                        if (uri == null) {
                            error =
                                "Debes tomar una fotografía del bache reparado."
                            return@Button
                        }

                        scope.launch {
                            guardando =
                                true

                            error = ""

                            try {
                                val bytes =
                                    context
                                        .contentResolver
                                        .openInputStream(
                                            uri
                                        )
                                        ?.use {
                                                inputStream ->
                                            inputStream
                                                .readBytes()
                                        }
                                        ?: throw IllegalStateException(
                                            "No fue posible leer la fotografía."
                                        )

                                /*
                                 * 1) Sube la foto usando el endpoint
                                 *    corregido por tu compañera.
                                 */
                                val urlFotoResolucion =
                                    ApiClient.subirFoto(
                                        bytes =
                                            bytes,
                                        nombreArchivo =
                                            "resuelto_${bacheId}_${System.currentTimeMillis()}.jpg"
                                    )

                                /*
                                 * 2) Guarda evidencia + comentario +
                                 *    fecha en PostgreSQL y cambia a
                                 *    RESUELTO en una sola operación.
                                 */
                                val resultado =
                                    ApiClient.resolverBache(
                                        id =
                                            bacheId,
                                        fotoResolucionUrl =
                                            urlFotoResolucion,
                                        comentarioResolucion =
                                            comentario
                                                .trim()
                                    )

                                if (
                                    resultado
                                        .fotoResolucionUrl
                                        .isBlank()
                                ) {
                                    throw IllegalStateException(
                                        "El reporte se resolvió, pero el servidor no devolvió la evidencia."
                                    )
                                }

                                /*
                                 * 3) Verificación final contra Railway.
                                 */
                                val verificacion =
                                    ApiClient
                                        .obtenerBachePorId(
                                            bacheId
                                        )

                                val estadoVerificado =
                                    verificacion
                                        .estado
                                        .trim()
                                        .lowercase()
                                        .replace(
                                            " ",
                                            "_"
                                        )

                                if (
                                    estadoVerificado !=
                                    "resuelto"
                                ) {
                                    throw IllegalStateException(
                                        "El servidor respondió, pero el estado no quedó guardado como resuelto."
                                    )
                                }

                                if (
                                    verificacion
                                        .fotoResolucionUrl
                                        .isBlank()
                                ) {
                                    throw IllegalStateException(
                                        "El servidor respondió, pero la fotografía de resolución no quedó guardada."
                                    )
                                }

                                onResolucionExitosa()

                            } catch (
                                e: Exception
                            ) {
                                error =
                                    "No fue posible resolver el reporte: " +
                                        (
                                            e.message
                                                ?: "Error desconocido"
                                        )
                            } finally {
                                guardando =
                                    false
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape =
                        RoundedCornerShape(
                            16.dp
                        ),
                    colors =
                        ButtonDefaults
                            .buttonColors(
                                containerColor =
                                    VerdeResolver,
                                contentColor =
                                    Color.White
                            )
                ) {
                    Icon(
                        imageVector =
                            Icons.Outlined
                                .CheckCircle,
                        contentDescription =
                            null
                    )

                    Spacer(
                        modifier =
                            Modifier.width(9.dp)
                    )

                    Text(
                        text =
                            "Guardar evidencia y resolver",
                        fontWeight =
                            FontWeight.ExtraBold
                    )
                }

                OutlinedButton(
                    onClick =
                        onVolver,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape =
                        RoundedCornerShape(
                            16.dp
                        )
                ) {
                    Text(
                        text =
                            "Cancelar",
                        color =
                            NegroResolver,
                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )
        }
    }
}
