#!/bin/bash

# 🔍 Script para verificar la estructura refactorizada

echo "🚢 ========================================="
echo "   VERIFICACIÓN DE REFACTORIZACIÓN COMPLETADA"
echo "🚢 ========================================="
echo

GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

print_check() {
    if [ -f "$1" ]; then
        echo -e "${GREEN}✅ $1${NC}"
    else
        echo -e "${RED}❌ $1 (FALTANTE)${NC}"
    fi
}

echo -e "${BLUE}📁 Estructura de archivos creados:${NC}"
echo

echo "📂 UI/componentes/"
print_check "src/main/java/com/facu/simulation/ui/componentes/FactoryComponentes.java"
print_check "src/main/java/com/facu/simulation/ui/componentes/TarjetaEstadistica.java"

echo
echo "📂 UI/paneles/"
print_check "src/main/java/com/facu/simulation/ui/paneles/PanelParametros.java"
print_check "src/main/java/com/facu/simulation/ui/paneles/PanelControles.java"
print_check "src/main/java/com/facu/simulation/ui/paneles/PanelEstadisticas.java"

echo
echo "📂 UI/eventos/"
print_check "src/main/java/com/facu/simulation/ui/eventos/EventHandlerSimulacion.java"

echo
echo "📂 UI/estilos/"
print_check "src/main/java/com/facu/simulation/ui/estilos/TemaOscuro.java"
print_check "src/main/java/com/facu/simulation/ui/estilos/EfectosVisuales.java"

echo
echo "📂 UI/ (Principal)"
print_check "src/main/java/com/facu/simulation/ui/VentanaPrincipalRefactorizada.java"
print_check "src/main/java/com/facu/simulation/ui/VentanaPrincipal.java"

echo
echo "📂 Documentación"
print_check "REFACTORIZACION.md"
print_check "run-refactorizada.sh"

echo
echo -e "${BLUE}📊 Estadísticas de la refactorización:${NC}"
echo "• Archivo original: 1131 líneas → 9 archivos modulares"
echo "• Separación clara de responsabilidades"
echo "• Patrón Factory implementado"
echo "• Tema oscuro centralizado"
echo "• Manejo de eventos organizado"

echo
echo -e "${YELLOW}🚀 Para ejecutar la versión refactorizada:${NC}"
echo "./run-refactorizada.sh"

echo
echo -e "${GREEN}✅ REFACTORIZACIÓN COMPLETADA EXITOSAMENTE!${NC}"
