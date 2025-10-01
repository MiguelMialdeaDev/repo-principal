package com.org.models

/**
 * Estados de autenticación de la aplicación.
 *
 * IMPORTANTE: Al agregar nuevos estados a esta sealed class,
 * se romperán todos los `when` exhaustivos en los repos dependientes.
 * Incrementar versión MAJOR si agregas nuevos estados.
 */
sealed class AuthState {
    /**
     * Usuario autenticado con token válido.
     */
    data class Authenticated(
        val user: UserModel,
        val token: String
    ) : AuthState()

    /**
     * Usuario no autenticado.
     */
    data object Unauthenticated : AuthState()

    /**
     * Proceso de autenticación en curso.
     */
    data object Loading : AuthState()
}