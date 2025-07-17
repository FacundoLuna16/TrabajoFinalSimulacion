package com.facu.simulation.ui;

import com.facu.simulation.dto.BarcoSlotDTO;
import com.facu.simulation.dto.FilaVectorDTO;

import java.util.*;

/**
 * Generador de columnas dinámicas para la tabla de simulación.
 * Maneja tanto las columnas base como las columnas dinámicas de barcos.
 */
public class GeneradorColumnasTabla {
    
    // Columnas base fijas de la simulación
    private static final String[] COLUMNAS_BASE = {
        "Fila", "Evento", "Reloj", "RND Llegada", "Prox Llegada",
        "RND Descarga 1", "Fin Descarga 1", "RND Descarga 2", "Fin Descarga 2", "Bahia Cola",
        "Muelle 1 Estado", "M1 Inicio Ocup.", "Muelle 2 Estado", "M2 Inicio Ocup.",
        "Grua 1 Estado", "G1 Inicio Ocup.", "Grua 2 Estado", "G2 Inicio Ocup.",
        "MAX T Perm.", "MIN T Perm.", "AC T Perm.", "AC Cant Barcos", "Media T Perm.",
        "M1 AC T Ocupado", "M1 Util (%)", "M2 AC T Ocupado", "M2 Util (%)",
        "G1 AC T Ocupado", "G1 Util (%)", "G2 AC T Ocupado", "G2 Util (%)",
        "Barcos en Sistema"
    };

    /**
     * Genera el array completo de encabezados incluyendo columnas dinámicas
     */
    public String[] generarEncabezados(int maxBarcosEnSistema) {
        List<String> columnas = new ArrayList<>();
        
        // Agregar columnas base
        columnas.addAll(Arrays.asList(COLUMNAS_BASE));
        
        // Agregar columnas dinámicas de barcos
        for (int i = 1; i <= maxBarcosEnSistema; i++) {
            columnas.add("B_Slot" + i + "_ID");
            columnas.add("B_Slot" + i + "_Estado");
            columnas.add("B_Slot" + i + "_T_Ingreso");
        }
        
        return columnas.toArray(new String[0]);
    }

    /**
     * Genera los datos completos de una fila incluyendo datos de slots
     */
    public Object[] generarDatosFila(FilaVectorDTO fila, int maxBarcosEnSistema) {
        List<Object> datos = new ArrayList<>();
        
        // Agregar datos base
        datos.addAll(extraerDatosBase(fila));
        
        // Agregar datos de slots
        Object[] slotsData = generarDatosSlots(fila.getBarcosEnSistema(), maxBarcosEnSistema);
        datos.addAll(Arrays.asList(slotsData));
        
        return datos.toArray();
    }

    /**
     * Extrae los datos base de la fila (sin slots de barcos)
     */
    private List<Object> extraerDatosBase(FilaVectorDTO fila) {
        List<Object> datos = new ArrayList<>();
        
        // Agregar campos en el mismo orden que COLUMNAS_BASE
        datos.add(fila.getNumeroFila());
        datos.add(fila.getEvento());
        datos.add(formatearTiempo(fila.getTiempo()));
        datos.add(formatearRnd(fila.getRndLlegada()));
        datos.add(formatearTiempo(fila.getProximaLlegada()));
        datos.add(formatearRnd(fila.getRndDescargaMuelle1()));
        datos.add(formatearTiempo(fila.getFinDescarga1()));
        datos.add(formatearRnd(fila.getRndDescargaMuelle2()));
        datos.add(formatearTiempo(fila.getFinDescarga2()));
        datos.add(fila.getCantidadBarcosBahia());
        datos.add(fila.getMuelle1Estado());
        datos.add(formatearTiempo(fila.getMuelle1InicioOcupado()));
        datos.add(fila.getMuelle2Estado());
        datos.add(formatearTiempo(fila.getMuelle2InicioOcupado()));
        datos.add(fila.getGrua1Estado());
        datos.add(formatearTiempo(fila.getGrua1InicioOcupado()));
        datos.add(fila.getGrua2Estado());
        datos.add(formatearTiempo(fila.getGrua2InicioOcupado()));
        datos.add(formatearTiempo(fila.getMaxTiempoPermanencia()));
        datos.add(formatearTiempo(fila.getMinTiempoPermanencia()));
        datos.add(formatearTiempo(fila.getAcumuladorTiempoEsperaBahia()));
        datos.add(fila.getContadorBarcosAtendidos());
        datos.add(formatearTiempo(fila.getMediaTiempoPermanencia()));
        datos.add(formatearTiempo(fila.getMuelle1AcTiempoOcupado()));
        datos.add(String.format("%.2f", fila.getMuelle1Utilizacion()));
        datos.add(formatearTiempo(fila.getMuelle2AcTiempoOcupado()));
        datos.add(String.format("%.2f", fila.getMuelle2Utilizacion()));
        datos.add(formatearTiempo(fila.getGrua1AcTiempoOcupado()));
        datos.add(String.format("%.2f", fila.getGrua1Utilizacion()));
        datos.add(formatearTiempo(fila.getGrua2AcTiempoOcupado()));
        datos.add(String.format("%.2f", fila.getGrua2Utilizacion()));
        datos.add(fila.getCantBarcosEnSistema());
        
        return datos;
    }

    /**
     * Genera los datos de slots de barcos para la fila
     */
    private Object[] generarDatosSlots(List<BarcoSlotDTO> barcosEnSistema, int maxSlots) {
        Object[] datos = new Object[maxSlots * 3]; // 3 columnas por slot
        
        // Inicializar todos los slots como vacíos
        for (int i = 0; i < datos.length; i++) {
            datos[i] = "";
        }
        
        // Llenar datos de barcos actuales
        if (barcosEnSistema != null) {
            for (BarcoSlotDTO barco : barcosEnSistema) {
                int slot = barco.getSlotAsignado();
                if (slot >= 1 && slot <= maxSlots) {
                    int indiceBase = (slot - 1) * 3; // 0-indexed
                    datos[indiceBase] = barco.getId();
                    datos[indiceBase + 1] = barco.getEstado();
                    datos[indiceBase + 2] = String.format("%.2f", barco.getTiempoIngreso());
                }
            }
        }
        
        return datos;
    }

    // Métodos de formateo
    private String formatearRnd(double valor) {
        return (valor == -1.0) ? "" : String.format("%.4f", valor);
    }

    private String formatearTiempo(double valor) {
        return (valor == -1.0 || valor == 0.0) ? "" : String.format("%.2f", valor);
    }
}
