package com.facu.simulation.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GeneradorAleatorio {

    private final Random random;

    /**
     * Constructor que inicializa el generador con una semilla específica.
     * @param seed La semilla para el generador de números aleatorios.
     */
    public GeneradorAleatorio(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Redondea un valor a 4 decimales, para simular el comportamiento del código Python.
     * @param value El valor a redondear.
     * @return El valor redondeado.
     */
    private double roundToFourDecimals(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }

    /**
     * Genera un número aleatorio con distribución uniforme entre a y b.
     * @param a Límite inferior.
     * @param b Límite superior.
     * @return Un número aleatorio con distribución uniforme.
     */
    public double generarUniforme(double a, double b) {
        double rnd = this.random.nextDouble(); // Genera un valor entre 0.0 y 1.0
        return roundToFourDecimals(a + rnd * (b - a));
    }

    /**
     * Genera un array de n números aleatorios con distribución uniforme y los devuelve ordenados.
     * @param a Límite inferior.
     * @param b Límite superior.
     * @param n Cantidad de números a generar.
     * @return Un array de números ordenados.
     */
    public double[] generarNumerosUniformes(double a, double b, int n) {
        double[] numeros = new double[n];
        for (int i = 0; i < n; i++) {
            numeros[i] = generarUniforme(a, b);
        }
        Arrays.sort(numeros);
        return numeros;
    }

    /**
     * Genera un número aleatorio con distribución exponencial negativa.
     * @param media La media de la distribución.
     * @return Un número aleatorio con distribución exponencial.
     */
    public double generarExponencialNegativa(double media) {
        if (media <= 0) {
            throw new IllegalArgumentException("La media debe ser un valor positivo.");
        }
        double rnd = this.random.nextDouble();
        // Se evita que rnd sea 1.0, lo que causaría que Math.log(0) sea infinito.
        if (rnd == 1.0) {
            rnd = 0.999999;
        }
        return roundToFourDecimals(-media * Math.log(1 - rnd));
    }

    /**
     * Genera un array de n números aleatorios con distribución exponencial y los devuelve ordenados.
     * @param media La media de la distribución.
     * @param n Cantidad de números a generar.
     * @return Un array de números ordenados.
     */
    public double[] generarNumerosExponenciales(double media, int n) {
        double[] numeros = new double[n];
        for (int i = 0; i < n; i++) {
            numeros[i] = generarExponencialNegativa(media);
        }
        Arrays.sort(numeros);
        return numeros;
    }

    /**
     * Genera una lista de n números aleatorios con distribución normal usando el método de Box-Muller.
     * @param media La media de la distribución.
     * @param desviacion La desviación estándar de la distribución.
     * @param n La cantidad de números a generar.
     * @return Una lista de números aleatorios con distribución normal.
     */
    public List<Double> generarNormales(double media, double desviacion, int n) {
        List<Double> datos = new ArrayList<>();
        while (datos.size() < n) {
            double u1 = this.random.nextDouble();
            double u2 = this.random.nextDouble();

            // Evita que u1 sea 0, ya que el logaritmo no está definido.
            if (u1 == 0) {
                u1 = 1e-10; // Un valor muy pequeño, pero no cero.
            }

            // Transformada de Box-Muller para generar dos valores normales estándar (z1, z2)
            double z1 = Math.sqrt(-2 * Math.log(u1)) * Math.cos(2 * Math.PI * u2);
            double z2 = Math.sqrt(-2 * Math.log(u1)) * Math.sin(2 * Math.PI * u2);

            // Se ajustan los valores a la media y desviación deseadas
            datos.add(roundToFourDecimals(z1 * desviacion + media));
            datos.add(roundToFourDecimals(z2 * desviacion + media));
        }

        // Se ajusta la lista al tamaño exacto solicitado (n), eliminando el sobrante.
        while (datos.size() > n) {
            datos.remove(datos.size() - 1);
        }
        return datos;
    }
    
    /**
     * Genera un número aleatorio entre 0 y 1.
     * @return Un número aleatorio entre 0.0 y 1.0
     */
    public double generarNumeroAleatorio() {
        return roundToFourDecimals(this.random.nextDouble());
    }
    
    /**
     * Convierte un número aleatorio uniforme [0,1] a distribución exponencial.
     * @param rnd Número aleatorio entre 0 y 1
     * @param media La media de la distribución exponencial
     * @return Valor con distribución exponencial
     */
    public double convertirAExponencial(double rnd, double media) {
        if (media <= 0) {
            throw new IllegalArgumentException("La media debe ser un valor positivo.");
        }
        if (rnd == 1.0) {
            rnd = 0.999999;
        }
        return roundToFourDecimals(-media * Math.log(1 - rnd));
    }
    
    /**
     * Convierte un número aleatorio uniforme [0,1] a distribución uniforme [a,b].
     * @param rnd Número aleatorio entre 0 y 1
     * @param a Límite inferior
     * @param b Límite superior
     * @return Valor con distribución uniforme entre a y b
     */
    public double convertirAUniforme(double rnd, double a, double b) {
        return roundToFourDecimals(a + rnd * (b - a));
    }
}