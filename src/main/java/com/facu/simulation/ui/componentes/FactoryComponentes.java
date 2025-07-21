package com.facu.simulation.ui.componentes;

import com.facu.simulation.ui.estilos.TemaOscuro;
import com.facu.simulation.ui.estilos.EfectosVisuales;

import javax.swing.*;
import java.awt.*;

/**
 * ⭐ FACTORY PARA CREAR COMPONENTES CON ESTILO CONSISTENTE ⭐
 * Centraliza la creación de todos los componentes UI
 */
public class FactoryComponentes {
    
    /**
     * Crea un campo de texto con el estilo del tema oscuro
     */
    public static JTextField crearCampoTexto(String valorInicial) {
        JTextField campo = new JTextField(valorInicial);
        campo.setBackground(TemaOscuro.COLOR_CAMPO_ACTIVO);
        campo.setForeground(TemaOscuro.COLOR_TEXTO_PRINCIPAL);
        campo.setCaretColor(TemaOscuro.COLOR_ACENTO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaOscuro.COLOR_BORDE, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        campo.setHorizontalAlignment(SwingConstants.CENTER);
        campo.setFont(TemaOscuro.FUENTE_MONOSPACE);
        campo.setPreferredSize(TemaOscuro.TAMAÑO_CAMPO_TEXTO);
        
        EfectosVisuales.aplicarEfectoFocusCampoTexto(campo);
        return campo;
    }
    
    /**
     * Crea un botón primario con color personalizado
     */
    public static JButton crearBotonPrimario(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(6, 12, 6, 11)
        ));
        boton.setPreferredSize(TemaOscuro.TAMAÑO_BOTON_PRIMARIO);
        boton.setFont(TemaOscuro.FUENTE_BOTON);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        EfectosVisuales.aplicarEfectoHoverBotonPrimario(boton, color);
        return boton;
    }
    
    /**
     * Crea un botón secundario
     */
    public static JButton crearBotonSecundario(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(TemaOscuro.COLOR_FONDO_PANEL);
        boton.setForeground(TemaOscuro.COLOR_TEXTO_PRINCIPAL);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaOscuro.COLOR_BORDE, 1),
                BorderFactory.createEmptyBorder(9, 19, 9, 19)
        ));
        boton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        EfectosVisuales.aplicarEfectoHoverBotonSecundario(boton);
        return boton;
    }
    
    /**
     * Crea un label para dashboard con fuente grande
     */
    public static JLabel crearLabelDashboard(String texto) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setFont(TemaOscuro.FUENTE_DASHBOARD_GRANDE);
        label.setOpaque(false);
        return label;
    }
    
    /**
     * Crea un checkbox con estilo del tema
     */
    public static JCheckBox crearCheckBox(String texto) {
        JCheckBox check = new JCheckBox(texto);
        check.setBackground(TemaOscuro.COLOR_FONDO_CARD);
        check.setForeground(TemaOscuro.COLOR_TEXTO_PRINCIPAL);
        check.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        check.setFocusPainted(false);
        return check;
    }
    
    /**
     * Crea un grupo de input con label y unidad
     */
    public static JPanel crearGrupoInput(String label, JTextField campo, String unidad) {
        JPanel panel = new JPanel(new BorderLayout(5, 3));
        panel.setBackground(TemaOscuro.COLOR_FONDO_CARD);

        JLabel lblTop = new JLabel(label, SwingConstants.CENTER);
        lblTop.setForeground(TemaOscuro.COLOR_TEXTO_SECUNDARIO);
        lblTop.setFont(TemaOscuro.FUENTE_TEXTO);

        JLabel lblUnidad = new JLabel(unidad, SwingConstants.CENTER);
        lblUnidad.setForeground(TemaOscuro.COLOR_TEXTO_SECUNDARIO);
        lblUnidad.setFont(TemaOscuro.FUENTE_TEXTO_PEQUEÑO);

        panel.add(lblTop, BorderLayout.NORTH);
        panel.add(campo, BorderLayout.CENTER);
        panel.add(lblUnidad, BorderLayout.SOUTH);

        return panel;
    }
    
    /**
     * Crea un título con borde para secciones
     */
    public static JPanel crearTituloSeccion(String titulo) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(TemaOscuro.COLOR_FONDO_PRINCIPAL);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(),
                titulo,
                0, 0,
                TemaOscuro.FUENTE_SUBTITULO,
                TemaOscuro.COLOR_ACENTO
        ));
        return panel;
    }
    
    /**
     * Crea un toggle switch (checkbox estilizado)
     */
    public static JCheckBox crearToggleSwitch(String texto) {
        JCheckBox toggle = crearCheckBox(texto);
        toggle.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaOscuro.COLOR_BORDE, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        toggle.setOpaque(true);
        return toggle;
    }
    
    /**
     * Ajusta el tamaño de botones para que sean iguales
     */
    public static void igualarTamañoBotones(JButton... botones) {
        if (botones.length == 0) return;
        
        int maxWidth = 0;
        int height = botones[0].getPreferredSize().height;
        
        // Encontrar el ancho máximo
        for (JButton boton : botones) {
            maxWidth = Math.max(maxWidth, boton.getPreferredSize().width);
        }
        
        // Aplicar el mismo tamaño a todos
        Dimension tamaño = new Dimension(maxWidth, height);
        for (JButton boton : botones) {
            boton.setPreferredSize(tamaño);
        }
    }
}
