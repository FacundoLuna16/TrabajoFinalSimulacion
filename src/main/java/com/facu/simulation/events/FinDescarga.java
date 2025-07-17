package com.facu.simulation.events;

import com.facu.simulation.engine.Simulador;
import com.facu.simulation.model.Barco;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
public class FinDescarga extends Evento {
    private Barco barco;
    
    public FinDescarga(double tiempo, Barco barco) {
        super(tiempo);
        this.barco = barco;
    }
    
    @Override
    public void procesar(Simulador simulador) {
        simulador.procesarFinDescarga(this.barco);
    }
    // Setter and Getter
    public Barco getBarco() {
        return barco;
    }
    public void setBarco(Barco barco) {
        this.barco = barco;
    }

}