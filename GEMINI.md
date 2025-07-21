# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a **discrete event simulation** of a port system in Java 21 that models ships, docks, and cranes over a 90-day period. The simulation uses the **event-driven architecture** to calculate performance metrics like ship waiting times and resource utilization.

**🎯 RECENT MAJOR UPDATE**: The UI layer has been **completely refactorized** from a monolithic design to a modern, modular architecture with separation of concerns.

## Key Technologies

- **Java 21** with Maven as build tool
- **Swing** with FlatLaf Look and Feel for GUI (now with modern modular architecture)
- **Lombok** for boilerplate reduction
- **JUnit 5** for testing

## Common Development Commands

### Build and Run
```bash
# Clean and compile
mvn clean compile

# Run the original application
mvn exec:java

# Run the REFACTORED version (recommended)
./run-refactorizada.sh
# OR
mvn exec:java -Dexec.mainClass="com.facu.simulation.ui.VentanaPrincipalRefactorizada"

# Alternative: use convenience scripts
./run.sh          # Original version
./run-swing.sh    # Original Swing version

# Build executable JAR
mvn clean package
```

### Testing and Verification
```bash
# Run all tests
mvn test

# Verify refactorization structure
./verificar-refactorizacion.sh

# Check compilation of refactored code
mvn clean compile -q
```

### Main Entry Points
- **Original Application**: `PuertoSimulationApp.java:54` with Swing GUI
- **Refactored Application**: `VentanaPrincipalRefactorizada.java:207` with modular architecture
- **Main class**: `com.facu.simulation.PuertoSimulationApp` (original) or `com.facu.simulation.ui.VentanaPrincipalRefactorizada` (refactored)

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

### 🎯 **UI Layer - REFACTORED ARCHITECTURE (`ui/`)**

#### **📂 Core Components**
- **`VentanaPrincipal.java`**: Original monolithic UI (1131 lines) - PRESERVED as backup
- **`VentanaPrincipalRefactorizada.java`**: **NEW** - Modern, clean main window with modular design

#### **📂 UI/componentes/** - Component Factory Pattern
- **`FactoryComponentes.java`**: **NEW** - Centralized factory for creating UI components with consistent styling
- **`TarjetaEstadistica.java`**: **NEW** - Reusable dashboard-style statistical cards with hover effects

#### **📂 UI/paneles/** - Specialized Panels with Single Responsibility
- **`PanelParametros.java`**: **NEW** - Encapsulates all simulation configuration fields with validation
- **`PanelControles.java`**: **NEW** - Contains simulation control buttons (Run/Reset) and filters
- **`PanelEstadisticas.java`**: **NEW** - Modern dashboard displaying results with interactive cards

#### **📂 UI/eventos/** - Event Handling Separation
- **`EventHandlerSimulacion.java`**: **NEW** - Centralized event handler managing all UI interactions and simulation logic

#### **📂 UI/estilos/** - Design System
- **`TemaOscuro.java`**: **NEW** - Centralized design tokens (colors, fonts, dimensions, spacing)
- **`EfectosVisuales.java`**: **NEW** - Custom UI effects (hover, focus, shadows, modern scrollbars)

#### **🎨 UI Architecture Benefits**
1. **Separation of Concerns**: Each panel has a single, well-defined responsibility
2. **Factory Pattern**: Consistent component creation with unified styling
3. **Centralized Theming**: All visual constants in one place for easy modification
4. **Event Isolation**: All event logic centralized in dedicated handler
5. **Reusable Components**: Cards and components can be used across different views
6. **Modern Design**: Dark theme with hover effects and professional appearance

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
- **NEW**: `EventHandlerSimulacion.ejecutarSimulacion()`: Centralized simulation execution with UI updates
- **NEW**: `PanelParametros.validarCampos()`: Input validation separated from UI logic

### Random Number Generation
- Uses `GeneradorAleatorio.java` with fixed seed (12345) for reproducibility
- Exponential distribution for ship arrivals (mean = 1.25 days)
- Uniform distribution for discharge times (0.5 - 1.5 days)

### UI Technology Stack
- **Base**: Java Swing with custom Look and Feel configuration
- **Architecture**: **MVVM-inspired** with View (Panels), ViewModel (EventHandler), Model (Simulation Engine)
- **Design Pattern**: Factory Pattern for component creation
- **Theming**: Centralized design system with dark theme
- **Layout**: GridBagLayout for responsive design
- **Components**: Custom statistical cards, modern scrollbars, hover effects

## Important Implementation Details

- **Time Units**: All simulation time is in **days** 
- **Event Processing**: Uses priority queue for Future Event List (FEL)
- **State Tracking**: System generates detailed trace vectors for analysis
- **Resource Management**: Tracks utilization statistics for each dock and crane individually
- **FIFO Queue**: Ships waiting in bay are served in First-In-First-Out order
- **🎯 UI State Management**: Each panel manages its own state, event handler coordinates between panels
- **🎯 Component Lifecycle**: Factory ensures consistent initialization and styling across all components

## Testing Strategy

- Test classes mirror the main package structure under `src/test/`
- Focus on testing the simulation engine logic, especially event processing and resource allocation
- Use JUnit 5 for unit tests
- **NEW**: UI components are designed to be testable in isolation
- **NEW**: Event handler can be unit tested separately from UI components

## Development Notes

### Simulation Engine
- The simulation uses a **fixed seed** for random number generation to ensure reproducible results
- All statistical calculations are performed during simulation execution
- The GUI displays both real-time simulation state and final performance metrics

### 🎯 **UI Development Guidelines**
1. **Adding New UI Components**: Use `FactoryComponentes` for consistent styling
2. **Modifying Colors/Fonts**: Update `TemaOscuro.java` constants
3. **New Panels**: Follow the pattern established by existing panels in `ui/paneles/`
4. **Event Handling**: Add new events to `EventHandlerSimulacion.java`
5. **Visual Effects**: Use or extend methods in `EfectosVisuales.java`

### 🚀 **Extending the Refactored UI**
- **New Statistical Cards**: Extend `TarjetaEstadistica` or create similar components
- **Additional Panels**: Follow single-responsibility principle, create in `ui/paneles/`
- **New Themes**: Create new theme classes similar to `TemaOscuro.java`
- **Custom Components**: Use factory pattern for consistency

## Files and Documentation

### Core Documentation
- `README.md`: Project documentation
- `REFACTORIZACION.md`: Detailed refactoring documentation
- `RESUMEN_FINAL.md`: Executive summary of refactoring work

### Execution Scripts
- `run-refactorizada.sh`: **Execute refactored version** (recommended)
- `verificar-refactorizacion.sh`: Verify refactored file structure
- `run.sh`, `run-swing.sh`: Original execution scripts

## Migration Path

### From Original to Refactored UI
1. **Current**: Both versions coexist - original `VentanaPrincipal.java` is preserved
2. **Testing**: Use `VentanaPrincipalRefactorizada.java` for new features
3. **Migration**: When ready, replace original with refactored version
4. **Rollback**: Original version always available as backup

### 🎯 **Recommended Approach**
- **New Development**: Use the refactored architecture
- **Bug Fixes**: Can be applied to both versions during transition
- **UI Enhancements**: Implement in refactored version first
- **Testing**: Verify both versions maintain identical simulation logic

---

**✅ The refactored UI maintains 100% functional compatibility while providing a modern, maintainable, and extensible architecture.**
