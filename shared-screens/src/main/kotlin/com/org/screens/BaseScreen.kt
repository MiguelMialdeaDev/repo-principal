package com.org.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Pantalla base con estructura común (TopBar, BottomBar, FAB).
 *
 * Todas las pantallas de los países pueden extender este composable
 * para mantener consistencia visual.
 *
 * IMPORTANTE: Al agregar nuevos parámetros, usar valores por defecto.
 *
 * @param title Título de la pantalla
 * @param modifier Modificador opcional
 * @param showBackButton Si mostrar botón de retroceso
 * @param onBackClick Callback al presionar back
 * @param actions Acciones adicionales en la TopBar
 * @param floatingActionButton FAB opcional
 * @param bottomBar Barra inferior opcional
 * @param content Contenido de la pantalla
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    title: String,
    modifier: Modifier = Modifier,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    actions: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                },
                actions = { actions() }
            )
        },
        floatingActionButton = floatingActionButton,
        bottomBar = bottomBar
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            content(paddingValues)
        }
    }
}