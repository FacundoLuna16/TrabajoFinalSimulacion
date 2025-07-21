package com.facu.simulation.ui;

import com.facu.simulation.ui.paneles.PanelParametros;
import com.facu.simulation.ui.paneles.PanelControles;
import com.facu.simulation.ui.paneles.PanelEstadisticas;
import com.facu.simulation.ui.eventos.EventHandlerSimulacion;
import com.facu.simulation.ui.estilos.TemaOscuro;
import com.facu.simulation.ui.estilos.EfectosVisuales;

import javax.swing.*;
import java.awt.*;

/**
 * ⭐ VENTANA PRINCIPAL REFACTORIZADA ⭐
 * Versión limpia y modular con separación de responsabilidades
 */
public class VentanaPrincipalRefactorizada extends JFrame {

    // =============== PANELES PRINCIPALES ===============
    private JPanel panelPrincipal;
    private PanelParametros panelParametros;
    private PanelControles panelControles;
    private PanelEstadisticas panelEstadisticas;
    private TablaMejorada tablaMejorada;

    // =============== MANEJADOR DE EVENTOS ===============
    private EventHandlerSimulacion eventHandler;

    // =============== CONFIGURACIÓN INICIAL ===============
    private final int cantidadMuellesActual = 2;
    private final int cantidadGruasActual = 2;

    // =============== CONSTRUCTOR ===============

    public VentanaPrincipalRefactorizada() {
        configurarLookAndFeel();
        inicializarComponentes();
        configurarVentana();
        configurarEventos();
    }

    /**
     * Configura el look and feel para el tema oscuro
     */
    private void configurarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            configurarScrollBars();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Configura las scrollbars globalmente
     */
    private void configurarScrollBars() {
        UIManager.put("ScrollBar.width", TemaOscuro.ANCHO_SCROLLBAR);
        UIManager.put("ScrollBar.thumbDarkShadow", TemaOscuro.COLOR_BORDE);
        UIManager.put("ScrollBar.thumbHighlight", TemaOscuro.COLOR_THUMB_SCROLLBAR);
        UIManager.put("ScrollBar.thumbShadow", TemaOscuro.COLOR_BORDE_ACTIVO);
        UIManager.put("ScrollBar.track", TemaOscuro.COLOR_FONDO_PANEL);
        UIManager.put("ScrollBar.trackHighlight", TemaOscuro.COLOR_FONDO_CARD);
    }

    /**
     * Inicializa todos los componentes principales
     */
    private void inicializarComponentes() {
        // 1. Crear el panel principal con GridBagLayout
        panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBackground(TemaOscuro.COLOR_FONDO_PRINCIPAL);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(
                TemaOscuro.MARGEN_GRANDE, TemaOscuro.MARGEN_GRANDE, 
                TemaOscuro.MARGEN_GRANDE, TemaOscuro.MARGEN_GRANDE
        ));
        
        // 2. Crear los paneles especializados
        crearPaneles();
        
        // 3. Organizar en el layout principal
        organizarLayout();
    }

    /**
     * Crea todos los paneles especializados
     */
    private void crearPaneles() {
        panelParametros = new PanelParametros();
        panelControles = new PanelControles();
        panelEstadisticas = new PanelEstadisticas();
        
        // Crear tabla mejorada
        crearTablaVectorEstado();
    }

    /**
     * Organiza todos los componentes usando GridBagLayout
     */
    private void organizarLayout() {
        GridBagConstraints gbc = new GridBagConstraints();
        
        // A. Panel de Parámetros (Izquierda)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, TemaOscuro.MARGEN_MEDIO, TemaOscuro.MARGEN_MEDIO);
        panelPrincipal.add(panelParametros, gbc);

        // B. Panel de Controles (Centro)
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panelPrincipal.add(panelControles, gbc);

        // C. Panel de Estadísticas (Derecha)
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.6;
        gbc.fill = GridBagConstraints.BOTH;
        panelPrincipal.add(panelEstadisticas, gbc);

        // D. Tabla de Simulación (Abajo, ocupando todo el ancho)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        panelPrincipal.add(tablaMejorada, gbc);
    }

    /**
     * Crea la tabla con scroll personalizado
     */
    private void crearTablaVectorEstado() {
        tablaMejorada = new TablaMejorada(cantidadMuellesActual, cantidadGruasActual);
        
        // Personalizar el scroll pane
        JScrollPane scrollPane = tablaMejorada.getScrollPane();
        
        // Configurar scroll bars con estilo moderno
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setPreferredSize(new Dimension(TemaOscuro.ANCHO_SCROLLBAR, 0));
        verticalScrollBar.setBackground(TemaOscuro.COLOR_FONDO_PANEL);
        EfectosVisuales.configurarScrollBarModerno(verticalScrollBar);
        
        JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
        horizontalScrollBar.setPreferredSize(new Dimension(0, TemaOscuro.ANCHO_SCROLLBAR));
        horizontalScrollBar.setBackground(TemaOscuro.COLOR_FONDO_PANEL);
        EfectosVisuales.configurarScrollBarModerno(horizontalScrollBar);
        
        // Configurar el scroll pane
        scrollPane.setBorder(null);
        scrollPane.setBackground(TemaOscuro.COLOR_FONDO_CARD);
        scrollPane.getViewport().setBackground(TemaOscuro.COLOR_FONDO_CARD);
        
        // Configurar la tabla
        JTable tabla = tablaMejorada.getTabla();
        tabla.setRowHeight(28);
        tablaMejorada.ajustarAnchoColumnas();
        tabla.clearSelection();
    }

    /**
     * Configura la ventana principal
     */
    private void configurarVentana() {
        setTitle("🚢 Simulación de Barcos en Bahía - Versión Refactorizada");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panelPrincipal);
        setSize(1500, 1000);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1300, 800));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(TemaOscuro.COLOR_FONDO_PRINCIPAL);
    }

    /**
     * Configura todos los eventos
     */
    private void configurarEventos() {
        // Crear el manejador central de eventos
        eventHandler = new EventHandlerSimulacion(
            panelParametros,
            panelControles,
            panelEstadisticas,
            tablaMejorada,
            this
        );
        
        // Configurar eventos en los paneles
        panelControles.configurarEventos(eventHandler);
    }

    // =============== MÉTODO MAIN PARA EJECUTAR ===============

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Configurar el look and feel
                UIManager.setLookAndFeel(UIManager.getLookAndFeel());
                
                new VentanaPrincipalRefactorizada().setVisible(true);
                
            } catch (Exception e) {
                e.printStackTrace();
                
                // Fallback: crear ventana sin look and feel personalizado
                new VentanaPrincipalRefactorizada().setVisible(true);
            }
        });
    }
}
