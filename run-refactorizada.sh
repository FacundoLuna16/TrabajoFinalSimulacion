#!/bin/bash

# 🚢 Script para compilar y ejecutar la versión refactorizada
# Simulación de Barcos en Bahía

echo "🚢 ==================================="
echo "   SIMULACIÓN DE BARCOS - REFACTORIZADA"
echo "🚢 ==================================="
echo

# Colores para output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Función para mostrar mensajes
print_step() {
    echo -e "${BLUE}📋 $1${NC}"
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️ $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

# Verificar si estamos en el directorio correcto
if [ ! -f "pom.xml" ]; then
    print_error "No se encontró pom.xml. Ejecuta desde el directorio raíz del proyecto."
    exit 1
fi

print_step "Limpiando compilaciones anteriores..."
mvn clean -q

print_step "Compilando proyecto con Maven..."
if mvn compile -q; then
    print_success "Compilación exitosa"
else
    print_error "Error en la compilación"
    exit 1
fi

print_step "Ejecutando versión refactorizada..."
echo -e "${YELLOW}🎯 Iniciando VentanaPrincipalRefactorizada...${NC}"
echo

# Ejecutar con Maven
mvn exec:java -Dexec.mainClass="com.facu.simulation.ui.VentanaPrincipalRefactorizada" -Dexec.cleanupDaemonThreads=false -q

print_success "Aplicación cerrada correctamente"
echo -e "${BLUE}👋 ¡Gracias por usar la versión refactorizada!${NC}"
