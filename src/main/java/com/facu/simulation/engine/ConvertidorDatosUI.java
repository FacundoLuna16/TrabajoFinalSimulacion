package com.facu.simulation.engine;

import com.facu.simulation.dto.*;
import com.facu.simulation.model.Barco;
import com.facu.simulation.model.EstadoBarco;

import java.util.*;

/**
 * Convertidor de datos del motor de simulación a DTOs para la UI.
 * Maneja la lógica de slots persistentes y la conversión de tipos.
 */
public class ConvertidorDatosUI {

    /**
     * Convierte los vectores de estado a DTOs listos para la UI
     */
    public static ResultadosSimulacionDTO convertirAResultadosDTO(List<FilaVector> vectoresEstado) {
        if (vectoresEstado == null || vectoresEstado.isEmpty()) {
            return new ResultadosSimulacionDTO();
        }

        // Calcular máximo de barcos en sistema
        int maxBarcosEnSistema = calcularMaxBarcosEnSistema(vectoresEstado);

        // Convertir filas con lógica de slots
        List<FilaVectorDTO> filasDTO = convertirFilasConSlots(vectoresEstado, maxBarcosEnSistema);

        // Crear resultado
        ResultadosSimulacionDTO resultado = new ResultadosSimulacionDTO();
        resultado.setFilasTabla(filasDTO);
        resultado.setMaxBarcosEnSistema(maxBarcosEnSistema);

        // Calcular estadísticas finales
        if (!filasDTO.isEmpty()) {
            FilaVectorDTO ultimaFila = filasDTO.get(filasDTO.size() - 1);
            resultado.setTiempoPermaneciaMedio(ultimaFila.getMediaTiempoPermanencia());
            resultado.setTiempoPermanciaMinimo(ultimaFila.getMinTiempoPermanencia());
            resultado.setTiempoPermanciaMaximo(ultimaFila.getMaxTiempoPermanencia());
            
            // Estadísticas de utilizaciones (usar valores de la última fila)
            resultado.setUtilizacionMuelles(new double[]{
                ultimaFila.getMuelle1Utilizacion(),
                ultimaFila.getMuelle2Utilizacion()
            });
            resultado.setUtilizacionGruas(new double[]{
                ultimaFila.getGrua1Utilizacion(),
                ultimaFila.getGrua2Utilizacion()
            });
            
            // Otras estadísticas
            resultado.setTotalBarcosAtendidos(ultimaFila.getContadorBarcosAtendidos());
            resultado.setTiempoTotalSimulacion(ultimaFila.getTiempo());
        }

        return resultado;
    }

    /**
     * Calcula el máximo número de barcos en sistema durante toda la simulación
     */
    private static int calcularMaxBarcosEnSistema(List<FilaVector> vectoresEstado) {
        int max = 0;
        for (FilaVector fila : vectoresEstado) {
            int cantBarcos = fila.getCantBarcosEnSistema();
            if (cantBarcos > max) {
                max = cantBarcos;
            }
        }
        return Math.max(max, 2); // Mínimo 2 slots como en tu diseño original
    }

    /**
     * Convierte las filas aplicando la lógica de slots persistentes
     */
    private static List<FilaVectorDTO> convertirFilasConSlots(List<FilaVector> vectoresEstado, int maxSlots) {
        List<FilaVectorDTO> filasDTO = new ArrayList<>();
        
        // Estado de slots persistente a través de toda la simulación
        Map<Integer, Integer> barcoASlot = new HashMap<>(); // barcoId -> slotNumber
        boolean[] slotsOcupados = new boolean[maxSlots + 1]; // 1-indexed

        for (FilaVector fila : vectoresEstado) {
            // Convertir fila base
            FilaVectorDTO filaDTO = convertirFilaBase(fila);

            // Actualizar slots basado en barcos actuales en sistema
            actualizarSlots(fila.getBarcosEnSistema(), barcoASlot, slotsOcupados, maxSlots);

            // Generar DTOs de barcos con slots asignados
            filaDTO.setBarcosEnSistema(generarBarcosSlotDTO(fila.getBarcosEnSistema(), barcoASlot));

            filasDTO.add(filaDTO);
        }

        return filasDTO;
    }

    /**
     * Convierte una FilaVector a FilaVectorDTO (campos base)
     */
    private static FilaVectorDTO convertirFilaBase(FilaVector fila) {
        FilaVectorDTO dto = new FilaVectorDTO();

        // Copiar todos los campos base con manejo seguro de nulls
        dto.setNumeroFila(fila.getNumeroFila());
        dto.setEvento(fila.getEvento());
        dto.setTiempo(fila.getTiempo());
        dto.setRndLlegada(fila.getRndLlegada());
        dto.setProximaLlegada(fila.getProximaLlegada());
        dto.setRndDescargaMuelle1(fila.getRndDescargaMuelle1());
        dto.setTiempoRestanteMuelle1(fila.getTiempoRestanteMuelle1()); // NUEVO
        dto.setFinDescarga1(fila.getFinDescarga1());
        dto.setRndDescargaMuelle2(fila.getRndDescargaMuelle2());
        dto.setTiempoRestanteMuelle2(fila.getTiempoRestanteMuelle2()); // NUEVO
        dto.setFinDescarga2(fila.getFinDescarga2());
        dto.setCantidadBarcosBahia(fila.getCantidadBarcosBahia());
        dto.setMuelle1Estado(safeString(fila.getMuelle1Estado()));
        dto.setMuelle1InicioOcupado(fila.getMuelle1InicioOcupado());
        dto.setMuelle2Estado(safeString(fila.getMuelle2Estado()));
        dto.setMuelle2InicioOcupado(fila.getMuelle2InicioOcupado());
        dto.setGrua1Estado(safeString(fila.getGrua1Estado()));
        dto.setGrua1InicioOcupado(fila.getGrua1InicioOcupado());
        dto.setGrua2Estado(safeString(fila.getGrua2Estado()));
        dto.setGrua2InicioOcupado(fila.getGrua2InicioOcupado());
        dto.setMaxTiempoPermanencia(fila.getMaxTiempoPermanencia());
        dto.setMinTiempoPermanencia(fila.getMinTiempoPermanencia());
        dto.setAcumuladorTiempoEsperaBahia(fila.getAcumuladorTiempoEsperaBahia());
        dto.setContadorBarcosAtendidos(fila.getContadorBarcosAtendidos());
        dto.setMediaTiempoPermanencia(fila.getMediaTiempoPermanencia());
        dto.setMuelle1AcTiempoOcupado(fila.getMuelle1AcTiempoOcupado());
        dto.setMuelle1Utilizacion(fila.getMuelle1Utilizacion());
        dto.setMuelle2AcTiempoOcupado(fila.getMuelle2AcTiempoOcupado());
        dto.setMuelle2Utilizacion(fila.getMuelle2Utilizacion());
        dto.setGrua1AcTiempoOcupado(fila.getGrua1AcTiempoOcupado());
        dto.setGrua1Utilizacion(fila.getGrua1Utilizacion());
        dto.setGrua2AcTiempoOcupado(fila.getGrua2AcTiempoOcupado());
        dto.setGrua2Utilizacion(fila.getGrua2Utilizacion());
        dto.setCantBarcosEnSistema(fila.getCantBarcosEnSistema());

        return dto;
    }

    /**
     * Actualiza los slots persistentes basado en los barcos actuales en sistema
     */
    private static void actualizarSlots(List<Barco> barcosActuales, Map<Integer, Integer> barcoASlot, 
                                       boolean[] slotsOcupados, int maxSlots) {
        // Crear set de IDs de barcos actuales para detectar cuáles salieron del sistema
        Set<Integer> idsActuales = new HashSet<>();
        if (barcosActuales != null) {
            for (Barco barco : barcosActuales) {
                idsActuales.add(barco.getId());
            }
        }

        // Liberar slots de barcos que ya no están en el sistema
        Set<Integer> barcosAEliminar = new HashSet<>();
        for (Map.Entry<Integer, Integer> entry : barcoASlot.entrySet()) {
            int barcoId = entry.getKey();
            int slot = entry.getValue();
            if (!idsActuales.contains(barcoId)) {
                slotsOcupados[slot] = false;
                barcosAEliminar.add(barcoId);
            }
        }
        // Eliminar del mapa
        for (int barcoId : barcosAEliminar) {
            barcoASlot.remove(barcoId);
        }

        // Asignar slots a barcos nuevos
        if (barcosActuales != null) {
            for (Barco barco : barcosActuales) {
                if (!barcoASlot.containsKey(barco.getId())) {
                    // Buscar primer slot libre
                    for (int i = 1; i <= maxSlots; i++) {
                        if (!slotsOcupados[i]) {
                            barcoASlot.put(barco.getId(), i);
                            slotsOcupados[i] = true;
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Genera la lista de BarcoSlotDTO con slots asignados
     */
    private static List<BarcoSlotDTO> generarBarcosSlotDTO(List<Barco> barcosActuales, Map<Integer, Integer> barcoASlot) {
        List<BarcoSlotDTO> barcosSlot = new ArrayList<>();
        
        if (barcosActuales != null) {
            for (Barco barco : barcosActuales) {
                Integer slot = barcoASlot.get(barco.getId());
                if (slot != null) {
                    BarcoSlotDTO barcoSlot = new BarcoSlotDTO(
                        barco.getId(),
                        BarcoSlotDTO.mapearEstado(barco.getEstado()),
                        barco.getHoraLlegadaSistema(),
                        slot
                    );
                    barcosSlot.add(barcoSlot);
                }
            }
        }
        
        return barcosSlot;
    }

    // Métodos auxiliares para manejo seguro de nulls
    private static int safe(Integer value) {
        return value != null ? value : 0;
    }

    private static String safeString(Object obj) {
        return obj != null ? obj.toString() : "";
    }
}
