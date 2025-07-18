package com.facu.simulation.dto;

import lombok.Data;
import java.util.List;

/**
 * Data Transfer Object para transferir datos de una fila del vector de estado
 * desde el motor de simulación a la interfaz gráfica.
 * 
 * Esta clase incluye TODAS las columnas mostradas según la especificación de integración.
 */
@Data
public class FilaVectorDTO {
    
    // =============== CAMPOS BASE REQUERIDOS ===============
    
    // Información básica de la fila
    private int numeroFila;
    private String evento;
    private double tiempo;
    
    // Campos de llegada
    private double rndLlegada;
    private double proximaLlegada;
    
    // Campos de descarga (muelle específicos)
    private double rndDescargaMuelle1;
    private double tiempoRestanteMuelle1; // NUEVO: tiempo restante para descarga en muelle 1
    private double finDescarga1;
    private double rndDescargaMuelle2;
    private double tiempoRestanteMuelle2; // NUEVO: tiempo restante para descarga en muelle 2
    private double finDescarga2;
    
    // Estados de bahía
    private int cantidadBarcosBahia;
    
    // Estados de muelles
    private String muelle1Estado;
    private double muelle1InicioOcupado;
    private String muelle2Estado;
    private double muelle2InicioOcupado;
    
    // Estados de grúas
    private String grua1Estado;
    private double grua1InicioOcupado;
    private String grua2Estado;
    private double grua2InicioOcupado;
    
    // Estadísticas de tiempo de permanencia
    private double maxTiempoPermanencia;
    private double minTiempoPermanencia;
    private double acumuladorTiempoEsperaBahia;
    private int contadorBarcosAtendidos;
    private double mediaTiempoPermanencia;
    
    // Utilizaciones acumuladas
    private double muelle1AcTiempoOcupado;
    private double muelle1Utilizacion;
    private double muelle2AcTiempoOcupado;
    private double muelle2Utilizacion;
    private double grua1AcTiempoOcupado;
    private double grua1Utilizacion;
    private double grua2AcTiempoOcupado;
    private double grua2Utilizacion;
    
    // Cantidad total de barcos en sistema
    private int cantBarcosEnSistema;
    
    // =============== NUEVOS CAMPOS DINÁMICOS ===============
    
    // Lista de barcos en sistema para slots dinámicos
    private List<BarcoSlotDTO> barcosEnSistema;
    
    // Constructor por defecto
    public FilaVectorDTO() {}
    
    /**
     * Constructor completo
     */
    public FilaVectorDTO(int numeroFila, String evento, double tiempo,
                        double rndLlegada, double proximaLlegada,
                        double rndDescargaMuelle1, double tiempoRestanteMuelle1, double finDescarga1,
                        double rndDescargaMuelle2, double tiempoRestanteMuelle2, double finDescarga2,
                        int cantidadBarcosBahia,
                        String muelle1Estado, double muelle1InicioOcupado,
                        String muelle2Estado, double muelle2InicioOcupado,
                        String grua1Estado, double grua1InicioOcupado,
                        String grua2Estado, double grua2InicioOcupado,
                        double maxTiempoPermanencia, double minTiempoPermanencia,
                        double acumuladorTiempoEsperaBahia, int contadorBarcosAtendidos,
                        double mediaTiempoPermanencia,
                        double muelle1AcTiempoOcupado, double muelle1Utilizacion,
                        double muelle2AcTiempoOcupado, double muelle2Utilizacion,
                        double grua1AcTiempoOcupado, double grua1Utilizacion,
                        double grua2AcTiempoOcupado, double grua2Utilizacion,
                        int cantBarcosEnSistema,
                        List<BarcoSlotDTO> barcosEnSistema) {
        
        this.numeroFila = numeroFila;
        this.evento = evento;
        this.tiempo = tiempo;
        this.rndLlegada = rndLlegada;
        this.proximaLlegada = proximaLlegada;
        this.rndDescargaMuelle1 = rndDescargaMuelle1;
        this.tiempoRestanteMuelle1 = tiempoRestanteMuelle1;
        this.finDescarga1 = finDescarga1;
        this.rndDescargaMuelle2 = rndDescargaMuelle2;
        this.tiempoRestanteMuelle2 = tiempoRestanteMuelle2;
        this.finDescarga2 = finDescarga2;
        this.cantidadBarcosBahia = cantidadBarcosBahia;
        this.muelle1Estado = muelle1Estado;
        this.muelle1InicioOcupado = muelle1InicioOcupado;
        this.muelle2Estado = muelle2Estado;
        this.muelle2InicioOcupado = muelle2InicioOcupado;
        this.grua1Estado = grua1Estado;
        this.grua1InicioOcupado = grua1InicioOcupado;
        this.grua2Estado = grua2Estado;
        this.grua2InicioOcupado = grua2InicioOcupado;
        this.maxTiempoPermanencia = maxTiempoPermanencia;
        this.minTiempoPermanencia = minTiempoPermanencia;
        this.acumuladorTiempoEsperaBahia = acumuladorTiempoEsperaBahia;
        this.contadorBarcosAtendidos = contadorBarcosAtendidos;
        this.mediaTiempoPermanencia = mediaTiempoPermanencia;
        this.muelle1AcTiempoOcupado = muelle1AcTiempoOcupado;
        this.muelle1Utilizacion = muelle1Utilizacion;
        this.muelle2AcTiempoOcupado = muelle2AcTiempoOcupado;
        this.muelle2Utilizacion = muelle2Utilizacion;
        this.grua1AcTiempoOcupado = grua1AcTiempoOcupado;
        this.grua1Utilizacion = grua1Utilizacion;
        this.grua2AcTiempoOcupado = grua2AcTiempoOcupado;
        this.grua2Utilizacion = grua2Utilizacion;
        this.cantBarcosEnSistema = cantBarcosEnSistema;
        this.barcosEnSistema = barcosEnSistema;
    }
}
