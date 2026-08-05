package com.example.bachapp

import androidx.compose.runtime.Composable

@Composable
expect fun rememberBiometricAuthenticator(): BiometricAuthenticator

interface BiometricAuthenticator {

    fun autenticar(
        rol: String,
        onExito: () -> Unit,
        onError: (String) -> Unit
    )
}