package com.example.bachapp

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

private const val PREFS_EVIDENCIAS = "bachapp_evidencias_resolucion"

@Composable
actual fun recordarEvidenciaResolucionLocal(
    bacheId: Int
): EvidenciaResolucion? {
    val context = LocalContext.current

    return remember(bacheId) {
        val prefs = context.getSharedPreferences(
            PREFS_EVIDENCIAS,
            Context.MODE_PRIVATE
        )

        val fotoUrl = prefs.getString(
            "foto_$bacheId",
            ""
        ).orEmpty()

        if (fotoUrl.isBlank()) {
            null
        } else {
            EvidenciaResolucion(
                fotoUrl = fotoUrl,
                comentario = prefs.getString(
                    "comentario_$bacheId",
                    ""
                ).orEmpty(),
                fecha = prefs.getString(
                    "fecha_$bacheId",
                    ""
                ).orEmpty()
            )
        }
    }
}

internal fun guardarEvidenciaResolucionLocal(
    context: Context,
    bacheId: Int,
    fotoUrl: String,
    comentario: String,
    fecha: String
) {
    context.getSharedPreferences(
        PREFS_EVIDENCIAS,
        Context.MODE_PRIVATE
    )
        .edit()
        .putString("foto_$bacheId", fotoUrl)
        .putString("comentario_$bacheId", comentario)
        .putString("fecha_$bacheId", fecha)
        .apply()
}
