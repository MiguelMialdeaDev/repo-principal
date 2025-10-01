package com.org.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Botón personalizado con estado de loading.
 *
 * IMPORTANTE: Al agregar nuevos parámetros, SIEMPRE usar valores por defecto
 * para mantener compatibilidad con repos dependientes.
 *
 * @param text Texto del botón
 * @param onClick Acción al hacer click
 * @param modifier Modificador opcional
 * @param isLoading Si está en estado de carga
 * @param enabled Si el botón está habilitado
 */
@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled && !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 8.dp),
                strokeWidth = 2.dp
            )
        }
        Text(text = text)
    }
}