package com.facu.simulation.events;

import com.facu.simulation.engine.Simulador;
import com.facu.simulation.model.Barco;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode(callSuper = true)
public class LlegadaBarco extends Evento {
    private Barco barco;
    
    public LlegadaBarco(double tiempo, Barco barco) {
        super(tiempo);
        this.barco = barco;
    }
    
    @Override
    public void procesar(Simulador simulador) {
        simulador.procesarLlegadaBarco(this.barco);
    }

    // Setter and Getter
    public Barco getBarco() {
        return barco;
    }

    public void setBarco(Barco barco) {
        this.barco = barco;
    }
}