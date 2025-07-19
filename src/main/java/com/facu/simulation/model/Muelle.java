package com.facu.simulation.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Muelle {
    private int id;
    private EstadoMuelle estado;
    private Barco barcoAtendido;
    private int gruasAsignadas;
    private List<Grua> gruas; // Lista de grúas asignadas al muelle
    private double tiempoInicioOcupado;
    private double acumuladorTiempoOcupado;
    
    public Muelle(int id) {
        this.id = id;
        this.estado = EstadoMuelle.LIBRE;
        this.barcoAtendido = null;
        this.gruasAsignadas = 0;
        this.tiempoInicioOcupado = 0;
        this.acumuladorTiempoOcupado = 0;
        this.gruas = new ArrayList<>();
    }

    public void asignarGrua() {
        if (gruasAsignadas < 2) {
            gruasAsignadas++;
        }
    }
    
    public void liberarGrua() {
        if (gruasAsignadas > 0) {
            gruasAsignadas--;
        }
    }
    

    public double getInicioOcupacion() {
        return tiempoInicioOcupado;
    }

    public void acumularTiempoOcupado(double tiempo) {
        this.acumuladorTiempoOcupado += tiempo;
    }
    
    public void ocuparMuelle(Barco barco, double tiempoActual) {
        this.estado = EstadoMuelle.OCUPADO;
        this.barcoAtendido = barco;
        this.tiempoInicioOcupado = tiempoActual;
        barco.setEstado(EstadoBarco.SIENDO_DESCARGADO);
    }
    
    public void liberarMuelle(double tiempoActual) {
        if (estado == EstadoMuelle.OCUPADO) {
            acumularTiempoOcupado(tiempoActual - tiempoInicioOcupado);
        }
        this.estado = EstadoMuelle.LIBRE;
        this.gruas = new ArrayList<>(); // Limpiamos la lista de grúas
        this.barcoAtendido = null;
        this.gruasAsignadas = 0;
    }
    
    public boolean estaLibre() {
        return estado == EstadoMuelle.LIBRE;
    }
    
    public boolean estaOcupado() {
        return estado == EstadoMuelle.OCUPADO;
    }
}