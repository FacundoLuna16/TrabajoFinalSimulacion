package com.facu.simulation.ui.estilos;

import java.awt.*;

/**
 * ⭐ TEMA OSCURO CENTRALIZADO ⭐
 * Todos los colores, fuentes y constantes visuales en un solo lugar
 */
public class TemaOscuro {
    
    // =============== COLORES PRINCIPALES ===============
    public static final Color COLOR_FONDO_PRINCIPAL = new Color(25, 25, 25);
    public static final Color COLOR_FONDO_PANEL = new Color(35, 35, 35);
    public static final Color COLOR_FONDO_CARD = new Color(45, 45, 45);
    public static final Color COLOR_FONDO_CARD_ELEVADO = new Color(55, 55, 55);
    public static final Color COLOR_BORDE = new Color(70, 70, 70);
    public static final Color COLOR_BORDE_ACTIVO = new Color(100, 149, 237);
    
    // =============== COLORES DE TEXTO ===============
    public static final Color COLOR_TEXTO_PRINCIPAL = new Color(240, 240, 240);
    public static final Color COLOR_TEXTO_SECUNDARIO = new Color(180, 180, 180);
    
    // =============== COLORES DE ACENTO ===============
    public static final Color COLOR_ACENTO = new Color(100, 149, 237);
    public static final Color COLOR_ACENTO_VERDE = new Color(76, 175, 80);
    public static final Color COLOR_ACENTO_NARANJA = new Color(255, 152, 0);
    public static final Color COLOR_ACENTO_PURPURA = new Color(156, 39, 176);
    public static final Color COLOR_ACENTO_CYAN = new Color(0, 188, 212);
    public static final Color COLOR_ACENTO_PURPURA_CLARO = new Color(186, 104, 200);
    public static final Color COLOR_ACENTO_CYAN_CLARO = new Color(77, 208, 225);
    
    // =============== COLORES DE ESTADO ===============
    public static final Color COLOR_EXITO = new Color(76, 175, 80);
    public static final Color COLOR_ALERTA = new Color(255, 193, 7);
    public static final Color COLOR_ERROR = new Color(244, 67, 54);
    
    // =============== COLORES DE CAMPOS ===============
    public static final Color COLOR_CAMPO_ACTIVO = new Color(55, 55, 55);
    public static final Color COLOR_CAMPO_INACTIVO = new Color(40, 40, 40);
    
    // =============== FUENTES ===============
    public static final Font FUENTE_TITULO = new Font(Font.SANS_SERIF, Font.BOLD, 14);
    public static final Font FUENTE_SUBTITULO = new Font(Font.SANS_SERIF, Font.BOLD, 12);
    public static final Font FUENTE_TEXTO = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    public static final Font FUENTE_TEXTO_PEQUEÑO = new Font(Font.SANS_SERIF, Font.ITALIC, 11);
    public static final Font FUENTE_DASHBOARD_GRANDE = new Font(Font.SANS_SERIF, Font.BOLD, 24);
    public static final Font FUENTE_DASHBOARD_PEQUEÑA = new Font(Font.SANS_SERIF, Font.BOLD, 10);
    public static final Font FUENTE_MONOSPACE = new Font(Font.MONOSPACED, Font.PLAIN, 14);
    public static final Font FUENTE_BOTON = new Font(Font.SANS_SERIF, Font.BOLD, 11);
    public static final Font FUENTE_TITULO_SECCION = new Font(Font.SANS_SERIF, Font.BOLD, 11);
    
    // =============== DIMENSIONES ===============
    public static final Dimension TAMAÑO_CAMPO_TEXTO = new Dimension(80, 32);
    public static final Dimension TAMAÑO_BOTON_PRIMARIO = new Dimension(90, 32);
    public static final Dimension TAMAÑO_TARJETA_ESTADISTICA = new Dimension(140, 85);
    
    // =============== ESPACIADO ===============
    public static final int MARGEN_PEQUEÑO = 5;
    public static final int MARGEN_MEDIO = 10;
    public static final int MARGEN_GRANDE = 15;
    public static final int ESPACIADO_COMPONENTES = 8;
    
    // =============== CONFIGURACIÓN SCROLLBARS ===============
    public static final int ANCHO_SCROLLBAR = 16;
    public static final Color COLOR_THUMB_SCROLLBAR = COLOR_ACENTO;
    public static final Color COLOR_THUMB_SCROLLBAR_HOVER = new Color(120, 169, 255);
    public static final Color COLOR_TRACK_SCROLLBAR = COLOR_FONDO_PANEL;
    
    /**
     * Obtiene un color de acento basado en un índice
     */
    public static Color obtenerColorAcento(int indice) {
        Color[] colores = {
            COLOR_ACENTO_VERDE,
            COLOR_ACENTO_NARANJA, 
            COLOR_ACENTO,
            COLOR_ACENTO_PURPURA,
            COLOR_ACENTO_CYAN,
            COLOR_ACENTO_PURPURA_CLARO,
            COLOR_ACENTO_CYAN_CLARO
        };
        return colores[indice % colores.length];
    }
    
    /**
     * Obtiene un emoji basado en un índice
     */
    public static String obtenerEmoji(int indice) {
        String[] emojis = {"🟢", "🟠", "🔵", "🟣", "🔷", "🟪", "💎"};
        return emojis[indice % emojis.length];
    }
}
