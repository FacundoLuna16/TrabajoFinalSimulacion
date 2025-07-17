package com.facu.simulation;

import com.facu.simulation.ui.VentanaPrincipal;
import com.formdev.flatlaf.FlatLightLaf;
import lombok.extern.java.Log;

import javax.swing.*;
import java.awt.*;

/**
 * Clase principal de la aplicación de simulación del puerto.
 * 
 * Esta aplicación simula el sistema de un puerto con barcos, muelles y grúas
 * usando eventos discretos para calcular métricas de rendimiento.
 * 
 * @author Facu
 * @version 1.0.0
 */
@Log
public class PuertoSimulationApp {
    
    public static void main(String[] args) {
        // Configurar el Look and Feel moderno
        try {
            // Usar FlatLaf con tema claro
            UIManager.setLookAndFeel(new FlatLightLaf());
            
            // Configuraciones adicionales para mejor apariencia
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
            
            log.info("FlatLaf Look and Feel configurado exitosamente");
            
        } catch (UnsupportedLookAndFeelException e) {
            log.severe("Error al configurar FlatLaf: " + e.getMessage());
            // Fallback al Look and Feel del sistema
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                log.severe("Error al configurar Look and Feel del sistema: " + ex.getMessage());
                // Como último recurso, usar el Look and Feel por defecto
                try {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (Exception e2) {
                    log.severe("Error al configurar Look and Feel por defecto: " + e2.getMessage());
                }
            }
        }
        
        // Ejecutar la interfaz en el Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaPrincipal().setVisible(true);
                log.info("Aplicación de simulación portuaria iniciada");
            }
        });
    }
}
