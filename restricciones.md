# Restricciones de Visualización de Barcos en Sistema

## Lógica de Slots para Barcos

### Comportamiento General
- Los barcos ocupan slots dinámicos basados en el máximo número de barcos en sistema durante toda la simulación
- Cada slot representa: `B_SlotN_ID | B_SlotN_Estado | B_SlotN_T_Ingreso`

### Reglas de Asignación de Slots

1. **Asignación inicial**: Un barco ocupa el primer slot libre cuando entra al sistema
2. **Persistencia**: Mantiene ese slot durante toda su estadía (EN_BAHIA → SIENDO_DESCARGADO)
3. **Liberación**: Solo se libera el slot cuando el barco sale del sistema (evento FinDescarga)
4. **Reutilización**: Los slots liberados pueden ser ocupados por nuevos barcos

### Estados a Mostrar
- **EN_BAHIA**: Barco esperando en la cola
- **SIENDO_DESCARGADO**: Barco siendo atendido en un muelle

### Cálculo de Máximo
- `maxBarcosEnSistema` incluye tanto barcos en bahía como los que están siendo descargados
- Se calcula procesando todos los vectores antes de generar la tabla

### Ejemplo de Comportamiento
```
Iteración 1: B_Slot1_ID=1, Estado=SA, T_Ingreso=0.40 | B_Slot2_ID=2, Estado=SA, T_Ingreso=1.2
Iteración 2: B_Slot1_ID=vacío (barco 1 terminó) | B_Slot2_ID=2, Estado=SA, T_Ingreso=1.2
Iteración 3: B_Slot1_ID=3, Estado=SA, T_Ingreso=1.9 | B_Slot2_ID=2, Estado=SA, T_Ingreso=1.2
```

### T_Ingreso
- Representa el momento de entrada al sistema
- Puede ser cuando ingresa a la bahía (si tuvo que hacer cola) 
- O la hora de inicio de descarga (si llegó y fue atendido inmediatamente)
- En ambos casos es cuando el barco aparece por primera vez en el sistema

### Implementación
- La lógica de slots se maneja en `TestSimuladorAvanzado` para mantener separación de responsabilidades
- El `Simulador` mantiene la lista de `barcosEnSistema` en cada `FilaVector`
- Los slots se generan dinámicamente basados en el `maxBarcosEnSistema` calculado
