# Shared Core - Módulos Compartidos Android

[![Validate PRs](https://github.com/org/repo-principal/actions/workflows/validate-pr.yml/badge.svg)](https://github.com/org/repo-principal/actions/workflows/validate-pr.yml)
[![AI Review](https://github.com/org/repo-principal/actions/workflows/ai-review.yml/badge.svg)](https://github.com/org/repo-principal/actions/workflows/ai-review.yml)

Biblioteca de módulos compartidos para aplicaciones Android multi-país. Incluye modelos de datos, componentes UI en Jetpack Compose, y pantallas base reutilizables.

## 📦 Módulos

### `shared-models`
Modelos de datos comunes:
- `UserModel`: Modelo de usuario
- `AuthState`: Estados de autenticación (sealed class)
- `ApiResponse<T>`: Wrapper genérico para respuestas de API

### `shared-components`
Componentes UI reutilizables en Jetpack Compose:
- `CustomButton`: Botón con estado de loading
- `CustomTextField`: Campo de texto con validación
- `LoadingIndicator`: Indicador de carga
- `TextFieldValidators`: Utilidades de validación

### `shared-screens`
Pantallas base y layouts:
- `BaseScreen`: Layout común con TopBar, BottomBar, FAB
- `SplashScreen`: Pantalla de splash personalizable

## 🚀 Instalación

### Opción 1: GitHub Packages (Recomendado para producción)

1. Configura tu autenticación en `~/.gradle/gradle.properties`:

```properties
gpr.user=tu-github-username
gpr.token=tu-github-token
```

2. Agrega el repositorio en tu `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.github.com/org/repo-principal")
            credentials {
                username = providers.gradleProperty("gpr.user").orNull
                password = providers.gradleProperty("gpr.token").orNull
            }
        }
    }
}
```

3. Agrega las dependencias en tu `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.org:shared-models:1.0.0")
    implementation("com.org:shared-components:1.0.0")
    implementation("com.org:shared-screens:1.0.0")
}
```

### Opción 2: Maven Local (Para desarrollo)

```bash
# En el repo principal
./scripts/publish-local.sh

# En tu proyecto
repositories {
    mavenLocal()
}
```

## 📚 Ejemplos de Uso

### UserModel

```kotlin
import com.org.models.UserModel

val user = UserModel(
    id = "123",
    name = "Juan Pérez",
    email = "juan@example.com"
)
```

### CustomButton con Loading

```kotlin
import com.org.components.CustomButton

@Composable
fun MyScreen() {
    var isLoading by remember { mutableStateOf(false) }

    CustomButton(
        text = "Login",
        onClick = {
            isLoading = true
            // Hacer login...
        },
        isLoading = isLoading
    )
}
```

### BaseScreen

```kotlin
import com.org.screens.BaseScreen

@Composable
fun HomeScreen(navController: NavController) {
    BaseScreen(
        title = "Home",
        showBackButton = false,
        actions = {
            IconButton(onClick = { /* ... */ }) {
                Icon(Icons.Default.Settings, "Settings")
            }
        }
    ) { paddingValues ->
        // Tu contenido aquí
        Text("Bienvenido", modifier = Modifier.padding(paddingValues))
    }
}
```

### ApiResponse

```kotlin
import com.org.models.ApiResponse

suspend fun getUser(id: String): ApiResponse<UserModel> {
    return try {
        val user = api.fetchUser(id)
        ApiResponse.Success(user)
    } catch (e: Exception) {
        ApiResponse.Error(e.message ?: "Unknown error")
    }
}

// Uso
when (val response = getUser("123")) {
    is ApiResponse.Success -> showUser(response.data)
    is ApiResponse.Error -> showError(response.message)
    is ApiResponse.Loading -> showLoading()
}
```

## 🔧 Desarrollo

### Requisitos

- JDK 17+
- Android SDK 34
- Gradle 8.2+
- Kotlin 1.9.20+

### Compilar localmente

```bash
./gradlew assembleRelease
```

### Ejecutar tests

```bash
./gradlew test
```

### Publicar a mavenLocal

```bash
./scripts/publish-local.sh
```

## 🚨 Breaking Changes

Este proyecto sigue [Semantic Versioning](https://semver.org/):

- **MAJOR**: Breaking changes
- **MINOR**: Nuevas features (backwards-compatible)
- **PATCH**: Bug fixes

### Reglas para evitar Breaking Changes

✅ **PERMITIDO:**
- Agregar propiedades con valores por defecto al final
- Agregar propiedades nullable
- Agregar nuevas funciones
- Hacer propiedades más permisivas (non-null → nullable)

❌ **EVITAR (Breaking Changes):**
- Agregar propiedades requeridas sin default
- Cambiar el orden de parámetros
- Eliminar propiedades o funciones
- Cambiar tipos de datos
- Agregar casos a sealed classes
- Cambiar parámetros de @Composables sin defaults

### Antes de crear un PR

Ejecuta el script de validación:

```bash
./scripts/check-breaking-changes.sh
```

## 🤖 CI/CD

### Validación automática

Cada PR ejecuta:

1. **validate-pr.yml**: Compila el código y valida que los 3 repos de países sigan funcionando
2. **ai-review.yml**: Devin/Claude analiza el código y detecta breaking changes automáticamente

### Publicación

Para publicar una nueva versión:

```bash
# 1. Bump version
./scripts/bump-version.sh [major|minor|patch]

# 2. Actualizar CHANGELOG.md

# 3. Commit y tag
git add .
git commit -m "chore: bump version to X.Y.Z"
git tag -a vX.Y.Z -m "Release vX.Y.Z"

# 4. Push
git push origin main --tags

# GitHub Actions publicará automáticamente a GitHub Packages
```

## 📋 Scripts Útiles

| Script | Descripción |
|--------|-------------|
| `scripts/publish-local.sh` | Publica todos los módulos a mavenLocal |
| `scripts/bump-version.sh` | Incrementa la versión del proyecto |
| `scripts/check-breaking-changes.sh` | Valida breaking changes vs main |
| `scripts/validate-data-classes.main.kts` | Compara data classes entre versiones |

## 🏗️ Arquitectura

```
repo-principal/
├── shared-models/          # Data models
│   └── com.org.models/
├── shared-components/      # UI Components (Compose)
│   └── com.org.components/
├── shared-screens/         # Screen layouts
│   └── com.org.screens/
├── .github/workflows/      # CI/CD
├── scripts/                # Utility scripts
└── README.md
```

## 🔗 Repos Dependientes

Este repo es consumido por:

- `repo-pais-A` 🇦🇷
- `repo-pais-B` 🇧🇷
- `repo-pais-C` 🇨🇴

Cualquier cambio aquí puede afectar a estos repos. Por eso existe validación automática.

## 🤝 Contribuir

1. Crea una branch desde `main`
2. Haz tus cambios siguiendo las convenciones
3. Ejecuta `./scripts/check-breaking-changes.sh`
4. Crea un PR
5. Espera la validación automática y el review de IA
6. Mergea cuando todos los checks pasen ✅

## 📄 License

[MIT License](LICENSE)

## 💬 Soporte

- Issues: [GitHub Issues](https://github.com/org/repo-principal/issues)
- Documentación: [Ver plan completo](multi-repo-integration-plan.md)