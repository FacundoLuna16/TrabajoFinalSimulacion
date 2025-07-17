#!/bin/bash

# Script para compilar y ejecutar las mejoras de la tabla
echo "🔧 Compilando proyecto TrabajoTpSim..."

# Navegar al directorio del proyecto
cd /home/facu/proyectos/TrabajoTpSim

# Compilar con Maven
echo "📦 Compilando con Maven..."
mvn clean compile -q

if [ $? -eq 0 ]; then
    echo "✅ Compilación exitosa!"
    echo ""
    echo "🚀 Opciones de ejecución:"
    echo "1. Ejecutar aplicación completa"
    echo "2. Ejecutar prueba de tabla mejorada"
    echo "3. Salir"
    echo ""
    
    read -p "Selecciona una opción (1-3): " opcion
    
    case $opcion in
        1)
            echo "🏃 Ejecutando aplicación completa..."
            java -cp target/classes com.facu.simulation.PuertoSimulationApp
            ;;
        2)
            echo "🧪 Ejecutando prueba de tabla mejorada..."
            java -cp target/classes com.facu.simulation.ui.GeneradorDatosPrueba
            ;;
        3)
            echo "👋 ¡Hasta luego!"
            exit 0
            ;;
        *)
            echo "❌ Opción no válida"
            exit 1
            ;;
    esac
else
    echo "❌ Error en la compilación"
    exit 1
fi
