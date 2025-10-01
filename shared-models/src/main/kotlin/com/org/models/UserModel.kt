package com.org.models

/**
 * Modelo de usuario común para todos los países.
 *
 * IMPORTANTE: Al agregar nuevas propiedades, SIEMPRE:
 * - Agregar al final con valor por defecto, O
 * - Hacer nullable con valor por defecto null
 *
 * Esto previene breaking changes en los repos dependientes.
 *
 * Test: trigger workflow
 */
data class UserModel(
    val id: String,
    val age: Int,  // ⚠️ BREAKING CHANGE: Campo añadido sin default en medio
    val name: String,
    val email: String
)