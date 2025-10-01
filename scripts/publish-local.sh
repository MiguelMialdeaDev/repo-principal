#!/bin/bash

# Script para publicar todos los módulos a mavenLocal
# Útil para testing local antes de publicar a GitHub Packages

set -e

echo "🚀 Publishing shared modules to mavenLocal..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# Limpiar builds anteriores
echo "🧹 Cleaning previous builds..."
./gradlew clean

# Compilar todos los módulos
echo "🔨 Building all modules..."
./gradlew assembleRelease

# Ejecutar tests
echo "🧪 Running tests..."
./gradlew test

# Publicar a mavenLocal
echo "📦 Publishing to mavenLocal..."
./gradlew publishToMavenLocal

echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "✅ Successfully published to mavenLocal!"
echo ""
echo "📍 Artifacts location:"
echo "   ~/.m2/repository/com/org/"
echo ""
echo "📝 To use in other projects, add to build.gradle.kts:"
echo "   repositories {"
echo "       mavenLocal()"
echo "   }"
echo "   dependencies {"
echo "       implementation(\"com.org:shared-models:1.0.0\")"
echo "       implementation(\"com.org:shared-components:1.0.0\")"
echo "       implementation(\"com.org:shared-screens:1.0.0\")"
echo "   }"