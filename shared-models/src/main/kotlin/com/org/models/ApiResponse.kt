package com.org.models

/**
 * Respuesta genérica de API.
 *
 * Utilizar para envolver respuestas de red y manejar estados de loading/error.
 *
 * Ejemplo de uso:
 * ```
 * when (val response = userRepository.getUser()) {
 *     is ApiResponse.Success -> showUser(response.data)
 *     is ApiResponse.Error -> showError(response.message)
 *     is ApiResponse.Loading -> showLoading()
 * }
 * ```
 */
sealed class ApiResponse<out T> {
    /**
     * Respuesta exitosa con datos.
     */
    data class Success<T>(val data: T) : ApiResponse<T>()

    /**
     * Error en la petición.
     */
    data class Error(
        val message: String,
        val code: Int? = null,
        val exception: Throwable? = null
    ) : ApiResponse<Nothing>()

    /**
     * Petición en progreso.
     */
    data object Loading : ApiResponse<Nothing>()
}

/**
 * Extension function para mapear el contenido de un ApiResponse.Success.
 */
inline fun <T, R> ApiResponse<T>.map(transform: (T) -> R): ApiResponse<R> {
    return when (this) {
        is ApiResponse.Success -> ApiResponse.Success(transform(data))
        is ApiResponse.Error -> this
        is ApiResponse.Loading -> this
    }
}

/**
 * Extension function para obtener los datos o null.
 */
fun <T> ApiResponse<T>.dataOrNull(): T? {
    return when (this) {
        is ApiResponse.Success -> data
        else -> null
    }
}