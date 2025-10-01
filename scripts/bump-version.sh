#!/bin/bash

# Script para incrementar la versión del proyecto
# Uso: ./scripts/bump-version.sh [major|minor|patch]

set -e

GRADLE_PROPERTIES="gradle.properties"

if [ ! -f "$GRADLE_PROPERTIES" ]; then
    echo "❌ Error: gradle.properties no encontrado"
    exit 1
fi

# Leer versión actual
CURRENT_VERSION=$(grep "^VERSION_NAME=" $GRADLE_PROPERTIES | cut -d'=' -f2)

if [ -z "$CURRENT_VERSION" ]; then
    echo "❌ Error: VERSION_NAME no encontrado en gradle.properties"
    exit 1
fi

echo "📌 Versión actual: $CURRENT_VERSION"

# Separar major.minor.patch
IFS='.' read -ra VERSION_PARTS <<< "$CURRENT_VERSION"
MAJOR="${VERSION_PARTS[0]}"
MINOR="${VERSION_PARTS[1]}"
PATCH="${VERSION_PARTS[2]}"

# Determinar tipo de bump
BUMP_TYPE="${1:-patch}"

case $BUMP_TYPE in
    major)
        MAJOR=$((MAJOR + 1))
        MINOR=0
        PATCH=0
        echo "🔼 Incrementando versión MAJOR (breaking changes)"
        ;;
    minor)
        MINOR=$((MINOR + 1))
        PATCH=0
        echo "🔼 Incrementando versión MINOR (nuevas features)"
        ;;
    patch)
        PATCH=$((PATCH + 1))
        echo "🔼 Incrementando versión PATCH (bug fixes)"
        ;;
    *)
        echo "❌ Error: Tipo inválido. Use: major, minor, o patch"
        exit 1
        ;;
esac

NEW_VERSION="$MAJOR.$MINOR.$PATCH"
echo "📌 Nueva versión: $NEW_VERSION"

# Pedir confirmación
read -p "¿Continuar con el bump? (y/N): " CONFIRM
if [[ ! "$CONFIRM" =~ ^[Yy]$ ]]; then
    echo "❌ Bump cancelado"
    exit 1
fi

# Actualizar gradle.properties
sed -i.bak "s/^VERSION_NAME=.*/VERSION_NAME=$NEW_VERSION/" $GRADLE_PROPERTIES
rm -f "${GRADLE_PROPERTIES}.bak"

# Actualizar build.gradle.kts (si tienen versión hardcodeada)
find . -name "build.gradle.kts" -type f -exec sed -i.bak "s/version = \"$CURRENT_VERSION\"/version = \"$NEW_VERSION\"/" {} \;
find . -name "*.bak" -type f -delete

echo "✅ Versión actualizada a $NEW_VERSION"
echo ""
echo "📝 Siguiente paso:"
echo "   1. Actualiza CHANGELOG.md con los cambios"
echo "   2. Commit: git add . && git commit -m \"chore: bump version to $NEW_VERSION\""
echo "   3. Tag: git tag -a v$NEW_VERSION -m \"Release v$NEW_VERSION\""
echo "   4. Push: git push origin main --tags"