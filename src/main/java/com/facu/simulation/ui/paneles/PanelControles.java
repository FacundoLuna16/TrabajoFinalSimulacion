package com.facu.simulation.ui.paneles;

import com.facu.simulation.ui.componentes.FactoryComponentes;
import com.facu.simulation.ui.estilos.TemaOscuro;
import com.facu.simulation.ui.estilos.EfectosVisuales;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * 🎮 PANEL DE CONTROLES CENTRALES 🎮
 * Panel moderno con controles principales organizados en secciones
 * Mantiene diseño tipo tarjeta con efectos visuales mejorados
 */
public class PanelControles extends JPanel {

    private JButton btnSimular;
    private JButton btnReset;
    private JCheckBox chkFiltrarPorFila;

    public PanelControles() {
        inicializar();
    }

    /**
     * Inicializa el panel con los controles
     */
    private void inicializar() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(TemaOscuro.COLOR_FONDO_PRINCIPAL);
        setMaximumSize(new Dimension(180, Short.MAX_VALUE));
        setPreferredSize(new Dimension(180, 150)); // Misma altura que parámetros

        // Título de la sección
        JPanel tituloContainer = FactoryComponentes.crearTituloSeccion("🎮 CONTROLES");
        add(tituloContainer);
        
        // Container principal con diseño moderno
        JPanel containerPrincipal = new JPanel();
        containerPrincipal.setLayout(new BoxLayout(containerPrincipal, BoxLayout.Y_AXIS));
        containerPrincipal.setBackground(TemaOscuro.COLOR_FONDO_CARD);
        containerPrincipal.setBorder(BorderFactory.createCompoundBorder(
            EfectosVisuales.crearBordeSimple(),
            BorderFactory.createEmptyBorder(20, 15, 20, 15)
        ));
        
        crearBotones();
        crearCheckBox();
        organizarComponentes(containerPrincipal);
        add(containerPrincipal);
    }

    /**
     * Crea los botones principales
     */
    private void crearBotones() {
        btnSimular = FactoryComponentes.crearBotonPrimario("▶ Simular", TemaOscuro.COLOR_EXITO);
        btnSimular.setActionCommand("SIMULAR");
        
        btnReset = FactoryComponentes.crearBotonSecundario("↻ Reset");
        btnReset.setActionCommand("RESET");

        // Igualar tamaños de botones
        FactoryComponentes.igualarTamañoBotones(btnSimular, btnReset);
    }

    /**
     * Crea el checkbox de filtro por fila
     */
    private void crearCheckBox() {
        chkFiltrarPorFila = FactoryComponentes.crearCheckBox("Filtrar por Fila");
        chkFiltrarPorFila.setActionCommand("FILTRAR_POR_FILA");
        chkFiltrarPorFila.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    /**
     * Organiza todos los componentes en el panel
     */
    private void organizarComponentes(JPanel container) {
        // Sección de botones principales
        crearSeccionBotones(container);
        
        // Espaciador entre secciones
        container.add(Box.createRigidArea(new Dimension(0, TemaOscuro.MARGEN_GRANDE)));
        
        // Sección de opciones
        crearSeccionOpciones(container);
    }
    
    /**
     * Crea la sección de botones principales
     */
    private void crearSeccionBotones(JPanel container) {
        // Subtítulo
        JLabel lblSeccion = new JLabel("⚡ Acciones");
        lblSeccion.setFont(TemaOscuro.FUENTE_TITULO_SECCION);
        lblSeccion.setForeground(TemaOscuro.COLOR_ACENTO);
        lblSeccion.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSeccion.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        container.add(lblSeccion);
        
        // Botones centrados
        btnSimular.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnReset.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        container.add(btnSimular);
        container.add(Box.createRigidArea(new Dimension(0, TemaOscuro.ESPACIADO_COMPONENTES)));
        container.add(btnReset);
    }
    
    /**
     * Crea la sección de opciones de configuración
     */
    private void crearSeccionOpciones(JPanel container) {
        // Subtítulo
        JLabel lblSeccion = new JLabel("🔍 Filtros");
        lblSeccion.setFont(TemaOscuro.FUENTE_TITULO_SECCION);
        lblSeccion.setForeground(TemaOscuro.COLOR_ACENTO_CYAN);
        lblSeccion.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSeccion.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        container.add(lblSeccion);
        
        // CheckBox centrado
        chkFiltrarPorFila.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(chkFiltrarPorFila);
    }

    /**
     * Configura los listeners de eventos
     */
    public void configurarEventos(ActionListener eventHandler) {
        btnSimular.addActionListener(eventHandler);
        btnReset.addActionListener(eventHandler);
        chkFiltrarPorFila.addActionListener(eventHandler);
    }

    // =============== MÉTODOS PÚBLICOS PARA ACCESO ===============

    public JButton getBotonSimular() {
        return btnSimular;
    }

    public JButton getBotonReset() {
        return btnReset;
    }

    public boolean isFiltrarPorFila() {
        return chkFiltrarPorFila.isSelected();
    }

    public void setFiltrarPorFila(boolean filtrar) {
        chkFiltrarPorFila.setSelected(filtrar);
    }

    /**
     * Resetea los controles a estado inicial
     */
    public void resetearControles() {
        chkFiltrarPorFila.setSelected(false);
    }
}
