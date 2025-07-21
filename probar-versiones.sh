#!/bin/bash

# 🔍 Script para probar y comparar ambas versiones de la UI

echo "🚢 ============================================="
echo "   PRUEBA DE COMPATIBILIDAD - ORIGINAL vs REFACTORIZADA"
echo "🚢 ============================================="
echo

GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

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

# Verificar directorio
if [ ! -f "pom.xml" ]; then
    print_error "No se encontró pom.xml. Ejecuta desde el directorio raíz del proyecto."
    exit 1
fi

print_step "1. Compilando proyecto..."
if mvn compile -q; then
    print_success "Compilación exitosa"
else
    print_error "Error en la compilación"
    exit 1
fi

echo
print_step "2. Verificando funcionalidades críticas en el código..."

# Verificar que elementos críticos estén en la versión refactorizada
echo -n "   - Scroll horizontal configurado: "
if grep -q "HORIZONTAL_SCROLLBAR_AS_NEEDED" src/main/java/com/facu/simulation/ui/TablaMejorada.java; then
    echo -e "${GREEN}✓${NC}"
else
    echo -e "${RED}✗${NC}"
fi

echo -n "   - AUTO_RESIZE_OFF configurado: "
if grep -q "AUTO_RESIZE_OFF" src/main/java/com/facu/simulation/ui/TablaMejorada.java; then
    echo -e "${GREEN}✓${NC}"
else
    echo -e "${RED}✗${NC}"
fi

echo -n "   - EventHandler implementado: "
if [ -f "src/main/java/com/facu/simulation/ui/eventos/EventHandlerSimulacion.java" ]; then
    echo -e "${GREEN}✓${NC}"
else
    echo -e "${RED}✗${NC}"
fi

echo -n "   - Valores por defecto preservados: "
if grep -q "VALORES_DEFAULT" src/main/java/com/facu/simulation/ui/paneles/PanelParametros.java; then
    echo -e "${GREEN}✓${NC}"
else
    echo -e "${RED}✗${NC}"
fi

echo -n "   - Campo semilla incluido: "
if grep -q "getSemillaSeed" src/main/java/com/facu/simulation/ui/paneles/PanelParametros.java; then
    echo -e "${GREEN}✓${NC}"
else
    echo -e "${RED}✗${NC}"
fi

echo
print_step "3. Contando líneas de código para comparación..."
echo "   📄 VentanaPrincipal.java (original):"
echo "      $(wc -l src/main/java/com/facu/simulation/ui/VentanaPrincipal.java | cut -d' ' -f1) líneas"

echo "   📄 VentanaPrincipalRefactorizada.java (nueva):"
echo "      $(wc -l src/main/java/com/facu/simulation/ui/VentanaPrincipalRefactorizada.java | cut -d' ' -f1) líneas"

echo "   📁 Archivos refactorizados totales:"
total_lines=0
for file in src/main/java/com/facu/simulation/ui/componentes/*.java \
            src/main/java/com/facu/simulation/ui/paneles/*.java \
            src/main/java/com/facu/simulation/ui/eventos/*.java \
            src/main/java/com/facu/simulation/ui/estilos/*.java \
            src/main/java/com/facu/simulation/ui/VentanaPrincipalRefactorizada.java; do
    if [ -f "$file" ]; then
        lines=$(wc -l "$file" | cut -d' ' -f1)
        echo "      $(basename "$file"): $lines líneas"
        total_lines=$((total_lines + lines))
    fi
done
echo "      📊 Total refactorizado: $total_lines líneas"

echo
print_step "4. Opciones de ejecución disponibles:"
echo -e "${YELLOW}   A) Ejecutar versión ORIGINAL:${NC}"
echo "      mvn exec:java -Dexec.mainClass=\"com.facu.simulation.PuertoSimulationApp\""

echo -e "${YELLOW}   B) Ejecutar versión REFACTORIZADA:${NC}"
echo "      ./run-refactorizada.sh"
echo "      o"
echo "      mvn exec:java -Dexec.mainClass=\"com.facu.simulation.ui.VentanaPrincipalRefactorizada\""

echo
print_warning "¿Qué versión quieres probar?"
echo "1) Versión ORIGINAL"
echo "2) Versión REFACTORIZADA"
echo "3) Salir"
read -p "Selecciona una opción (1-3): " choice

case $choice in
    1)
        print_step "Ejecutando versión ORIGINAL..."
        mvn exec:java -Dexec.mainClass="com.facu.simulation.PuertoSimulationApp" -Dexec.cleanupDaemonThreads=false
        ;;
    2)
        print_step "Ejecutando versión REFACTORIZADA..."
        mvn exec:java -Dexec.mainClass="com.facu.simulation.ui.VentanaPrincipalRefactorizada" -Dexec.cleanupDaemonThreads=false
        ;;
    3)
        print_success "Saliendo..."
        exit 0
        ;;
    *)
        print_error "Opción no válida"
        exit 1
        ;;
esac

print_success "Aplicación cerrada correctamente"
echo -e "${BLUE}👋 ¡Gracias por probar la refactorización!${NC}"
