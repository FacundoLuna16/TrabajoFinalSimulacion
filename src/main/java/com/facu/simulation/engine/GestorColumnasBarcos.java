package com.facu.simulation.engine;

import lombok.Data;
import java.util.*;

/**
 * Gestor inteligente de columnas de barcos con reutilización.
 * Maneja la asignación y liberación de columnas para barcos en sistema,
 * optimizando el uso de espacio en la tabla.
 */
@Data
public class GestorColumnasBarcos {
    private Map<Integer, Integer> barcoAColumna = new HashMap<>();
    private Set<Integer> columnasLibres = new HashSet<>();
    private int maxColumnasCreadas = 0;
    private int maxBarcosSimultaneos;
    
    // Estadísticas para debugging
    private int totalAsignaciones = 0;
    private int totalLiberaciones = 0;
    private int reutilizacionesEfectuadas = 0;
    
    /**
     * Constructor con máximo de barcos simultáneos conocido
     */
    public GestorColumnasBarcos(int maxBarcosSimultaneos) {
        this.maxBarcosSimultaneos = maxBarcosSimultaneos;
        // Pre-inicializar columnas libres
        for (int i = 0; i < maxBarcosSimultaneos; i++) {
            columnasLibres.add(i);
        }
        maxColumnasCreadas = maxBarcosSimultaneos;
    }
    
    /**
     * Asigna una columna a un barco específico.
     * Reutiliza columnas libres o crea nueva si es necesario.
     * 
     * @param idBarco ID del barco que necesita columna
     * @return Índice de la columna asignada
     */
    public int asignarColumna(int idBarco) {
        totalAsignaciones++;
        
        // Verificar si el barco ya tiene columna asignada
        if (barcoAColumna.containsKey(idBarco)) {
            return barcoAColumna.get(idBarco);
        }
        
        int columnaAsignada;
        
        // Intentar reutilizar columna libre
        if (!columnasLibres.isEmpty()) {
            columnaAsignada = columnasLibres.iterator().next();
            columnasLibres.remove(columnaAsignada);
            reutilizacionesEfectuadas++;
        } else {
            // Crear nueva columna (esto no debería pasar con pre-análisis)
            columnaAsignada = maxColumnasCreadas;
            maxColumnasCreadas++;
        }
        
        // Registrar asignación
        barcoAColumna.put(idBarco, columnaAsignada);
        
        return columnaAsignada;
    }
    
    /**
     * Libera la columna de un barco específico.
     * La columna queda disponible para reutilización.
     * 
     * @param idBarco ID del barco que libera su columna
     * @return true si se liberó exitosamente, false si no tenía columna
     */
    public boolean liberarColumna(int idBarco) {
        totalLiberaciones++;
        
        Integer columna = barcoAColumna.remove(idBarco);
        if (columna != null) {
            columnasLibres.add(columna);
            return true;
        }
        return false;
    }
    
    /**
     * Obtiene la columna asignada a un barco específico.
     * 
     * @param idBarco ID del barco
     * @return Índice de columna o -1 si no tiene asignada
     */
    public int obtenerColumna(int idBarco) {
        return barcoAColumna.getOrDefault(idBarco, -1);
    }
    
    /**
     * Verifica si un barco tiene columna asignada.
     * 
     * @param idBarco ID del barco
     * @return true si tiene columna asignada
     */
    public boolean tieneColumna(int idBarco) {
        return barcoAColumna.containsKey(idBarco);
    }
    
    /**
     * Obtiene todos los barcos con columnas asignadas actualmente.
     * 
     * @return Set con IDs de barcos activos
     */
    public Set<Integer> obtenerBarcosActivos() {
        return new HashSet<>(barcoAColumna.keySet());
    }
    
    /**
     * Obtiene el número total de columnas creadas.
     * 
     * @return Número total de columnas
     */
    public int obtenerTotalColumnas() {
        return maxColumnasCreadas;
    }
    
    /**
     * Obtiene el número de columnas actualmente ocupadas.
     * 
     * @return Número de columnas ocupadas
     */
    public int obtenerColumnasOcupadas() {
        return barcoAColumna.size();
    }
    
    /**
     * Obtiene el número de columnas libres disponibles.
     * 
     * @return Número de columnas libres
     */
    public int obtenerColumnasLibres() {
        return columnasLibres.size();
    }
    
    /**
     * Genera estadísticas de uso del gestor.
     * 
     * @return String con estadísticas formateadas
     */
    public String generarEstadisticas() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ESTADÍSTICAS GESTOR COLUMNAS ===\n");
        sb.append("Máximo barcos simultáneos: ").append(maxBarcosSimultaneos).append("\n");
        sb.append("Columnas creadas: ").append(maxColumnasCreadas).append("\n");
        sb.append("Columnas ocupadas: ").append(obtenerColumnasOcupadas()).append("\n");
        sb.append("Columnas libres: ").append(obtenerColumnasLibres()).append("\n");
        sb.append("Total asignaciones: ").append(totalAsignaciones).append("\n");
        sb.append("Total liberaciones: ").append(totalLiberaciones).append("\n");
        sb.append("Reutilizaciones: ").append(reutilizacionesEfectuadas).append("\n");
        
        if (totalAsignaciones > 0) {
            double eficiencia = (double) reutilizacionesEfectuadas / totalAsignaciones * 100;
            sb.append("Eficiencia reutilización: ").append(String.format("%.2f%%", eficiencia)).append("\n");
        }
        
        return sb.toString();
    }
    
    /**
     * Resetea el gestor para una nueva simulación.
     */
    public void reset() {
        barcoAColumna.clear();
        columnasLibres.clear();
        
        // Re-inicializar columnas libres
        for (int i = 0; i < maxBarcosSimultaneos; i++) {
            columnasLibres.add(i);
        }
        
        maxColumnasCreadas = maxBarcosSimultaneos;
        totalAsignaciones = 0;
        totalLiberaciones = 0;
        reutilizacionesEfectuadas = 0;
    }
    
    /**
     * Valida la consistencia interna del gestor.
     * 
     * @return true si el estado es consistente
     */
    public boolean validarConsistencia() {
        // Verificar que no haya duplicados entre ocupadas y libres
        Set<Integer> ocupadas = new HashSet<>(barcoAColumna.values());
        Set<Integer> intersection = new HashSet<>(ocupadas);
        intersection.retainAll(columnasLibres);
        
        if (!intersection.isEmpty()) {
            return false; // Hay columnas marcadas como ocupadas y libres
        }
        
        // Verificar que el total de columnas sea consistente
        int totalEsperado = ocupadas.size() + columnasLibres.size();
        if (totalEsperado > maxColumnasCreadas) {
            return false; // Más columnas registradas que creadas
        }
        
        return true;
    }
    
    /**
     * Obtiene información detallada del estado actual.
     * 
     * @return String con información detallada
     */
    public String obtenerEstadoDetallado() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ESTADO DETALLADO GESTOR ===\n");
        
        sb.append("Asignaciones actuales:\n");
        for (Map.Entry<Integer, Integer> entry : barcoAColumna.entrySet()) {
            sb.append("  Barco ").append(entry.getKey()).append(" → Columna ").append(entry.getValue()).append("\n");
        }
        
        sb.append("Columnas libres: ").append(columnasLibres).append("\n");
        sb.append("Validación: ").append(validarConsistencia() ? "✓ CONSISTENTE" : "✗ INCONSISTENTE").append("\n");
        
        return sb.toString();
    }
}
