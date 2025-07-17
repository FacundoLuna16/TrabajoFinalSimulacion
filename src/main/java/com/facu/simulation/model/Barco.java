package com.facu.simulation.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Barco {
    private int id;
    private EstadoBarco estado;
    private double tiempoLlegadaSistema;
    private double tiempoDescargaRestante;
    
    public Barco(int id, double horaLlegada, double tiempoDescarga) {
        this.id = id;
        this.tiempoLlegadaSistema = horaLlegada;
        this.tiempoDescargaRestante = tiempoDescarga;
        this.estado = EstadoBarco.EN_BAHIA;
    }

    public Barco (int id, double horaLlegada) {
        this.id = id;
        this.tiempoLlegadaSistema = horaLlegada;
        this.tiempoDescargaRestante = 0; // Por defecto, sin tiempo de descarga
        this.estado = null; // Estado no definido

    }

    public double getHoraLlegadaSistema() {
        return tiempoLlegadaSistema;
    }

    public void reducirTiempoDescarga(double tiempoTrabajado) {
        this.tiempoDescargaRestante = Math.max(0, this.tiempoDescargaRestante - tiempoTrabajado);
    }
    
    public boolean descargaCompleta() {
        return tiempoDescargaRestante <= 0;
    }
}