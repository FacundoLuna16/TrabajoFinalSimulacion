package com.facu.simulation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para representar un barco en un slot específico de la tabla.
 * Implementa la lógica de slots persistentes.
 */
@Data
@NoArgsConstructor
public class BarcoSlotDTO {
    private int id;
    private String estado; // "EB" (En Bahía) o "SD" (Siendo Descargado)
    private double tiempoIngreso; // Momento en que ingresó al sistema
    private double tiempoInicioDescarga; // Momento en que comenzó la descarga
    private int slotAsignado; // Número de slot (1, 2, 3, etc.)

    public BarcoSlotDTO(int id, String estado, double tiempoIngreso, double tiempoInicioDescarga, int slotAsignado) {
        this.id = id;
        this.estado = estado;
        this.tiempoIngreso = tiempoIngreso;
        this.tiempoInicioDescarga = tiempoInicioDescarga;
        this.slotAsignado = slotAsignado;
    }

    /**
     * Mapea el estado interno del barco a la representación de la UI
     */
    public static String mapearEstado(com.facu.simulation.model.EstadoBarco estado) {
        if (estado == null) return "";
        switch (estado) {
            case EN_BAHIA: return "EB";
            case SIENDO_DESCARGADO: return "SD";
            default: return estado.toString();
        }
    }
}
