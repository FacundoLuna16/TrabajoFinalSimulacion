#!/bin/bash

# Script para compilar y ejecutar la aplicación de simulación portuaria
# Este script debe ejecutarse desde el directorio raíz del proyecto

echo "🚢 Compilando aplicación de simulación portuaria..."
mvn clean compile

if [ $? -eq 0 ]; then
    echo "✅ Compilación exitosa"
    echo "🚀 Ejecutando aplicación..."
    mvn exec:java -Dexec.mainClass="com.facu.simulation.PuertoSimulationApp"
else
    echo "❌ Error en la compilación"
    exit 1
fi
