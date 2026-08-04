package com.example.bachapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity

@Composable
actual fun rememberBiometricAuthenticator(): BiometricAuthenticator {
    val context = LocalContext.current
    val activity = context as FragmentActivity

    return remember(activity) {
        val biometricAuth = BiometricAuth(activity)

        object : BiometricAuthenticator {
            override fun autenticar(
                rol: String,
                onExito: () -> Unit,
                onError: (String) -> Unit
            ) {
                biometricAuth.autenticar(
                    rol = rol,
                    onExito = onExito,
                    onError = onError
                )
            }
        }
    }
}