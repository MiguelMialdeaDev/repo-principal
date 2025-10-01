# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Initial project setup

## [1.0.0] - 2024-01-15

### Added

#### shared-models
- `UserModel` data class with id, name, email
- `AuthState` sealed class (Authenticated, Unauthenticated, Loading)
- `ApiResponse<T>` sealed class for API responses
- Extension functions for ApiResponse (map, dataOrNull)

#### shared-components
- `CustomButton` composable with loading state
- `CustomTextField` composable with validation
- `LoadingIndicator` composable
- `TextFieldValidators` object with common validators

#### shared-screens
- `BaseScreen` composable with common layout structure
- `SplashScreen` composable with customizable logo

#### CI/CD
- GitHub Actions workflow for PR validation
- GitHub Actions workflow for AI-powered code review
- Automatic breaking change detection

#### Scripts
- `validate-data-classes.main.kts` for local validation
- `publish-local.sh` for publishing to mavenLocal
- `bump-version.sh` for version management
- `check-breaking-changes.sh` for pre-PR validation

#### Documentation
- Complete README.md with usage examples
- Multi-repo integration plan
- Detekt configuration for code quality

### Changed
- N/A (initial release)

### Deprecated
- N/A (initial release)

### Removed
- N/A (initial release)

### Fixed
- N/A (initial release)

### Security
- N/A (initial release)

---

## Template para futuras versiones

```markdown
## [X.Y.Z] - YYYY-MM-DD

### Added
- Nueva funcionalidad agregada

### Changed
- Cambios en funcionalidad existente (backwards-compatible)

### Deprecated
- Funcionalidad marcada como deprecated

### Removed
- Funcionalidad eliminada (⚠️ BREAKING CHANGE)

### Fixed
- Bugs corregidos

### Security
- Vulnerabilidades resueltas

### Breaking Changes
⚠️ Lista de breaking changes y cómo migrar:
- Cambio X: descripción y migración
```

---

## Notas

- **MAJOR** version (X.0.0): Breaking changes
- **MINOR** version (0.X.0): New features (backwards-compatible)
- **PATCH** version (0.0.X): Bug fixes

### Ejemplos de Breaking Changes

- Agregar propiedad requerida sin default a data class
- Cambiar tipo de una propiedad
- Eliminar función pública
- Cambiar signature de función sin defaults
- Agregar caso a sealed class
- Cambiar parámetros de @Composable sin defaults

### Ejemplos de Non-Breaking Changes

- Agregar propiedad con default value
- Agregar nueva función
- Agregar parámetro opcional a función existente
- Deprecar función (pero mantenerla funcionando)
- Bug fixes internos