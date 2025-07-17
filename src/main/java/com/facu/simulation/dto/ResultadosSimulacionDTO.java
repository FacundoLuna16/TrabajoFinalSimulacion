package com.facu.simulation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object que encapsula todos los resultados de una simulación.
 * Incluye tanto la tabla de vectores de estado como las estadísticas finales.
 */
@Data
@NoArgsConstructor
public class ResultadosSimulacionDTO {
    
    // Lista de filas para mostrar en la tabla principal
    private List<FilaVectorDTO> filasTabla;
    
    // Estadísticas de tiempo de permanencia
    private double tiempoPermaneciaMedio;
    private double tiempoPermanciaMinimo;
    private double tiempoPermanciaMaximo;
    
    // Estadísticas generales
    private int totalBarcosGenerados;
    private int totalBarcosAtendidos;
    private double tiempoTotalSimulacion;
    
    // Utilizaciones por recurso individual
    private double[] utilizacionMuelles; // Array con utilización de cada muelle
    private double[] utilizacionGruas;   // Array con utilización de cada grúa
    
    // NUEVO: Campo para columnas dinámicas
    private int maxBarcosEnSistema; // Máximo número de barcos simultáneos para generar columnas
    
    public ResultadosSimulacionDTO(List<FilaVectorDTO> filasTabla,
                                 double tiempoPermaneciaMedio,
                                 double tiempoPermanciaMinimo,
                                 double tiempoPermanciaMaximo,
                                 int totalBarcosGenerados,
                                 int totalBarcosAtendidos,
                                 double tiempoTotalSimulacion,
                                 double[] utilizacionMuelles,
                                 double[] utilizacionGruas,
                                 int maxBarcosEnSistema) {
        this.filasTabla = filasTabla;
        this.tiempoPermaneciaMedio = tiempoPermaneciaMedio;
        this.tiempoPermanciaMinimo = tiempoPermanciaMinimo;
        this.tiempoPermanciaMaximo = tiempoPermanciaMaximo;
        this.totalBarcosGenerados = totalBarcosGenerados;
        this.totalBarcosAtendidos = totalBarcosAtendidos;
        this.tiempoTotalSimulacion = tiempoTotalSimulacion;
        this.utilizacionMuelles = utilizacionMuelles;
        this.utilizacionGruas = utilizacionGruas;
        this.maxBarcosEnSistema = maxBarcosEnSistema;
    }
}

