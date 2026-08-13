package com.example.bachapp

import androidx.compose.runtime.Composable

@Composable
expect fun PantallaResolverBache(
    bacheId: Int,
    onVolver: () -> Unit,
    onResolucionExitosa: () -> Unit
)
