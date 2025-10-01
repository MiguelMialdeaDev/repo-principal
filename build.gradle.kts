plugins {
    id("com.android.library") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.20" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.4" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

// Configuración común para todos los submódulos
subprojects {
    group = "com.org"
    version = "1.0.0"

    // Aplicar detekt a todos los módulos
    apply(plugin = "io.gitlab.arturbosch.detekt")
}