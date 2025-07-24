package com.facu.simulation.engine;

import lombok.Data;

/**
 * Clase que encapsula todos los parámetros de configuración de la simulación.
 */
@Data
public class ConfiguracionSimulacion {
    // Parámetros de llegadas
    private double mediaLlegadas;
    
    // Parámetros de descarga
    private double tiempoDescargaMin;
    private double tiempoDescargaMax;
    
    // Recursos del puerto
    private int cantidadMuelles;
    private int cantidadGruas;
    
    // Control de simulación
    private double diasSimulacion;
    
    // Control de reporte (filtrado)
    private int mostrarDesde; // Día inicial para mostrar
    private int mostrarHasta; // Día final para mostrar
    private int mostrarFilaDesde; // Fila inicial para mostrar
    private int mostrarFilaHasta; // Fila final para mostrar
    private boolean mostrarPorDia = true; // true: filtra por día, false: filtra por fila
    
    // Semilla para el generador aleatorio
    private long semilla;

    /**
     * Constructor para filtrar por DÍA.
     */
    public ConfiguracionSimulacion(double mediaLlegadas, double tiempoDescargaMin, double tiempoDescargaMax,
                                 int cantidadMuelles, int cantidadGruas, double diasSimulacion,
                                 int mostrarDesde, int mostrarHasta, long semilla) {
        this.mediaLlegadas = mediaLlegadas;
        this.tiempoDescargaMin = tiempoDescargaMin;
        this.tiempoDescargaMax = tiempoDescargaMax;
        this.cantidadMuelles = cantidadMuelles;
        this.cantidadGruas = cantidadGruas;
        this.diasSimulacion = diasSimulacion;
        this.mostrarDesde = mostrarDesde;
        this.mostrarHasta = mostrarHasta;
        this.mostrarPorDia = true;
        this.semilla = semilla;
    }

    /**
     * Constructor para filtrar por NÚMERO DE FILA.
     */
    public ConfiguracionSimulacion(double mediaLlegadas, double tiempoDescargaMin, double tiempoDescargaMax,
                                 int cantidadMuelles, int cantidadGruas, double diasSimulacion,
                                 int mostrarFilaDesde, int mostrarFilaHasta, boolean esPorFila, long semilla) {
        this.mediaLlegadas = mediaLlegadas;
        this.tiempoDescargaMin = tiempoDescargaMin;
        this.tiempoDescargaMax = tiempoDescargaMax;
        this.cantidadMuelles = cantidadMuelles;
        this.cantidadGruas = cantidadGruas;
        this.diasSimulacion = diasSimulacion;
        this.mostrarFilaDesde = mostrarFilaDesde;
        this.mostrarFilaHasta = mostrarFilaHasta;
        this.mostrarPorDia = !esPorFila;
        this.semilla = semilla;
    }

}