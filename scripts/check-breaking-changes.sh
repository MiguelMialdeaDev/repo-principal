#!/bin/bash

# Script para verificar breaking changes antes de crear PR
# Compara la rama actual con main

set -e

echo "🔍 Checking for breaking changes..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# Asegurar que estamos en el directorio correcto
cd "$(dirname "$0")/.."

# Verificar que tenemos kotlin instalado
if ! command -v kotlin &> /dev/null; then
    echo "❌ Error: kotlin no está instalado"
    echo "   Instala desde: https://kotlinlang.org/docs/command-line.html"
    exit 1
fi

# Obtener archivos Kotlin modificados
MODIFIED_FILES=$(git diff --name-only origin/main...HEAD | grep "\.kt$" || true)

if [ -z "$MODIFIED_FILES" ]; then
    echo "✅ No se modificaron archivos Kotlin"
    exit 0
fi

echo "📄 Archivos modificados:"
echo "$MODIFIED_FILES"
echo ""

HAS_BREAKING_CHANGES=false

# Para cada archivo modificado, verificar cambios
for FILE in $MODIFIED_FILES; do
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "📝 Analizando: $FILE"

    # Guardar versión vieja
    git show origin/main:"$FILE" > /tmp/old_version.kt 2>/dev/null || {
        echo "   ✅ Archivo nuevo (no hay versión anterior)"
        continue
    }

    # Ejecutar validación
    if kotlin scripts/validate-data-classes.main.kts /tmp/old_version.kt "$FILE"; then
        echo "   ✅ No breaking changes"
    else
        echo "   ❌ Breaking changes detectados"
        HAS_BREAKING_CHANGES=true
    fi
done

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

if [ "$HAS_BREAKING_CHANGES" = true ]; then
    echo "❌ Se detectaron BREAKING CHANGES"
    echo ""
    echo "⚠️  Esto requerirá:"
    echo "   - Incrementar versión MAJOR"
    echo "   - Documentar en CHANGELOG.md"
    echo "   - Notificar a equipos de los países"
    echo ""
    exit 1
else
    echo "✅ No se detectaron breaking changes"
    echo "   Puedes continuar con tu PR"
    exit 0
fi