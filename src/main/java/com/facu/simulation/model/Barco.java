package com.facu.simulation.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Barco {
    private int id;
    private EstadoBarco estado;
    private double horaLlegadaBahia;
    private double tiempoDescargaRestante;
    private double tiempoLlegadaSistema;
    
    public Barco(int id, double horaLlegadaBahia, double tiempoDescarga) {
        this.id = id;
        this.horaLlegadaBahia = horaLlegadaBahia;
        this.tiempoDescargaRestante = tiempoDescarga;
        this.estado = EstadoBarco.EN_BAHIA;
    }

    public Barco (int id, double horaLlegadaBahia) {
        this.id = id;
        this.horaLlegadaBahia = horaLlegadaBahia;
        this.tiempoDescargaRestante = 0; // Por defecto, sin tiempo de descarga
        this.estado = null; // Estado no definido

    }

    public double getHoraLlegadaSistema() {
        return horaLlegadaBahia;
    }

    public void reducirTiempoDescarga(double tiempoTrabajado) {
        this.tiempoDescargaRestante = Math.max(0, this.tiempoDescargaRestante - tiempoTrabajado);
    }
    
    public boolean descargaCompleta() {
        return tiempoDescargaRestante <= 0;
    }
}