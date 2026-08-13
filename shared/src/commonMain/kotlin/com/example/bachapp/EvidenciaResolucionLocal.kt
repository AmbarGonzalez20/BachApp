package com.example.bachapp

import androidx.compose.runtime.Composable

data class EvidenciaResolucion(
    val fotoUrl: String,
    val comentario: String,
    val fecha: String
)

@Composable
expect fun recordarEvidenciaResolucionLocal(
    bacheId: Int
): EvidenciaResolucion?
