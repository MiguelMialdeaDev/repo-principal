#!/usr/bin/env kotlin

/**
 * Script para detectar breaking changes en data classes.
 *
 * Uso:
 *   kotlin scripts/validate-data-classes.main.kts old/UserModel.kt new/UserModel.kt
 */

import java.io.File

data class DataClassInfo(
    val name: String,
    val properties: List<PropertyInfo>
)

data class PropertyInfo(
    val name: String,
    val type: String,
    val hasDefault: Boolean,
    val isNullable: Boolean,
    val position: Int
)

fun analyzeDataClass(content: String): List<DataClassInfo> {
    val dataClassRegex = """data class (\w+)\s*\((.*?)\)""".toRegex(RegexOption.DOT_MATCHES_ALL)
    val propertyRegex = """(?:val|var)\s+(\w+):\s*([^,=\)]+?)(\?)?(?:\s*=\s*([^,\)]+))?""".toRegex()

    return dataClassRegex.findAll(content).map { match ->
        val className = match.groupValues[1]
        val params = match.groupValues[2]

        val properties = propertyRegex.findAll(params).mapIndexed { index, propMatch ->
            PropertyInfo(
                name = propMatch.groupValues[1],
                type = propMatch.groupValues[2].trim(),
                isNullable = propMatch.groupValues[3].isNotEmpty(),
                hasDefault = propMatch.groupValues[4].isNotEmpty(),
                position = index
            )
        }.toList()

        DataClassInfo(className, properties)
    }.toList()
}

fun detectBreakingChanges(old: DataClassInfo, new: DataClassInfo): List<String> {
    val issues = mutableListOf<String>()

    // 1. Propiedad añadida sin default
    new.properties.forEachIndexed { index, newProp ->
        val oldProp = old.properties.getOrNull(index)
        if (oldProp?.name != newProp.name && !newProp.hasDefault && !newProp.isNullable) {
            issues.add("❌ BREAKING: Propiedad '${newProp.name}' añadida en posición $index sin valor por defecto")
        }
    }

    // 2. Cambio de orden
    old.properties.forEachIndexed { index, oldProp ->
        val newIndex = new.properties.indexOfFirst { it.name == oldProp.name }
        if (newIndex != -1 && newIndex != index) {
            issues.add("⚠️  WARNING: Propiedad '${oldProp.name}' cambió de posición $index → $newIndex")
        }
    }

    // 3. Propiedad eliminada
    old.properties.forEach { oldProp ->
        if (new.properties.none { it.name == oldProp.name }) {
            issues.add("❌ BREAKING: Propiedad '${oldProp.name}' eliminada")
        }
    }

    // 4. Tipo cambiado
    old.properties.forEach { oldProp ->
        val newProp = new.properties.find { it.name == oldProp.name }
        if (newProp != null && oldProp.type != newProp.type) {
            issues.add("❌ BREAKING: Tipo de '${oldProp.name}' cambió: ${oldProp.type} → ${newProp.type}")
        }
    }

    // 5. Nullability cambiada
    old.properties.forEach { oldProp ->
        val newProp = new.properties.find { it.name == oldProp.name }
        if (newProp != null && oldProp.isNullable != newProp.isNullable) {
            if (!oldProp.isNullable && newProp.isNullable) {
                issues.add("✅ SAFE: '${oldProp.name}' ahora es nullable (menos restrictivo)")
            } else {
                issues.add("❌ BREAKING: '${oldProp.name}' cambió de nullable a non-nullable")
            }
        }
    }

    return issues
}

// Main execution
if (args.size < 2) {
    println("Uso: kotlin validate-data-classes.main.kts <archivo-viejo> <archivo-nuevo>")
    kotlin.system.exitProcess(1)
}

val oldFile = File(args[0])
val newFile = File(args[1])

if (!oldFile.exists() || !newFile.exists()) {
    println("Error: Uno o ambos archivos no existen")
    kotlin.system.exitProcess(1)
}

println("🔍 Analizando cambios en data classes...")
println("━".repeat(60))

val oldClasses = analyzeDataClass(oldFile.readText())
val newClasses = analyzeDataClass(newFile.readText())

if (oldClasses.isEmpty() && newClasses.isEmpty()) {
    println("No se encontraron data classes en los archivos")
    kotlin.system.exitProcess(0)
}

var hasBreakingChanges = false

// Comparar clases que existen en ambos archivos
oldClasses.forEach { oldClass ->
    val newClass = newClasses.find { it.name == oldClass.name }

    if (newClass == null) {
        println("\n❌ BREAKING: Data class '${oldClass.name}' eliminada")
        hasBreakingChanges = true
    } else {
        println("\n📦 Analizando: ${oldClass.name}")
        val issues = detectBreakingChanges(oldClass, newClass)

        if (issues.isEmpty()) {
            println("   ✅ No hay breaking changes")
        } else {
            issues.forEach { println("   $it") }
            if (issues.any { it.startsWith("❌") }) {
                hasBreakingChanges = true
            }
        }
    }
}

// Clases nuevas
newClasses.forEach { newClass ->
    if (oldClasses.none { it.name == newClass.name }) {
        println("\n✅ Nueva data class agregada: ${newClass.name}")
    }
}

println("\n" + "━".repeat(60))

if (hasBreakingChanges) {
    println("❌ Se detectaron BREAKING CHANGES")
    println("💡 Sugerencias:")
    println("   - Agregar valores por defecto a nuevas propiedades")
    println("   - Hacer propiedades nullable")
    println("   - Agregar propiedades al final, no en medio")
    println("   - Usar @Deprecated antes de eliminar")
    kotlin.system.exitProcess(1)
} else {
    println("✅ No se detectaron breaking changes")
    kotlin.system.exitProcess(0)
}