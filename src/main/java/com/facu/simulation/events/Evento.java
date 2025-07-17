package com.facu.simulation.events;

import com.facu.simulation.engine.Simulador;

public abstract class Evento implements Comparable<Evento> {

    /**
     * Tiempo en el que ocurre el evento.
     */
    protected double tiempo;

    /**
     * Constructor de la clase Evento.
     * @param tiempo Tiempo en el que ocurre el evento.
     */
    public Evento(double tiempo) {
        this.tiempo = tiempo;
    }

    /**
     * Obtiene el tiempo del evento.
     * @return Tiempo del evento.
     */
    public double getTiempo() {
        return tiempo;
    }

    /**
     * Procesa el evento en el simulador.
     * Este método debe ser implementado por las subclases para definir
     * la lógica específica de cada tipo de evento.
     * @param simulador Instancia del simulador donde se procesará el evento.
     */
    public abstract void procesar(Simulador simulador);

    /**
     * Devuelve una representación en cadena del evento.
     * @return Descripción del evento.
     */
    @Override
    public int compareTo(Evento otro) {
        return Double.compare(this.tiempo, otro.tiempo);
    }
}