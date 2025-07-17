#!/bin/bash

echo "🚀 INICIANDO PRUEBA DE INTEGRACIÓN UI COMPLETA"
echo "================================================="

cd /home/facu/proyectos/TrabajoTpSim

# Compilar todo el proyecto
echo "📦 Compilando proyecto..."
mvn clean compile

if [ $? -eq 0 ]; then
    echo "✅ Compilación exitosa"
else
    echo "❌ Error en compilación"
    exit 1
fi

# Ejecutar la aplicación principal
echo "🖥️  Iniciando interfaz gráfica..."
mvn exec:java -Dexec.mainClass="com.facu.simulation.PuertoSimulationApp"

echo "🎯 Prueba de integración completada"
