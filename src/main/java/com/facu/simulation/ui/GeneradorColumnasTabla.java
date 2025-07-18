package com.facu.simulation.ui;

import com.facu.simulation.dto.BarcoSlotDTO;
import com.facu.simulation.dto.FilaVectorDTO;

import java.util.*;

/**
 * Generador de columnas dinámicas para la tabla de simulación.
 * Maneja tanto las columnas base como las columnas dinámicas de barcos.
 */
public class GeneradorColumnasTabla {
    
    // Columnas base fijas de la simulación según el nuevo orden solicitado
    private static final String[] COLUMNAS_BASE = {
        "Fila", "Evento", "Reloj", "RNDLleg", "ProxLleg", 
        "RNDM1", "TiemRest1", "FinDescM1", "RNDM2", "TiemRest2", "FinDescM2", "Bahía",
        "M1Est", "M1Inic", "M2Est", "M2Inic", "G1Est", "G1Inic", "G2Est", "G2Inic",
        "MaxTPer", "MinTPer", "AcTPer", "CantB", "MedTPer",
        "M1AcTOc", "M1Ut%", "M2AcTOc", "M2Ut%", "G1AcTOc", "G1Ut%", "G2AcTOc", "G2Ut%",
        "BSist"
    };

    /**
     * Genera el array completo de encabezados incluyendo columnas dinámicas
     */
    public String[] generarEncabezados(int maxBarcosEnSistema) {
        List<String> columnas = new ArrayList<>();
        
        // Agregar columnas base
        columnas.addAll(Arrays.asList(COLUMNAS_BASE));
        
        // Agregar columnas dinámicas de barcos con el formato solicitado
        for (int i = 1; i <= maxBarcosEnSistema; i++) {
            columnas.add("B" + i + "_ID");
            columnas.add("B" + i + "_Estado");
            columnas.add("B" + i + "_Ingr");
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
        
        // Agregar campos en el nuevo orden solicitado
        datos.add(fila.getNumeroFila());                    // "Fila"
        datos.add(fila.getEvento());                        // "Evento"
        datos.add(formatearTiempo(fila.getTiempo()));       // "Reloj"
        datos.add(formatearRnd(fila.getRndLlegada()));      // "RNDLleg"
        datos.add(formatearTiempo(fila.getProximaLlegada())); // "ProxLleg"
        datos.add(formatearRnd(fila.getRndDescargaMuelle1())); // "RNDM1"
        datos.add(formatearTiempo(fila.getTiempoRestanteMuelle1())); // "TiemRest1" - NUEVO
        datos.add(formatearTiempo(fila.getFinDescarga1())); // "FinDescM1"
        datos.add(formatearRnd(fila.getRndDescargaMuelle2())); // "RNDM2"
        datos.add(formatearTiempo(fila.getTiempoRestanteMuelle2())); // "TiemRest2" - NUEVO
        datos.add(formatearTiempo(fila.getFinDescarga2())); // "FinDescM2"
        datos.add(fila.getCantidadBarcosBahia());           // "Bahía"
        datos.add(fila.getMuelle1Estado());                // "M1Est"
        datos.add(formatearTiempo(fila.getMuelle1InicioOcupado())); // "M1Inic"
        datos.add(fila.getMuelle2Estado());                // "M2Est"
        datos.add(formatearTiempo(fila.getMuelle2InicioOcupado())); // "M2Inic"
        datos.add(fila.getGrua1Estado());                  // "G1Est"
        datos.add(formatearTiempo(fila.getGrua1InicioOcupado())); // "G1Inic"
        datos.add(fila.getGrua2Estado());                  // "G2Est"
        datos.add(formatearTiempo(fila.getGrua2InicioOcupado())); // "G2Inic"
        datos.add(formatearTiempo(fila.getMaxTiempoPermanencia())); // "MaxTPer"
        datos.add(formatearTiempo(fila.getMinTiempoPermanencia())); // "MinTPer"
        datos.add(formatearTiempo(fila.getAcumuladorTiempoEsperaBahia())); // "AcTPer"
        datos.add(fila.getContadorBarcosQueEsperaronEnBahia());       // "CantB" - Ahora cuenta barcos que esperaron en bahía
        datos.add(formatearTiempo(fila.getMediaTiempoPermanencia())); // "MedTPer"
        datos.add(formatearTiempo(fila.getMuelle1AcTiempoOcupado())); // "M1AcTOc"
        datos.add(String.format("%.2f", fila.getMuelle1Utilizacion())); // "M1Ut%"
        datos.add(formatearTiempo(fila.getMuelle2AcTiempoOcupado())); // "M2AcTOc"
        datos.add(String.format("%.2f", fila.getMuelle2Utilizacion())); // "M2Ut%"
        datos.add(formatearTiempo(fila.getGrua1AcTiempoOcupado())); // "G1AcTOc"
        datos.add(String.format("%.2f", fila.getGrua1Utilizacion())); // "G1Ut%"
        datos.add(formatearTiempo(fila.getGrua2AcTiempoOcupado())); // "G2AcTOc"
        datos.add(String.format("%.2f", fila.getGrua2Utilizacion())); // "G2Ut%"
        datos.add(fila.getCantBarcosEnSistema());           // "BSist"
        
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
