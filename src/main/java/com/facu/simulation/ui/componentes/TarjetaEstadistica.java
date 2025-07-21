package com.facu.simulation.ui.componentes;

import com.facu.simulation.ui.estilos.TemaOscuro;
import com.facu.simulation.ui.estilos.EfectosVisuales;
//import com.facu.simulation.ui.estilos.SombraSubtil;

import javax.swing.*;
import java.awt.*;

/**
 * ⭐ TARJETA ESTADÍSTICA REUTILIZABLE ⭐
 * Componente moderno tipo dashboard para mostrar métricas
 */
public class TarjetaEstadistica extends JPanel {
    
    private JLabel labelValor;
    private Color colorAccent;
    private String unidad;
    
    /**
     * Constructor principal
     */
    public TarjetaEstadistica(String titulo, String unidad, Color colorAccent, String emoji) {
        this.colorAccent = colorAccent;
        this.unidad = unidad;
        
        inicializar(titulo, emoji);
        configurarEstilo();
        aplicarEfectos();
    }
    
    /**
     * Inicializa la estructura del componente
     */
    private void inicializar(String titulo, String emoji) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(TemaOscuro.TAMAÑO_TARJETA_ESTADISTICA);
        
        // HEADER: Emoji + Título
        JPanel header = crearHeader(titulo, emoji);
        
        // VALOR PRINCIPAL (grande y colorido)
        labelValor = FactoryComponentes.crearLabelDashboard("--");
        labelValor.setForeground(colorAccent);
        labelValor.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // FOOTER: Unidad
        JLabel lblUnidad = crearFooter();
        
        // Ensamblar componente
        add(header);
        add(Box.createVerticalGlue());
        add(labelValor);
        add(Box.createRigidArea(new Dimension(0, 2)));
        add(lblUnidad);
        add(Box.createVerticalGlue());
    }
    
    /**
     * Crea el header con emoji y título
     */
    private JPanel crearHeader(String titulo, String emoji) {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 0));
        header.setBackground(TemaOscuro.COLOR_FONDO_CARD_ELEVADO);

        JLabel lblEmoji = new JLabel(emoji);
        lblEmoji.setFont(TemaOscuro.FUENTE_TEXTO);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(TemaOscuro.COLOR_TEXTO_SECUNDARIO);
        lblTitulo.setFont(TemaOscuro.FUENTE_DASHBOARD_PEQUEÑA);

        header.add(lblEmoji);
        header.add(lblTitulo);
        
        return header;
    }
    
    /**
     * Crea el footer con la unidad
     */
    private JLabel crearFooter() {
        JLabel lblUnidad = new JLabel(unidad);
        lblUnidad.setForeground(TemaOscuro.COLOR_TEXTO_SECUNDARIO);
        lblUnidad.setFont(TemaOscuro.FUENTE_TEXTO_PEQUEÑO);
        lblUnidad.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lblUnidad;
    }
    
    /**
     * Configura el estilo visual del componente
     */
    private void configurarEstilo() {
        setBackground(TemaOscuro.COLOR_FONDO_CARD_ELEVADO);
        
//        // Efecto de elevación con sombra sutil
//        setBorder(BorderFactory.createCompoundBorder(
//                new SombraSubtil(colorAccent),
//                BorderFactory.createEmptyBorder(8, 10, 8, 10)
//        ));
    }
    
    /**
     * Aplica efectos interactivos
     */
    private void aplicarEfectos() {
        EfectosVisuales.aplicarEfectoHoverTarjeta(this, labelValor, colorAccent);
    }
    
    // =============== MÉTODOS PÚBLICOS ===============
    
    /**
     * Actualiza el valor mostrado en la tarjeta
     */
    public void actualizarValor(String nuevoValor) {
        SwingUtilities.invokeLater(() -> {
            labelValor.setText(nuevoValor);
            repaint();
        });
    }
    
    /**
     * Actualiza el valor con formato decimal
     */
    public void actualizarValor(double valor, java.text.DecimalFormat formato) {
        actualizarValor(formato.format(valor));
    }
    
    /**
     * Resetea la tarjeta a su estado inicial
     */
    public void resetear() {
        actualizarValor("--");
    }
    
    /**
     * Obtiene el label del valor (para acceso directo si es necesario)
     */
    public JLabel getLabelValor() {
        return labelValor;
    }
    
    /**
     * Cambia el color de acento dinámicamente
     */
    public void cambiarColorAccent(Color nuevoColor) {
        this.colorAccent = nuevoColor;
        labelValor.setForeground(nuevoColor);
        configurarEstilo();
        repaint();
    }
    
    /**
     * Habilita o deshabilita la tarjeta
     */
    public void setHabilitada(boolean habilitada) {
        setEnabled(habilitada);
        Color color = habilitada ? colorAccent : TemaOscuro.COLOR_TEXTO_SECUNDARIO;
        labelValor.setForeground(color);
        repaint();
    }
}
