package com.facu.simulation.model;

import lombok.Data;

@Data
public class Grua {
    private int id;
    private EstadoGrua estado;
    private Muelle muelleAsignado;
    private Barco barcoAsignado; // Agregamos este campo para el simulador
    private double tiempoInicioOcupado;
    private double acumuladorTiempoOcupado;
    
    public Grua(int id) {
        this.id = id;
        this.estado = EstadoGrua.LIBRE;
        this.muelleAsignado = null;
        this.tiempoInicioOcupado = 0;
        this.acumuladorTiempoOcupado = 0;
    }
    
    public double getInicioOcupacion() {
        return tiempoInicioOcupado;
    }

    public void acumularTiempoOcupado(double tiempo) {
        this.acumuladorTiempoOcupado += tiempo;
    }
    
    public void setTiempoInicioOcupado(double tiempo) {
        this.tiempoInicioOcupado = tiempo;
    }
    
    public void asignarAMuelle(Muelle muelle, double tiempoActual) {
        this.estado = EstadoGrua.OCUPADA;
        this.muelleAsignado = muelle;
        this.tiempoInicioOcupado = tiempoActual;
        muelle.asignarGrua();
    }
    
    public void liberar(double tiempoActual) {
        if (estado == EstadoGrua.OCUPADA) {
            acumularTiempoOcupado(tiempoActual - tiempoInicioOcupado);
            if (muelleAsignado != null) {
                muelleAsignado.liberarGrua();
            }
        }
        this.estado = EstadoGrua.LIBRE;
        this.muelleAsignado = null;
        this.barcoAsignado = null; // ¡ESTO TAMBIÉN FALTABA!
    }
    
    public boolean estaLibre() {
        return estado == EstadoGrua.LIBRE;
    }
    
    public boolean estaOcupada() {
        return estado == EstadoGrua.OCUPADA;
    }
    
    public double getUtilizacion(double tiempoTotal) {
        if (tiempoTotal <= 0) {
            return 0;
        }
        return acumuladorTiempoOcupado / tiempoTotal;
    }
}