
package com.example.bachapp

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class BiometricAuth(
    private val activity: FragmentActivity
) {

    fun autenticar(
        rol: String,
        onExito: () -> Unit,
        onError: (String) -> Unit
    ) {
        val biometricManager = BiometricManager.from(activity)

        val autenticadores =
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL

        when (
            biometricManager.canAuthenticate(autenticadores)
        ) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                mostrarVentanaBiometrica(
                    rol = rol,
                    autenticadores = autenticadores,
                    onExito = onExito,
                    onError = onError
                )
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                onError(
                    "Este dispositivo no cuenta con autenticación biométrica."
                )
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                onError(
                    "La autenticación biométrica no está disponible temporalmente."
                )
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                onError(
                    "No hay una huella, rostro o bloqueo de pantalla configurado."
                )
            }

            else -> {
                onError(
                    "No fue posible iniciar la autenticación biométrica."
                )
            }
        }
    }

    private fun mostrarVentanaBiometrica(
        rol: String,
        autenticadores: Int,
        onExito: () -> Unit,
        onError: (String) -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(activity)

        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    onExito()
                }

                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(
                        errorCode,
                        errString
                    )

                    if (
                        errorCode != BiometricPrompt.ERROR_USER_CANCELED &&
                        errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON
                    ) {
                        onError(errString.toString())
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()

                    onError(
                        "La identidad no pudo ser verificada. Inténtalo nuevamente."
                    )
                }
            }
        )

        val titulo = if (rol == "administrador") {
            "Acceso administrativo"
        } else {
            "Verificación de identidad"
        }

        val subtitulo = if (rol == "administrador") {
            "Utilice el reconocimiento biométrico para acceder al panel."
        } else {
            "Utilice su huella o método biométrico para continuar."
        }

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(titulo)
            .setSubtitle(subtitulo)
            .setAllowedAuthenticators(autenticadores)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}