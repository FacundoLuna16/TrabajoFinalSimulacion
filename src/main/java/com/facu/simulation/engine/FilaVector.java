package com.facu.simulation.engine;

import com.facu.simulation.model.Barco;
import com.facu.simulation.model.EstadoGrua;
import com.facu.simulation.model.EstadoMuelle;
import lombok.Data;

import java.util.List;

/**
 * Representa una fila en la tabla de simulación, capturando el estado
 * del sistema en un momento específico del tiempo.
 */
@Data
public class FilaVector {
    private int numeroFila;
    private double tiempo;
    private String evento;
    // Llegada_barco
    private double rndLlegada;
    private double proximaLlegada;
    // Descarga_barco
    private double rndDescargaMuelle1;
    private double finDescarga1;
    private double tiempoRestanteMuelle1; // NUEVO: tiempo restante para terminar descarga en muelle 1
    private double rndDescargaMuelle2;
    private double finDescarga2;
    private double tiempoRestanteMuelle2; // NUEVO: tiempo restante para terminar descarga en muelle 2
    // Bahía COLA
    private int cantidadBarcosBahia;
    // Muelle
    private EstadoMuelle muelle1Estado;
    private int muelle1GruasAsignadas;
    private double muelle1InicioOcupado;
    private EstadoMuelle muelle2Estado;
    private int muelle2GruasAsignadas;
    private double muelle2InicioOcupado;
    // Grúas
    private EstadoGrua grua1Estado;
    private double grua1InicioOcupado;
    private EstadoGrua grua2Estado;
    private double grua2InicioOcupado;
    // Estadísticas
    private double maxTiempoPermanencia;
    private double minTiempoPermanencia;
    private double acumuladorTiempoEsperaBahia;
    private int contadorBarcosAtendidos;
    private double mediaTiempoPermanencia;
    // Recursos del puerto
    private double muelle1AcTiempoOcupado;
    private double muelle1Utilizacion;
    private double muelle2AcTiempoOcupado;
    private double muelle2Utilizacion;
    // Grúas
    private double grua1AcTiempoOcupado;
    private double grua1Utilizacion;
    private double grua2AcTiempoOcupado;
    private double grua2Utilizacion;
    // Variables de control
    private int cantBarcosEnSistema;
    private int cantMaxBarcosEnSistema;
    private List<Barco> barcosEnSistema; // Lista de barcos en el sistema

    //otros
    private int contadorBarcosQueEsperonEnBahia;


    public FilaVector(int numeroFila, double tiempo, String evento) {
        this.numeroFila = numeroFila;
        this.tiempo = tiempo;
        this.evento = evento;
        
        // Inicializar campos que podrían no tener valores válidos
        this.rndLlegada = -1.0;
        this.rndDescargaMuelle1 = -1.0;
        this.rndDescargaMuelle2 = -1.0;
        this.finDescarga1 = -1.0;
        this.finDescarga2 = -1.0;
        this.tiempoRestanteMuelle1 = -1.0;
        this.tiempoRestanteMuelle2 = -1.0;
        this.muelle1InicioOcupado = 0.0;
        this.muelle2InicioOcupado = 0.0;
        this.grua1InicioOcupado = 0.0;
        this.grua2InicioOcupado = 0.0;
        
        // Los demás campos se irán llenando según el estado del simulador
    }
}
