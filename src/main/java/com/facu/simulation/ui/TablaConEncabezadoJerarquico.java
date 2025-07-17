package com.facu.simulation.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Componente personalizado para crear una tabla con encabezados jerárquicos
 * de múltiples filas como en la imagen de referencia.
 */
public class TablaConEncabezadoJerarquico extends JPanel {
    
    private JTable tabla;
    private DefaultTableModel modelo;
    private JPanel panelEncabezados;
    private JScrollPane scrollPane;
    
    public TablaConEncabezadoJerarquico(int cantidadMuelles, int cantidadGruas) {
        setLayout(new BorderLayout());
        crearEncabezadoJerarquico(cantidadMuelles, cantidadGruas);
        crearTabla(cantidadMuelles, cantidadGruas);
        
        add(panelEncabezados, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Crea el panel de encabezados jerárquicos con múltiples filas
     */
    private void crearEncabezadoJerarquico(int cantidadMuelles, int cantidadGruas) {
        panelEncabezados = new JPanel();
        panelEncabezados.setLayout(new BoxLayout(panelEncabezados, BoxLayout.Y_AXIS));
        panelEncabezados.setBackground(Color.WHITE);
        
        // Fila 1: Encabezados principales (grupos)
        JPanel filaGrupos = crearFilaGrupos(cantidadMuelles, cantidadGruas);
        panelEncabezados.add(filaGrupos);
        
        // Fila 2: Subencabezados
        JPanel filaSubencabezados = crearFilaSubencabezados(cantidadMuelles, cantidadGruas);
        panelEncabezados.add(filaSubencabezados);
        
        // Línea separadora
        panelEncabezados.add(new JSeparator());
    }
    
    /**
     * Crea la fila de grupos principales
     */
    private JPanel crearFilaGrupos(int cantidadMuelles, int cantidadGruas) {
        JPanel fila = new JPanel(new GridBagLayout());
        fila.setBackground(Color.WHITE);
        fila.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, Color.GRAY));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.gridy = 0;
        gbc.gridx = 0;
        
        // Definir estructura de grupos
        List<GrupoEncabezado> grupos = List.of(
            new GrupoEncabezado("Control", 2, new Color(240, 240, 240)),
            new GrupoEncabezado("Llegada_Barco", 2, new Color(255, 228, 196)),
            new GrupoEncabezado("DescargaBarco", cantidadMuelles * 2, new Color(176, 196, 222)),
            new GrupoEncabezado("Bahía", 1, new Color(221, 160, 221)),
            new GrupoEncabezado("Estados Muelles", cantidadMuelles * 4, new Color(152, 251, 152)),
            new GrupoEncabezado("Estados Grúas", cantidadGruas * 2, new Color(255, 182, 193)),
            new GrupoEncabezado("Estadísticas", 2, new Color(255, 255, 224))
        );
        
        for (GrupoEncabezado grupo : grupos) {
            gbc.gridwidth = grupo.ancho;
            gbc.weightx = grupo.ancho;
            
            JLabel label = crearLabelGrupo(grupo.nombre, grupo.color);
            fila.add(label, gbc);
            
            gbc.gridx += grupo.ancho;
        }
        
        return fila;
    }
    
    /**
     * Crea la fila de subencabezados
     */
    private JPanel crearFilaSubencabezados(int cantidadMuelles, int cantidadGruas) {
        JPanel fila = new JPanel(new GridBagLayout());
        fila.setBackground(Color.WHITE);
        fila.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.GRAY));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        gbc.gridy = 0;
        gbc.gridx = 0;
        
        // Subencabezados para cada grupo
        String[] subencabezados = generarSubencabezados(cantidadMuelles, cantidadGruas);
        Color[] colores = generarColoresSubencabezados(cantidadMuelles, cantidadGruas);
        
        for (int i = 0; i < subencabezados.length; i++) {
            JLabel label = crearLabelSubencabezado(subencabezados[i], colores[i]);
            fila.add(label, gbc);
            gbc.gridx++;
        }
        
        return fila;
    }
    
    /**
     * Genera los nombres de subencabezados
     */
    private String[] generarSubencabezados(int cantidadMuelles, int cantidadGruas) {
        List<String> subs = new ArrayList<>();
        
        // Control
        subs.add("Evento");
        subs.add("Reloj");
        
        // Llegada
        subs.add("RND");
        subs.add("Prox_Llegada");
        
        // Descarga por muelle
        for (int i = 1; i <= cantidadMuelles; i++) {
            subs.add("RND");
            subs.add("Fin_descarga");
        }
        
        // Bahía
        subs.add("Cola");
        
        // Estados muelles
        for (int i = 1; i <= cantidadMuelles; i++) {
            subs.add("Estado");
            subs.add("Ocupado");
            subs.add("Inicio");
            subs.add("Fin");
        }
        
        // Estados grúas
        for (int i = 1; i <= cantidadGruas; i++) {
            subs.add("Estado");
            subs.add("Barco");
        }
        
        // Estadísticas
        subs.add("Ac.Permanencia");
        subs.add("Barcos");
        
        return subs.toArray(new String[0]);
    }
    
    /**
     * Genera los colores para subencabezados
     */
    private Color[] generarColoresSubencabezados(int cantidadMuelles, int cantidadGruas) {
        List<Color> colores = new ArrayList<>();
        
        // Control (2 columnas)
        for (int i = 0; i < 2; i++) {
            colores.add(new Color(240, 240, 240));
        }
        
        // Llegada (2 columnas)
        for (int i = 0; i < 2; i++) {
            colores.add(new Color(255, 228, 196));
        }
        
        // Descarga (2 * cantidadMuelles columnas)
        for (int i = 0; i < cantidadMuelles * 2; i++) {
            colores.add(new Color(176, 196, 222));
        }
        
        // Bahía (1 columna)
        colores.add(new Color(221, 160, 221));
        
        // Estados muelles (4 * cantidadMuelles columnas)
        for (int i = 0; i < cantidadMuelles * 4; i++) {
            colores.add(new Color(152, 251, 152));
        }
        
        // Estados grúas (2 * cantidadGruas columnas)
        for (int i = 0; i < cantidadGruas * 2; i++) {
            colores.add(new Color(255, 182, 193));
        }
        
        // Estadísticas (2 columnas)
        for (int i = 0; i < 2; i++) {
            colores.add(new Color(255, 255, 224));
        }
        
        return colores.toArray(new Color[0]);
    }
    
    /**
     * Crea un label para grupo principal
     */
    private JLabel crearLabelGrupo(String texto, Color color) {
        JLabel label = new JLabel(texto, JLabel.CENTER);
        label.setBackground(color);
        label.setOpaque(true);
        label.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        return label;
    }
    
    /**
     * Crea un label para subencabezado
     */
    private JLabel crearLabelSubencabezado(String texto, Color color) {
        JLabel label = new JLabel(texto, JLabel.CENTER);
        label.setBackground(color);
        label.setOpaque(true);
        label.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        return label;
    }
    
    /**
     * Crea la tabla principal
     */
    private void crearTabla(int cantidadMuelles, int cantidadGruas) {
        String[] columnas = generarSubencabezados(cantidadMuelles, cantidadGruas);
        
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabla = new JTable(modelo);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabla.setRowHeight(25);
        tabla.getTableHeader().setVisible(false); // Ocultar encabezado original
        
        // Estilo de la tabla
        tabla.setGridColor(Color.GRAY);
        tabla.setShowGrid(true);
        tabla.setIntercellSpacing(new Dimension(1, 1));
        
        scrollPane = new JScrollPane(tabla);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }
    
    /**
     * Clase auxiliar para definir grupos de encabezados
     */
    private static class GrupoEncabezado {
        final String nombre;
        final int ancho;
        final Color color;
        
        GrupoEncabezado(String nombre, int ancho, Color color) {
            this.nombre = nombre;
            this.ancho = ancho;
            this.color = color;
        }
    }
    
    // Métodos públicos para acceder a la tabla
    public JTable getTabla() {
        return tabla;
    }
    
    public DefaultTableModel getModelo() {
        return modelo;
    }
}
