package com.example.bachapp

import androidx.compose.runtime.Composable

@Composable
expect fun PantallaMapaAdmin(
    baches: List<Bache>,
    conteoCercania: Map<Int, Int>,
    onVerDetalle: (Int) -> Unit
)
