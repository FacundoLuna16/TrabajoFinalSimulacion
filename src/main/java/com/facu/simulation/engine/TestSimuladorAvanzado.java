package com.facu.simulation.engine;

import com.facu.simulation.dto.ResultadosSimulacionDTO;
import com.facu.simulation.model.Barco;
import com.facu.simulation.model.EstadoBarco;
import com.facu.simulation.model.EstadoMuelle;

import java.util.ArrayList;
import java.util.List;

public class TestSimuladorAvanzado {

    public static void main(String[] args) {
        // --- CONFIGURACIÓN PARA PROBAR RND SEPARADOS: Más llegadas para ver ambos muelles ---
        ConfiguracionSimulacion config = new ConfiguracionSimulacion(
            0.8,   // Media entre llegadas: 0.8 días (MÁS BARCOS para forzar uso de ambos muelles)
            0.5,   // Tiempo mínimo de descarga: 0.5 días
            1.5,   // Tiempo máximo de descarga: 1.5 días
            2,     // Cantidad de muelles: 2
            2,     // Cantidad de grúas: 2
            10,    // Días de simulación: 10 días
            0,    // Mostrar desde fila 25 (cuando ya hay más actividad)
            10,    // Hasta fila 35
            true   // Filtro por fila
        );

        // Crear y ejecutar simulador
        Simulador simulador = new Simulador(config);
        simulador.run(); // Ejecuta la simulación y llena los vectores de estado

        // Solo imprimir estadísticas finales para verificar funcionamiento
        System.out.println("Simulación completada exitosamente!");
        System.out.println("Mostrando tabla de eventos de los últimos días...\n");

        // Imprimir la tabla de resultados directamente desde los vectores de estado
        imprimirTablaSimulacion(simulador.getVectoresEstado());
    }

    private static void imprimirTablaSimulacion(List<FilaVector> vectores) {
        // 1. Encabezado de la tabla - AHORA 100% CORRECTO
        System.out.printf("%-5s | %-25s | %-10s | %-12s | %-12s | %-15s | %-15s | %-15s | %-15s | %-10s | " +
                        "%-25s | %-20s | %-25s | %-20s | %-25s | %-20s | %-25s | %-20s | %-25s | %-25s | " +
                        "%-30s | %-15s | %-25s | %-25s | %-25s | %-25s | %-25s | %-25s | %-25s | %-25s | " +
                        "%-25s | %-20s | %-15s | %-25s | %-25s | %-15s | %-25s | %-25s%n",
                "Fila", "Evento", "Reloj", "RND Llegada", "Prox Llegada",
                "RND Descarga 1", "Fin Descarga 1", "RND Descarga 2", "Fin Descarga 2", "Bahia Cola",
                "Muelle 1 Estado", "M1 Inicio Ocup.", "Muelle 2 Estado", "M2 Inicio Ocup.",
                "Grua 1 Estado", "G1 Inicio Ocup.", "Grua 2 Estado", "G2 Inicio Ocup.",
                "MAX T Perm.", "MIN T Perm.", "AC T Perm.", "AC Cant Barcos", "Media T Perm.",
                "M1 AC T Ocupado", "M1 Util (%)", "M2 AC T Ocupado", "M2 Util (%)",
                "G1 AC T Ocupado", "G1 Util (%)", "G2 AC T Ocupado", "G2 Util (%)",
                "Barcos en Sistema", "B_Slot1_ID", "B_Slot1_Estado", "B_Slot1_T_Ingreso",
                "B_Slot2_ID", "B_Slot2_Estado", "B_Slot2_T_Ingreso");

        // 2. Imprimir cada fila con manejo de datos seguro
        for (FilaVector fila : vectores) {
            // --- Preparar strings para valores que pueden estar vacíos ---
            String rndLlegadaStr = (fila.getRndLlegada() > 0) ? String.format("%.4f", fila.getRndLlegada()) : "";
            String finDescarga1Str = (fila.getFinDescarga1() > 0) ? String.format("%.2f", fila.getFinDescarga1()) : "";
            String rndDescargaM1Str = (fila.getRndDescargaMuelle1() > 0) ? String.format("%.4f", fila.getRndDescargaMuelle1()) : "";
            String finDescarga2Str = (fila.getFinDescarga2() > 0) ? String.format("%.2f", fila.getFinDescarga2()) : "";
            String rndDescargaM2Str = (fila.getRndDescargaMuelle2() > 0) ? String.format("%.4f", fila.getRndDescargaMuelle2()) : "";

            // --- Lógica para encontrar los barcos en los muelles ---
            Barco barcoEnMuelle1 = null;
            Barco barcoEnMuelle2 = null;
            if (fila.getBarcosEnSistema() != null) {
                // Hacemos una copia para no modificar la lista original
                List<Barco> barcosDescargando = new ArrayList<>();
                for(Barco b : fila.getBarcosEnSistema()) {
                    if (b.getEstado() == EstadoBarco.SIENDO_DESCARGADO) {
                        barcosDescargando.add(b);
                    }
                }
                // Asignamos los barcos a los muelles ocupados
                if (fila.getMuelle1Estado() == EstadoMuelle.OCUPADO && !barcosDescargando.isEmpty()) {
                    barcoEnMuelle1 = barcosDescargando.remove(0);
                }
                if (fila.getMuelle2Estado() == EstadoMuelle.OCUPADO && !barcosDescargando.isEmpty()) {
                    barcoEnMuelle2 = barcosDescargando.remove(0);
                }
            }

            // --- Preparar variables para los slots de barcos, asegurando que no haya nulls ---
            int b1_id = (barcoEnMuelle1 != null) ? barcoEnMuelle1.getId() : 0;
            String b1_estado = (barcoEnMuelle1 != null && barcoEnMuelle1.getEstado() != null) ? barcoEnMuelle1.getEstado().toString() : "";
            String b1_t_ingreso = (barcoEnMuelle1 != null) ? String.format("%.2f", barcoEnMuelle1.getTiempoLlegadaSistema()) : "";

            int b2_id = (barcoEnMuelle2 != null) ? barcoEnMuelle2.getId() : 0;
            String b2_estado = (barcoEnMuelle2 != null && barcoEnMuelle2.getEstado() != null) ? barcoEnMuelle2.getEstado().toString() : "";
            String b2_t_ingreso = (barcoEnMuelle2 != null) ? String.format("%.2f", barcoEnMuelle2.getTiempoLlegadaSistema()) : "";

            // --- Llamada final a printf con todos los datos preparados y seguros ---
            System.out.printf("%-5d | %-25s | %-10.2f | %-12s | %-12.2f | %-15s | %-15s | %-15s | %-15s | %-10d | " +
                            "%-25s | %-20.2f | %-25s | %-20.2f | %-25s | %-20.2f | %-25s | %-20.2f | %-25.4f | %-25.4f | " +
                            "%-30.2f | %-15d | %-25.4f | %-25.2f | %-25.2f | %-25.2f | %-25.2f | %-25.2f | %-25.2f | %-25.2f | " +
                            "%-25.2f | %-20s | %-15d | %-25s | %-25s | %-15d | %-25s | %-25s%n",
                    fila.getNumeroFila(),
                    fila.getEvento(),
                    fila.getTiempo(),
                    rndLlegadaStr,
                    fila.getProximaLlegada(),
                    rndDescargaM1Str,
                    finDescarga1Str,
                    rndDescargaM2Str,
                    finDescarga2Str,
                    fila.getCantidadBarcosBahia(),
                    safe(fila.getMuelle1Estado()),
                    fila.getMuelle1InicioOcupado(),
                    safe(fila.getMuelle2Estado()),
                    fila.getMuelle2InicioOcupado(),
                    safe(fila.getGrua1Estado()),
                    fila.getGrua1InicioOcupado(),
                    safe(fila.getGrua2Estado()),
                    fila.getGrua2InicioOcupado(),
                    fila.getMaxTiempoPermanencia(),
                    fila.getMinTiempoPermanencia(),
                    fila.getAcumuladorTiempoEsperaBahia(),
                    fila.getContadorBarcosQueEsperanEnBahia(), // Usar el contador correcto para la media
                    fila.getMediaTiempoPermanencia(),
                    fila.getMuelle1AcTiempoOcupado(),
                    fila.getMuelle1Utilizacion(),
                    fila.getMuelle2AcTiempoOcupado(),
                    fila.getMuelle2Utilizacion(),
                    fila.getGrua1AcTiempoOcupado(),
                    fila.getGrua1Utilizacion(),
                    fila.getGrua2AcTiempoOcupado(),
                    fila.getGrua2Utilizacion(),
                    fila.getCantBarcosEnSistema(),
                    // Datos de los barcos, ahora seguros
                    b1_id,
                    b1_estado,
                    b1_t_ingreso,
                    b2_id,
                    b2_estado,
                    b2_t_ingreso
            );
        }
    }

    // Método auxiliar para manejar nulls de forma segura
    private static int safe(Integer i) {
        return i != null ? i : 0;
    }

    private static double safe(Double d) {
        return d != null ? d : 0.0;
    }

    private static String safe(Enum<?> e) {
        return e != null ? e.toString() : "";
    }
}
