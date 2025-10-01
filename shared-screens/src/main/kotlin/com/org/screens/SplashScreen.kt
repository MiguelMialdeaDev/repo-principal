package com.org.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.org.components.LoadingIndicator
import kotlinx.coroutines.delay

/**
 * Pantalla de splash simple y reutilizable.
 *
 * Los países pueden customizar el logo pero mantener la estructura común.
 *
 * @param modifier Modificador opcional
 * @param appName Nombre de la app a mostrar
 * @param logo Logo a mostrar (null = solo texto)
 * @param logoSize Tamaño del logo
 * @param showLoading Si mostrar indicador de carga
 * @param delayMillis Delay antes de llamar onTimeout
 * @param onTimeout Callback cuando termina el splash
 */
@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    appName: String = "",
    logo: Painter? = null,
    logoSize: Dp = 120.dp,
    showLoading: Boolean = true,
    delayMillis: Long = 2000L,
    onTimeout: () -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (logo != null) {
                Image(
                    painter = logo,
                    contentDescription = "App Logo",
                    modifier = Modifier.size(logoSize)
                )
            } else if (appName.isNotEmpty()) {
                Text(
                    text = appName,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            if (showLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    LoadingIndicator()
                }
            }
        }

        LaunchedEffect(Unit) {
            delay(delayMillis)
            onTimeout()
        }
    }
}
