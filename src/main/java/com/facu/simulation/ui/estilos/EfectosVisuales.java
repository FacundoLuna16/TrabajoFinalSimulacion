package com.facu.simulation.ui.estilos;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

/**
 * ⭐ EFECTOS VISUALES Y COMPONENTES UI PERSONALIZADOS ⭐
 * Bordes, sombras, scrollbars y otros efectos visuales
 */
public class EfectosVisuales {
    
    /**
     * Crea un borde simple sin título
     */
    public static javax.swing.border.Border crearBordeSimple() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaOscuro.COLOR_BORDE, 1),
                BorderFactory.createEmptyBorder(
                        TemaOscuro.MARGEN_PEQUEÑO, 
                        TemaOscuro.ESPACIADO_COMPONENTES, 
                        TemaOscuro.MARGEN_PEQUEÑO, 
                        TemaOscuro.ESPACIADO_COMPONENTES
                )
        );
    }
    
    /**
     * Crea un borde con título para cards
     */
    public static javax.swing.border.Border crearBordeCard(String titulo) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(TemaOscuro.COLOR_BORDE, 1),
                        titulo,
                        javax.swing.border.TitledBorder.LEFT,
                        javax.swing.border.TitledBorder.TOP,
                        TemaOscuro.FUENTE_SUBTITULO,
                        TemaOscuro.COLOR_ACENTO
                ),
                BorderFactory.createEmptyBorder(3, 8, 8, 8)
        );
    }
    
    /**
     * Crea efectos hover para botones
     */
    public static void aplicarEfectoHoverBotonPrimario(JButton boton, Color colorBase) {
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorBase.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorBase);
            }
        });
    }
    
    /**
     * Crea efectos hover para botones secundarios
     */
    public static void aplicarEfectoHoverBotonSecundario(JButton boton) {
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(TemaOscuro.COLOR_CAMPO_ACTIVO);
                boton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(TemaOscuro.COLOR_BORDE_ACTIVO, 1),
                        BorderFactory.createEmptyBorder(9, 19, 9, 19)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(TemaOscuro.COLOR_FONDO_PANEL);
                boton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(TemaOscuro.COLOR_BORDE, 1),
                        BorderFactory.createEmptyBorder(9, 19, 9, 19)
                ));
            }
        });
    }
    
    /**
     * Crea efectos focus para campos de texto
     */
    public static void aplicarEfectoFocusCampoTexto(JTextField campo) {
        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                campo.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(TemaOscuro.COLOR_BORDE_ACTIVO, 2),
                        BorderFactory.createEmptyBorder(5, 7, 5, 7)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                campo.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(TemaOscuro.COLOR_BORDE, 1),
                        BorderFactory.createEmptyBorder(6, 8, 6, 8)
                ));
            }
        });
    }
    
    /**
     * Crea efectos hover para tarjetas estadísticas
     */
    public static void aplicarEfectoHoverTarjeta(JPanel tarjeta, JLabel valor, Color colorAccent) {
        tarjeta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tarjeta.setBackground(TemaOscuro.COLOR_FONDO_CARD_ELEVADO.brighter());
                valor.setForeground(colorAccent.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tarjeta.setBackground(TemaOscuro.COLOR_FONDO_CARD_ELEVADO);
                valor.setForeground(colorAccent);
            }
        });
    }
    
    /**
     * Configura un scrollbar vertical con el estilo moderno
     */
    public static void configurarScrollBarModerno(JScrollBar scrollBar) {
        if (scrollBar.getOrientation() == JScrollBar.VERTICAL) {
            scrollBar.setPreferredSize(new Dimension(TemaOscuro.ANCHO_SCROLLBAR, 0));
        } else {
            scrollBar.setPreferredSize(new Dimension(0, TemaOscuro.ANCHO_SCROLLBAR));
        }
        scrollBar.setBackground(TemaOscuro.COLOR_FONDO_PANEL);
        scrollBar.setUI(new ModernScrollBarUI());
    }
}

/**
 * ⭐ BORDE CON SOMBRA SUTIL PARA EFECTO DE ELEVACIÓN ⭐
 */
class SombraSubtil extends AbstractBorder {
    private Color colorSombra;
    
    public SombraSubtil(Color colorBase) {
        this.colorSombra = new Color(colorBase.getRed(), colorBase.getGreen(), colorBase.getBlue(), 80);
    }
    
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Sombra sutil
        g2.setColor(colorSombra);
        g2.drawRoundRect(x + 1, y + 1, width - 2, height - 2, 8, 8);
        
        // Borde principal
        g2.setColor(colorSombra.brighter());
        g2.drawRoundRect(x, y, width - 1, height - 1, 8, 8);
        
        g2.dispose();
    }
    
    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(2, 2, 2, 2);
    }
    
    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}
/**
 * ⭐ SCROLLBAR MODERNA CON DISEÑO MINIMALISTA ⭐
 */
class ModernScrollBarUI extends BasicScrollBarUI {
    
    @Override
    protected void configureScrollBarColors() {
        this.thumbColor = TemaOscuro.COLOR_THUMB_SCROLLBAR;
        this.thumbHighlightColor = TemaOscuro.COLOR_THUMB_SCROLLBAR_HOVER;
        this.thumbDarkShadowColor = TemaOscuro.COLOR_THUMB_SCROLLBAR;
        this.thumbLightShadowColor = TemaOscuro.COLOR_THUMB_SCROLLBAR;
        this.trackColor = TemaOscuro.COLOR_TRACK_SCROLLBAR;
        this.trackHighlightColor = TemaOscuro.COLOR_TRACK_SCROLLBAR;
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    private JButton createZeroButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color color = isDragging ? TemaOscuro.COLOR_THUMB_SCROLLBAR_HOVER : TemaOscuro.COLOR_THUMB_SCROLLBAR;
        g2.setPaint(color);
        g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2,
                thumbBounds.width - 4, thumbBounds.height - 4, 8, 8);
        g2.dispose();
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setPaint(TemaOscuro.COLOR_TRACK_SCROLLBAR);
        g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        g2.dispose();
    }
}
