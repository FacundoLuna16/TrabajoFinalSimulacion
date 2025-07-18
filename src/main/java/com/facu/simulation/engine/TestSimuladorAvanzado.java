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
        // Definir el ancho total de la tabla para permitir scroll horizontal
        final int ANCHO_TOTAL = 250;
        
        // Imprimir separador y título
        System.out.println("=".repeat(ANCHO_TOTAL));
        System.out.println("TABLA DE SIMULACIÓN - EVENTOS DEL PUERTO");
        System.out.println("=".repeat(ANCHO_TOTAL));
        System.out.println("(Use scroll horizontal si es necesario para ver todas las columnas)");
        System.out.println();
        
        // Encabezado completo en una sola línea con formato exacto que coincide con los datos
        System.out.printf(
                "%-4s|%-15s|%-6s|%-8s|%-8s|%-8s|%-10s|%-8s|%-10s|%-5s|" +
                "%-8s|%-8s|%-8s|%-8s|%-8s|%-8s|%-8s|%-8s|" +
                "%-8s|%-8s|%-8s|%-6s|%-8s|" +
                "%-8s|%-6s|%-8s|%-6s|%-8s|%-6s|%-8s|%-6s|" +
                "%-6s|%-6s|%-12s|%-8s|%-6s|%-12s|%-8s%n",
                "Fila", "Evento", "Reloj", "RNDLleg", "ProxLleg", "RNDM1", "FinDescM1", "RNDM2", "FinDescM2", "Bahía",
                "M1Est", "M1Inic", "M2Est", "M2Inic", "G1Est", "G1Inic", "G2Est", "G2Inic",
                "MaxTPer", "MinTPer", "AcTPer", "CantB", "MedTPer",
                "M1AcTOc", "M1Ut%", "M2AcTOc", "M2Ut%", "G1AcTOc", "G1Ut%", "G2AcTOc", "G2Ut%",
                "BSist", "B1_ID", "B1_Estado", "B1_Ingr", "B2_ID", "B2_Estado", "B2_Ingr"
        );
        
        // Línea separadora que coincide con el ancho de la tabla
        System.out.println("-".repeat(ANCHO_TOTAL));


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

            // Imprimir datos con formato exactamente alineado al encabezado
            System.out.printf(
                    "%-4d|%-15s|%-6.2f|%-8s|%-8.2f|%-8s|%-10s|%-8s|%-10s|%-5d|" +
                    "%-8s|%-8.2f|%-8s|%-8.2f|%-8s|%-8.2f|%-8s|%-8.2f|" +
                    "%-8.3f|%-8.3f|%-8.2f|%-6d|%-8.3f|" +
                    "%-8.2f|%-6.1f|%-8.2f|%-6.1f|%-8.2f|%-6.1f|%-8.2f|%-6.1f|" +
                    "%-6d|%-6s|%-12s|%-8s|%-6s|%-12s|%-8s%n",
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
                    fila.getContadorBarcosQueEsperanEnBahia(),
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
                    (b1_id > 0) ? String.valueOf(b1_id) : "",
                    b1_estado,
                    b1_t_ingreso,
                    (b2_id > 0) ? String.valueOf(b2_id) : "",
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
