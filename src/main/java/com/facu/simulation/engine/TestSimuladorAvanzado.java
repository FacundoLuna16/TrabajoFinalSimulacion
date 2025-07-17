package com.facu.simulation.engine;

import com.facu.simulation.dto.ResultadosSimulacionDTO;
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

        // 2. Imprimir cada fila (esta parte ya estaba bien)
        for (FilaVector fila : vectores) {
            String rndLlegadaStr = (fila.getRndLlegada() == -1.0) ? "" : String.format("%.4f", fila.getRndLlegada());
            String rndDescargaM1Str = (fila.getRndDescargaMuelle1() == -1.0) ? "" : String.format("%.4f", fila.getRndDescargaMuelle1());
            String rndDescargaM2Str = (fila.getRndDescargaMuelle2() == -1.0) ? "" : String.format("%.4f", fila.getRndDescargaMuelle2());
            String finDescarga1Str = (fila.getFinDescarga1() == -1.0) ? "" : String.format("%.2f", fila.getFinDescarga1());
            String finDescarga2Str = (fila.getFinDescarga2() == -1.0) ? "" : String.format("%.2f", fila.getFinDescarga2());

            // TU PRINTF EXACTO CON VALORES SEGUROS
            System.out.printf("%-5d | %-25s | %-10.2f | %-12s | %-12.2f | %-15s | %-15s | %-15s | %-15s | %-10d | " +
                            "%-25s | %-20.2f | %-25s | %-20.2f | %-25s | %-20.2f | %-25s | %-20.2f | %-25.2f | %-25.2f | " +
                            "%-30.2f | %-15d | %-25.2f | %-25.2f | %-25.2f | %-25.2f | %-25.2f | %-25.2f | %-25.2f | %-25.2f | " +
                            "%-25d | %-20d | %-15s | %-25s | %-25s | %-15d | %-25s | %-25s%n",
                    fila.getNumeroFila(),
                    fila.getEvento(),
                    fila.getTiempo(),
                    rndLlegadaStr,
                    fila.getProximaLlegada(),
                    rndDescargaM1Str,
                    finDescarga1Str,
                    rndDescargaM2Str,
                    finDescarga2Str,
                    safe(fila.getCantidadBarcosBahia()),
                    safe(fila.getMuelle1Estado()),
                    safe(fila.getMuelle1InicioOcupado()),
                    safe(fila.getMuelle2Estado()),
                    safe(fila.getMuelle2InicioOcupado()),
                    safe(fila.getGrua1Estado()),
                    safe(fila.getGrua1InicioOcupado()),
                    safe(fila.getGrua2Estado()),
                    safe(fila.getGrua2InicioOcupado()),
                    safe(fila.getMaxTiempoPermanencia()),
                    safe(fila.getMinTiempoPermanencia()),
                    safe(fila.getAcumuladorTiempoEsperaBahia()),
                    safe(fila.getContadorBarcosAtendidos()),
                    safe(fila.getMediaTiempoPermanencia()),
                    safe(fila.getMuelle1AcTiempoOcupado()),
                    safe(fila.getMuelle1Utilizacion()),
                    safe(fila.getMuelle2AcTiempoOcupado()),
                    safe(fila.getMuelle2Utilizacion()),
                    safe(fila.getGrua1AcTiempoOcupado()),
                    safe(fila.getGrua1Utilizacion()),
                    safe(fila.getGrua2AcTiempoOcupado()),
                    safe(fila.getGrua2Utilizacion()),
                    safe(fila.getCantBarcosEnSistema()),
                    // Placeholder slots
                    0, "", "", // B_Slot1: ID, Estado, T_Ingreso
                    0, "", ""   // B_Slot2: ID, Estado, T_Ingreso
            );
        }
    }

    // Método auxiliar para manejar nulls de forma segura
    private static Object safe(Object obj) {
        if (obj == null) {
            return 0.0; // Para doubles
        }
        return obj;
    }

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
