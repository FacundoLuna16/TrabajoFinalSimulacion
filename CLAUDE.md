# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a **discrete event simulation** of a port system in Java 21 that models ships, docks, and cranes over a 90-day period. The simulation uses the **event-driven architecture** to calculate performance metrics like ship waiting times and resource utilization.

## Key Technologies

- **Java 21** with Maven as build tool
- **Swing** with FlatLaf Look and Feel for GUI
- **Lombok** for boilerplate reduction
- **JUnit 5** for testing

## Common Development Commands

### Build and Run
```bash
# Clean and compile
mvn clean compile

# Run the application
mvn exec:java

# Alternative: use the convenience script
./run.sh

# Build executable JAR
mvn clean package
```

### Testing
```bash
# Run all tests
mvn test

# Run tests with coverage (if jacoco plugin is added)
mvn test jacoco:report
```

### Main Entry Point
- Application starts at `PuertoSimulationApp.java:54` with Swing GUI
- Main class: `com.facu.simulation.PuertoSimulationApp`

## Architecture Overview

### Core Simulation Engine (`engine/`)
- **`Simulador.java`**: Central discrete event simulation engine that manages the Future Event List (FEL), system clock, and coordinates all simulation logic
- **`ConfiguracionSimulacion.java`**: Configuration parameters including arrival rates, service times, and resource counts
- **`FilaVector.java`**: Represents system state snapshots for the simulation trace table

### Event System (`events/`)
- **`Evento.java`**: Base abstract class for all simulation events
- **`LlegadaBarco.java`**: Ship arrival event 
- **`FinDescarga.java`**: Ship discharge completion event

### Domain Models (`model/`)
- **`Barco.java`**: Ship entity with states (EN_BAHIA, SIENDO_DESCARGADO, DESCARGADO)
- **`Muelle.java`**: Dock resource that can be LIBRE or OCUPADO
- **`Grua.java`**: Crane resource that can be LIBRE or OCUPADA
- **Estado* enums**: Define valid states for each entity

### Key Business Rules
1. **Dynamic Crane Assignment**: 
   - 1 ship in port → 2 cranes assigned (halves service time)
   - 2 ships in port → 1 crane each
2. **Service Time Recalculation**: When cranes are reassigned, remaining service times are recalculated
3. **Event Priority**: FinDescarga events are processed before LlegadaBarco events when they occur at the same time

### Critical Methods
- `Simulador.procesarLlegadaBarco()`: Handles ship arrivals and dock assignment
- `Simulador.procesarFinDescarga()`: Handles service completion and resource release
- `Simulador.reasignarGruasYRecalcularTiempos()`: Core logic for crane reassignment and time recalculation

### Random Number Generation
- Uses `GeneradorAleatorio.java` with fixed seed (12345) for reproducibility
- Exponential distribution for ship arrivals (mean = 1.25 days)
- Uniform distribution for discharge times (0.5 - 1.5 days)

### UI Layer (`ui/`)
- **`VentanaPrincipal.java`**: Main Swing window with simulation controls and results display
- Uses FlatLaf for modern Look and Feel

## Important Implementation Details

- **Time Units**: All simulation time is in **days** 
- **Event Processing**: Uses priority queue for Future Event List (FEL)
- **State Tracking**: System generates detailed trace vectors for analysis
- **Resource Management**: Tracks utilization statistics for each dock and crane individually
- **FIFO Queue**: Ships waiting in bay are served in First-In-First-Out order

## Testing Strategy

- Test classes mirror the main package structure under `src/test/`
- Focus on testing the simulation engine logic, especially event processing and resource allocation
- Use JUnit 5 for unit tests

## Development Notes

- The simulation uses a **fixed seed** for random number generation to ensure reproducible results
- All statistical calculations are performed during simulation execution
- The GUI displays both real-time simulation state and final performance metrics