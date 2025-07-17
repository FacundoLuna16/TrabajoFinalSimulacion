# 🚢 Simulación del Puerto - Ejercicio 64

## 📖 Descripción

Este proyecto implementa una simulación discreta del problema del puerto usando Java 21, Maven, Swing y FlatLaf. La simulación modela el comportamiento de barcos, muelles y grúas durante 90 días para calcular métricas de rendimiento.

## 🎯 Objetivo

Simular el sistema del puerto para estimar:
- Tiempo de permanencia **mínimo**, **máximo** y **promedio** de los barcos en la bahía
- **Grado de utilización** de cada muelle y cada grúa

## ⚙️ Parámetros del Sistema

- **Media de tiempo entre llegadas:** 1,25 días (distribución Exponencial Negativa)
- **Tiempo de descarga con 1 grúa:** 0,5 - 1,5 días (distribución Uniforme)
- **Número de Muelles:** 2
- **Número de Grúas:** 2
- **Duración de simulación:** 90 días

## 🏗️ Tecnologías Utilizadas

- **Java 21** - Lenguaje de programación principal
- **Maven 3.9.11** - Gestión de dependencias y construcción
- **Swing** - Framework para la interfaz gráfica (GUI)
- **FlatLaf 3.7** - Look and Feel moderno para Swing
- **Lombok 1.18.38** - Reducción de código boilerplate
- **JUnit 5.10.1** - Framework de testing

## 📁 Estructura del Proyecto

```
TrabajoTpSim/
├── src/
│   ├── main/
│   │   ├── java/com/facu/simulation/
│   │   │   ├── PuertoSimulationApp.java    # Clase principal
│   │   │   ├── model/                      # Modelos (Barco, Muelle, Grúa)
│   │   │   ├── engine/                     # Motor de simulación
│   │   │   ├── events/                     # Eventos del sistema
│   │   │   ├── ui/                         # Interfaz gráfica
│   │   │   └── statistics/                 # Cálculo de estadísticas
│   │   └── resources/                      # Recursos (iconos, etc.)
│   └── test/                               # Tests unitarios
├── pom.xml                                 # Configuración de Maven
└── README.md                               # Este archivo
```
 M2`
- **Atributos:** `ID`, `HoraLlegadaBahia`, `TiempoRestanteDescarga`

#### **Muelle** (Permanente, 2 unidades)
- **Estados:** `Libre`, `Ocupado`
- **Atributos:** `BarcoAtendido`, `GruasAsignadas`

#### **Grúa** (Permanente, 2 unidades)
- **Estados:** `Libre`, `Ocupada`
- **Atributos:** `MuelleAsignado`

#### **Bahía** (Cola FIFO)
- **Disciplina:** First In, First Out
- **Atributos:** `CantidadEnCola`

### Eventos Principales

1. **INICIALIZACION** - Configuración inicial del sistema
2. **Llegada_Barco** - Nuevo barco entra al sistema
3. **Fin_Descarga_Muelle_X** - Barco termina servicio y libera recursos

### Reglas Clave

#### **Regla 1: Asignación Dinámica de Grúas**
- **1 barco** en puerto → **2 grúas** asignadas (tiempo ÷ 2)
- **2 barcos** en puerto → **1 grúa** cada uno

#### **Regla 2: Recálculo de Tiempo de Servicio**
- **Grúa se va:** `Tiempo_Restante * 2`
- **Grúa llega:** `Tiempo_Restante / 2`

#### **Regla 3: Eventos Simultáneos**
- Prioridad: `Fin_Descarga` antes que `Llegada_Barco`

## 📊 Vector de Estado

| Control | Eventos Futuros | Colas | Objetos Permanentes | Estadísticas | Objetos Temporales |
|---------|-----------------|-------|---------------------|--------------|-------------------|
| Evento | RND Llegada | Cola Bahía | Muelle 1 | Acum. T. Espera | Barco 1 |
| Reloj | T.E. Llegada | | Estado M1 | Cont. Barcos | Estado B1 |
| | Próx. Llegada | | Muelle 2 | | Hora Lleg. B1 |
| | Fin Descarga M1 | | Estado M2 | | T. Restante B1 |

## 📈 Métricas Calculadas

- **Tiempo Promedio en Bahía:** `Acumulador_Tiempo_Espera / Contador_Barcos`
- **Utilización Muelle:** `Tiempo_Ocupado_Muelle / Reloj_Final`
- **Utilización Grúa:** `Tiempo_Ocupada_Grua / Reloj_Final`

## 🧪 Testing

```bash
# Ejecutar tests unitarios
mvn test

# Ejecutar tests con reporte de cobertura
mvn test jacoco:report
```

## 🤝 Contribución

1. Fork el proyecto
2. Crear una rama para la funcionalidad (`git checkout -b feature/nueva-funcionalidad`)
3. Commit los cambios (`git commit -m 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abrir un Pull Request

## 📄 Licencia

Este proyecto es de uso académico para el curso de Simulación.

---

**Autor:** Facu  
**Versión:** 1.0.0  
**Fecha:** 2025
