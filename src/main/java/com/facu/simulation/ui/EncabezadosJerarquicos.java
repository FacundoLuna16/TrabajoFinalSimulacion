package com.facu.simulation.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Componente que crea encabezados jerárquicos reales para la tabla de simulación.
 * Los grupos padre abarcan múltiples columnas hijas, implementando el esquema diseñado.
 */
public class EncabezadosJerarquicos extends JPanel {
    
    private static final int ALTURA_PADRE = 30;
    private static final int ALTURA_HIJO = 25;
    
    private int maxBarcosEnSistema = 0;
    
    public EncabezadosJerarquicos() {
        setLayout(new BorderLayout());
        setBackground(new Color(40, 40, 40));
        inicializar(0); // Sin barcos dinámicos inicialmente
    }
    
    /**
     * Actualiza los encabezados cuando cambia el número de barcos en sistema
     */
    public void actualizarParaBarcos(int maxBarcosEnSistema) {
        if (this.maxBarcosEnSistema != maxBarcosEnSistema) {
            this.maxBarcosEnSistema = maxBarcosEnSistema;
            removeAll();
            inicializar(maxBarcosEnSistema);
            revalidate();
            repaint();
        }
    }
    
    private void inicializar(int maxBarcosEnSistema) {
        setPreferredSize(new Dimension(calcularAnchoTotal(maxBarcosEnSistema), ALTURA_PADRE + ALTURA_HIJO + 4));
        
        // Panel para encabezados padre (nivel superior)
        JPanel panelPadre = new JPanel();
        panelPadre.setLayout(new BoxLayout(panelPadre, BoxLayout.X_AXIS));
        panelPadre.setBackground(new Color(40, 40, 40));
        panelPadre.setPreferredSize(new Dimension(calcularAnchoTotal(maxBarcosEnSistema), ALTURA_PADRE));
        
        // Panel para encabezados hijo (nivel inferior)
        JPanel panelHijo = new JPanel();
        panelHijo.setLayout(new BoxLayout(panelHijo, BoxLayout.X_AXIS));
        panelHijo.setBackground(new Color(45, 45, 45));
        panelHijo.setPreferredSize(new Dimension(calcularAnchoTotal(maxBarcosEnSistema), ALTURA_HIJO));
        
        crearEncabezadosPadre(panelPadre, maxBarcosEnSistema);
        crearEncabezadosHijo(panelHijo, maxBarcosEnSistema);
        
        add(panelPadre, BorderLayout.NORTH);
        add(panelHijo, BorderLayout.CENTER);
        
        setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
    }
    
    /**
     * Sincroniza los anchos con la tabla real para evitar desfases
     */
    public void sincronizarConTabla(javax.swing.JTable tabla) {
        if (tabla != null && tabla.getColumnModel() != null) {
            // Recalcular anchos basándose en las columnas reales de la tabla
            removeAll();
            inicializarConAnchosSincronizados(tabla, maxBarcosEnSistema);
            revalidate();
            repaint();
        }
    }
    
    private void inicializarConAnchosSincronizados(javax.swing.JTable tabla, int maxBarcosEnSistema) {
        // Obtener anchos reales de las columnas de la tabla
        int[] anchosReales = new int[tabla.getColumnCount()];
        int anchoTotal = 0;
        
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            // Usar width actual en lugar de preferredWidth para mayor precisión
            int anchoActual = tabla.getColumnModel().getColumn(i).getWidth();
            if (anchoActual <= 0) {
                // Si no tiene ancho actual, usar el preferido
                anchoActual = tabla.getColumnModel().getColumn(i).getPreferredWidth();
            }
            anchosReales[i] = Math.max(anchoActual, 60); // Mínimo 60 píxeles
            anchoTotal += anchosReales[i];
            
            // Debug: mostrar información de columnas (comentado - descomentar si hay problemas)
            // if (i < 5 || i >= tabla.getColumnCount() - 3) {
            //     System.out.println("Columna " + i + ": " + tabla.getColumnName(i) + " - Ancho: " + anchosReales[i]);
            // }
        }
        
        // System.out.println("Ancho total calculado: " + anchoTotal + " | Columnas: " + tabla.getColumnCount());
        setPreferredSize(new Dimension(anchoTotal, ALTURA_PADRE + ALTURA_HIJO + 4));
        
        // Panel padre con anchos sincronizados
        JPanel panelPadre = new JPanel();
        panelPadre.setLayout(new BoxLayout(panelPadre, BoxLayout.X_AXIS));
        panelPadre.setBackground(new Color(40, 40, 40));
        panelPadre.setPreferredSize(new Dimension(anchoTotal, ALTURA_PADRE));
        panelPadre.setMinimumSize(new Dimension(anchoTotal, ALTURA_PADRE));
        
        // Panel hijo con anchos sincronizados
        JPanel panelHijo = new JPanel();
        panelHijo.setLayout(new BoxLayout(panelHijo, BoxLayout.X_AXIS));
        panelHijo.setBackground(new Color(45, 45, 45));
        panelHijo.setPreferredSize(new Dimension(anchoTotal, ALTURA_HIJO));
        panelHijo.setMinimumSize(new Dimension(anchoTotal, ALTURA_HIJO));
        
        crearEncabezadosSincronizados(panelPadre, panelHijo, anchosReales, maxBarcosEnSistema);
        
        add(panelPadre, BorderLayout.NORTH);
        add(panelHijo, BorderLayout.CENTER);
        
        setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
    }
    
    private int calcularAnchoTotal(int maxBarcosEnSistema) {
        // Anchos base según GeneradorColumnasTabla
        int anchoBase = 60 + 120 + 80 +  // Control (3): Fila, Evento, Reloj
                       80 + 100 +         // Llegada (2): RNDLleg, ProxLleg  
                       80 + 100 + 100 +   // Descarga M1 (3): RNDM1, TiemRest1, FinDescM1
                       80 + 100 + 100 +   // Descarga M2 (3): RNDM2, TiemRest2, FinDescM2
                       60 +               // Bahía (1): Bahía
                       100 + 100 + 100 + 100 + // Estados Muelles (4): M1Est, M1Inic, M2Est, M2Inic
                       100 + 100 + 100 + 100 + // Estados Grúas (4): G1Est, G1Inic, G2Est, G2Inic
                       80 + 80 + 80 + 100 + 80 + // T. Permanencia (5): MaxTPer, MinTPer, AcTPer, CantB, MedTPer
                       100 + 80 + 100 + 80 + 100 + 80 + 100 + 80 + // Utilización (8): M1AcTOc, M1Ut%, M2AcTOc, M2Ut%, G1AcTOc, G1Ut%, G2AcTOc, G2Ut%
                       80; // Sistema (1): BSist
        
        // Agregar anchos de barcos dinámicos (según GeneradorColumnasTabla)
        int anchoBarcos = maxBarcosEnSistema * (60 + 80 + 100); // B{i}_ID + B{i}_Estado + B{i}_Ingr
        
        return anchoBase + anchoBarcos;
    }
    
    private void crearEncabezadosPadre(JPanel panel, int maxBarcosEnSistema) {
        // CONTROL (3 columnas: 60+120+80 = 260)
        panel.add(crearLabelPadre("CONTROL", new Color(240, 240, 240), 260));
        
        // LLEGADA BARCO (2 columnas: 80+100 = 180)
        panel.add(crearLabelPadre("LLEGADA BARCO", new Color(255, 228, 196), 180));
        
        // DESCARGA MUELLE 1 (3 columnas: 80+100+100 = 280)
        panel.add(crearLabelPadre("DESCARGA MUELLE 1", new Color(176, 196, 222), 280));
        
        // DESCARGA MUELLE 2 (3 columnas: 80+100+100 = 280)
        panel.add(crearLabelPadre("DESCARGA MUELLE 2", new Color(135, 155, 180), 280));
        
        // BAHÍA (1 columna: 60)
        panel.add(crearLabelPadre("BAHÍA", new Color(221, 160, 221), 60));
        
        // ESTADOS MUELLES (4 columnas: 100*4 = 400)
        panel.add(crearLabelPadre("MUELLES", new Color(152, 251, 152), 400));
        
        // ESTADOS GRÚAS (4 columnas: 100*4 = 400)
        panel.add(crearLabelPadre("GRÚAS", new Color(255, 182, 193), 400));
        
        // T. PERMANENCIA (5 columnas: 80+80+80+100+80 = 420)
        panel.add(crearLabelPadre("T. PERMANENCIA", new Color(255, 255, 224), 420));
        
        // UTILIZACIÓN MUELLE (4 columnas: 100+80+100+80 = 360)
        panel.add(crearLabelPadre("UTILIZACIÓN MUELLE", new Color(135, 206, 250), 360));
        
        // UTILIZACIÓN GRÚA (4 columnas: 100+80+100+80 = 360)
        panel.add(crearLabelPadre("UTILIZACIÓN GRÚA", new Color(165, 180, 70), 360));
        
        // BARCOS EN SISTEMA (1 columna: 80)
        panel.add(crearLabelPadre("BARCOS SISTEMA", new Color(255, 218, 185), 80));
        
        // BARCOS DINÁMICOS (B1, B2, B3, etc.)
        for (int i = 1; i <= maxBarcosEnSistema; i++) {
            Color colorBarco = new Color(255, Math.max(140, 218 - (i * 15)), Math.max(100, 185 - (i * 10)));
            panel.add(crearLabelPadre("B" + i, colorBarco, 240)); // 60+80+100 = 240
        }
    }
    
    private void crearEncabezadosHijo(JPanel panel, int maxBarcosEnSistema) {
        // Mapeo exacto con GeneradorColumnasTabla.COLUMNAS_BASE
        String[] columnasHijas = {
            // Control (3)
            "Fila", "Evento", "Reloj",
            // Llegada (2) 
            "RND", "Próx. Llegada",
            // Descarga M1 (3)
            "RND", "T. Restante", "Fin Descarga",
            // Descarga M2 (3)
            "RND", "T. Restante", "Fin Descarga",
            // Bahía (1)
            "Cola",
            // Estados Muelles (4)
            "M1 Estado", "M1 Inicio", "M2 Estado", "M2 Inicio",
            // Estados Grúas (4)
            "G1 Estado", "G1 Inicio", "G2 Estado", "G2 Inicio",
            // T. Permanencia (5)
            "Máx", "Mín", "Acum.", "Cant.", "Media",
            // Utilización (8)
            "M1 Ac.T.O.", "M1 %", "M2 Ac.T.O.", "M2 %", "G1 Ac.T.O.", "G1 %", "G2 Ac.T.O.", "G2 %",
            // Sistema (1)
            "Total"
        };
        
        Color[] coloresHijas = {
            // Control (3)
            new Color(240, 240, 240), new Color(240, 240, 240), new Color(240, 240, 240),
            // Llegada (2)
            new Color(255, 228, 196), new Color(255, 228, 196),
            // Descarga M1 (3)
            new Color(176, 196, 222), new Color(176, 196, 222), new Color(176, 196, 222),
            // Descarga M2 (3)
            new Color(135, 155, 180), new Color(135, 155, 180), new Color(135, 155, 180),
            // Bahía (1)
            new Color(221, 160, 221),
            // Estados Muelles (4)
            new Color(152, 251, 152), new Color(152, 251, 152), new Color(152, 251, 152), new Color(152, 251, 152),
            // Estados Grúas (4)
            new Color(255, 182, 193), new Color(255, 182, 193), new Color(255, 182, 193), new Color(255, 182, 193),
            // T. Permanencia (5)
            new Color(255, 255, 224), new Color(255, 255, 224), new Color(255, 255, 224), new Color(255, 255, 224), new Color(255, 255, 224),
            // Utilización Muelle (4)
            new Color(135, 206, 250), new Color(135, 206, 250), new Color(135, 206, 250), new Color(135, 206, 250),
            // Utilización Grúa (4)
            new Color(165, 180, 70), new Color(165, 180, 70), new Color(165, 180, 70), new Color(165, 180, 70),
            // Sistema (1)
            new Color(255, 218, 185)
        };
        
        int[] anchosHijas = {
            // Control (3)
            60, 120, 80,
            // Llegada (2)
            80, 100,
            // Descarga M1 (3)
            80, 100, 100,
            // Descarga M2 (3)
            80, 100, 100,
            // Bahía (1)
            60,
            // Estados Muelles (4)
            100, 100, 100, 100,
            // Estados Grúas (4)
            100, 100, 100, 100,
            // T. Permanencia (5)
            80, 80, 80, 100, 80,
            // Utilización (8)
            100, 80, 100, 80, 100, 80, 100, 80,
            // Sistema (1)
            80
        };
        
        // Agregar columnas base
        for (int i = 0; i < columnasHijas.length; i++) {
            panel.add(crearLabelHijo(columnasHijas[i], coloresHijas[i], anchosHijas[i]));
        }
        
        // Agregar columnas dinámicas de barcos
        for (int i = 1; i <= maxBarcosEnSistema; i++) {
            Color colorBarco = new Color(255, Math.max(140, 218 - (i * 15)), Math.max(100, 185 - (i * 10)));
            panel.add(crearLabelHijo("ID", colorBarco, 60));
            panel.add(crearLabelHijo("Estado", colorBarco, 80));
            panel.add(crearLabelHijo("T. Ingreso", colorBarco, 100));
        }
    }
    
    private JLabel crearLabelPadre(String texto, Color color, int ancho) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(color);
        label.setForeground(Color.BLACK);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(2, 4, 2, 4)
        ));
        label.setPreferredSize(new Dimension(ancho, ALTURA_PADRE));
        label.setMinimumSize(new Dimension(ancho, ALTURA_PADRE));
        label.setMaximumSize(new Dimension(ancho, ALTURA_PADRE));
        return label;
    }
    
    private JLabel crearLabelHijo(String texto, Color color, int ancho) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(color);
        label.setForeground(Color.BLACK);
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLoweredBevelBorder(),
            BorderFactory.createEmptyBorder(2, 3, 2, 3)
        ));
        label.setPreferredSize(new Dimension(ancho, ALTURA_HIJO));
        label.setMinimumSize(new Dimension(ancho, ALTURA_HIJO));
        label.setMaximumSize(new Dimension(ancho, ALTURA_HIJO));
        return label;
    }
    
    /**
     * Crea encabezados con anchos sincronizados a las columnas reales de la tabla
     */
    private void crearEncabezadosSincronizados(JPanel panelPadre, JPanel panelHijo, int[] anchosReales, int maxBarcosEnSistema) {
        // Mapeo de columnas base - debe coincidir exactamente con GeneradorColumnasTabla
        String[] gruposPadre = {"CONTROL", "LLEGADA BARCO", "DESCARGA MUELLE 1", "DESCARGA MUELLE 2", 
                               "BAHÍA", "ESTADOS MUELLES", "ESTADOS GRÚAS", "T. PERMANENCIA", 
                               "UTILIZACIÓN MUELLE", "UTILIZACIÓN GRÚA", "BARCOS SISTEMA"};
        
        int[] columnasEnGrupo = {3, 2, 3, 3, 1, 4, 4, 5, 4, 4, 1}; // Número de columnas hijo por grupo padre
        
        Color[] coloresPadre = {
            new Color(240, 240, 240), new Color(255, 228, 196), new Color(176, 196, 222),
            new Color(135, 155, 180), new Color(221, 160, 221), new Color(152, 251, 152),
            new Color(255, 182, 193), new Color(255, 255, 224), new Color(135, 206, 250),
            new Color(165, 180, 70), new Color(255, 218, 185)
        };
        
        // Columnas hijas base
        String[] columnasHijas = {
            // Control (3)
            "Fila", "Evento", "Reloj",
            // Llegada (2) 
            "RND", "Próx. Llegada",
            // Descarga M1 (3)
            "RND", "T. Restante", "Fin Descarga",
            // Descarga M2 (3)
            "RND", "T. Restante", "Fin Descarga",
            // Bahía (1)
            "Cola",
            // Estados Muelles (4)
            "M1 Estado", "M1 Inicio", "M2 Estado", "M2 Inicio",
            // Estados Grúas (4)
            "G1 Estado", "G1 Inicio", "G2 Estado", "G2 Inicio",
            // T. Permanencia (5)
            "Máx", "Mín", "Acum.", "Cant.", "Media",
            // Utilización (8)
            "M1 Ac.T.O.", "M1 %", "M2 Ac.T.O.", "M2 %", "G1 Ac.T.O.", "G1 %", "G2 Ac.T.O.", "G2 %",
            // Sistema (1)
            "Total"
        };
        
        // Crear encabezados padre con anchos calculados EXACTOS
        int indiceColumna = 0;
        for (int g = 0; g < gruposPadre.length; g++) {
            int anchoGrupo = 0;
            
            // Sumar anchos REALES de las columnas hijas de este grupo
            for (int c = 0; c < columnasEnGrupo[g]; c++) {
                if (indiceColumna + c < anchosReales.length) {
                    anchoGrupo += anchosReales[indiceColumna + c];
                }
            }
            
            // Debug para verificar cálculos de grupos (comentado - descomentar si hay problemas)
            // System.out.println("Grupo " + gruposPadre[g] + " (cols " + indiceColumna + "-" + (indiceColumna + columnasEnGrupo[g] - 1) + "): " + anchoGrupo + "px");
            
            panelPadre.add(crearLabelPadre(gruposPadre[g], coloresPadre[g], anchoGrupo));
            indiceColumna += columnasEnGrupo[g];
        }
        
        // Agregar grupos de barcos dinámicos
        for (int i = 1; i <= maxBarcosEnSistema; i++) {
            int anchoBarco = 0;
            // Cada barco tiene 3 columnas: ID, Estado, T. Ingreso
            for (int c = 0; c < 3; c++) {
                if (indiceColumna + c < anchosReales.length) {
                    anchoBarco += anchosReales[indiceColumna + c];
                }
            }
            
            // Debug para barcos (comentado - descomentar si hay problemas)
            // if (i <= 3 || i > maxBarcosEnSistema - 3) {
            //     System.out.println("Barco B" + i + " (cols " + indiceColumna + "-" + (indiceColumna + 2) + "): " + anchoBarco + "px");
            // }
            
            Color colorBarco = new Color(255, Math.max(140, 218 - (i * 15)), Math.max(100, 185 - (i * 10)));
            panelPadre.add(crearLabelPadre("B" + i, colorBarco, anchoBarco));
            indiceColumna += 3;
        }
        
        // Crear encabezados hijo con anchos exactos de la tabla
        indiceColumna = 0;
        
        // Columnas base
        for (int i = 0; i < columnasHijas.length && indiceColumna < anchosReales.length; i++) {
            Color colorHijo = obtenerColorHijo(i);
            panelHijo.add(crearLabelHijo(columnasHijas[i], colorHijo, anchosReales[indiceColumna]));
            indiceColumna++;
        }
        
        // Columnas dinámicas de barcos
        for (int i = 1; i <= maxBarcosEnSistema; i++) {
            Color colorBarco = new Color(255, Math.max(140, 218 - (i * 15)), Math.max(100, 185 - (i * 10)));
            
            // ID
            if (indiceColumna < anchosReales.length) {
                panelHijo.add(crearLabelHijo("ID", colorBarco, anchosReales[indiceColumna++]));
            }
            // Estado
            if (indiceColumna < anchosReales.length) {
                panelHijo.add(crearLabelHijo("Estado", colorBarco, anchosReales[indiceColumna++]));
            }
            // T. Ingreso
            if (indiceColumna < anchosReales.length) {
                panelHijo.add(crearLabelHijo("T. Ingreso", colorBarco, anchosReales[indiceColumna++]));
            }
        }
    }
    
    /**
     * Obtiene el color correspondiente para una columna hija base
     */
    private Color obtenerColorHijo(int indice) {
        Color[] colores = {
            // Control (3)
            new Color(240, 240, 240), new Color(240, 240, 240), new Color(240, 240, 240),
            // Llegada (2)
            new Color(255, 228, 196), new Color(255, 228, 196),
            // Descarga M1 (3)
            new Color(176, 196, 222), new Color(176, 196, 222), new Color(176, 196, 222),
            // Descarga M2 (3)
            new Color(135, 155, 180), new Color(135, 155, 180), new Color(135, 155, 180),
            // Bahía (1)
            new Color(221, 160, 221),
            // Estados Muelles (4)
            new Color(152, 251, 152), new Color(152, 251, 152), new Color(152, 251, 152), new Color(152, 251, 152),
            // Estados Grúas (4)
            new Color(255, 182, 193), new Color(255, 182, 193), new Color(255, 182, 193), new Color(255, 182, 193),
            // T. Permanencia (5)
            new Color(255, 255, 224), new Color(255, 255, 224), new Color(255, 255, 224), new Color(255, 255, 224), new Color(255, 255, 224),
            // Utilización Muelle (4)
            new Color(135, 206, 250), new Color(135, 206, 250), new Color(135, 206, 250), new Color(135, 206, 250),
            // Utilización Grúa (4)
            new Color(165, 180, 70), new Color(165, 180, 70), new Color(165, 180, 70), new Color(165, 180, 70),
            // Sistema (1)
            new Color(255, 218, 185)
        };
        
        return indice < colores.length ? colores[indice] : new Color(200, 200, 200);
    }
}