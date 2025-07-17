package com.facu.simulation.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Renderer personalizado para crear encabezados de tabla con múltiples filas
 * y agrupaciones visuales como en la imagen de referencia.
 */
public class EncabezadoPersonalizado extends DefaultTableCellRenderer implements TableCellRenderer {
    
    // Estructura para definir encabezados jerárquicos
    public static class GrupoEncabezado {
        private String titulo;
        private List<String> subencabezados;
        private Color colorFondo;
        private Color colorTexto;
        
        public GrupoEncabezado(String titulo, List<String> subencabezados, Color colorFondo, Color colorTexto) {
            this.titulo = titulo;
            this.subencabezados = new ArrayList<>(subencabezados);
            this.colorFondo = colorFondo;
            this.colorTexto = colorTexto;
        }
        
        // Getters
        public String getTitulo() { return titulo; }
        public List<String> getSubencabezados() { return subencabezados; }
        public Color getColorFondo() { return colorFondo; }
        public Color getColorTexto() { return colorTexto; }
    }
    
    private List<GrupoEncabezado> grupos;
    private JTable tabla;
    
    public EncabezadoPersonalizado(JTable tabla) {
        this.tabla = tabla;
        this.grupos = new ArrayList<>();
        configurarRenderer();
    }
    
    private void configurarRenderer() {
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
        setOpaque(true);
        setBorder(BorderFactory.createRaisedBevelBorder());
    }
    
    /**
     * Configura los grupos de encabezados según la imagen de referencia
     */
    public void configurarGrupos(int cantidadMuelles, int cantidadGruas) {
        grupos.clear();
        
        // Grupo 1: Eventos y Control
        List<String> eventosControl = List.of("Evento", "Reloj");
        grupos.add(new GrupoEncabezado("Control", eventosControl, 
            new Color(240, 240, 240), Color.BLACK));
        
        // Grupo 2: Llegada de Barcos
        List<String> llegadaBarco = List.of("RND", "Prox_Llegada");
        grupos.add(new GrupoEncabezado("Llegada_Barco", llegadaBarco, 
            new Color(255, 228, 196), Color.BLACK));
        
        // Grupo 3: Descarga por Muelle
        for (int i = 1; i <= cantidadMuelles; i++) {
            List<String> descargaMuelle = List.of("RND", "Fin_descarga");
            grupos.add(new GrupoEncabezado("DescargaMuelle" + i, descargaMuelle, 
                new Color(176, 196, 222), Color.BLACK));
        }
        
        // Grupo 4: Bahía
        List<String> bahia = List.of("Cola");
        grupos.add(new GrupoEncabezado("Bahía", bahia, 
            new Color(221, 160, 221), Color.BLACK));
        
        // Grupo 5: Estados de Muelles
        for (int i = 1; i <= cantidadMuelles; i++) {
            List<String> estadoMuelle = List.of("Estado", "Ocupado", "Inicio", "Fin");
            grupos.add(new GrupoEncabezado("Muelle" + i, estadoMuelle, 
                new Color(152, 251, 152), Color.BLACK));
        }
        
        // Grupo 6: Estados de Grúas
        for (int i = 1; i <= cantidadGruas; i++) {
            List<String> estadoGrua = List.of("Estado", "Barco");
            grupos.add(new GrupoEncabezado("Grúa" + i, estadoGrua, 
                new Color(255, 182, 193), Color.BLACK));
        }
        
        // Grupo 7: Estadísticas
        List<String> estadisticas = List.of("Ac.Permanencia", "Barcos");
        grupos.add(new GrupoEncabezado("Estadísticas", estadisticas, 
            new Color(255, 255, 224), Color.BLACK));
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        // Configurar el componente base
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        // Encontrar a qué grupo pertenece esta columna
        int columnaActual = 0;
        for (GrupoEncabezado grupo : grupos) {
            if (column >= columnaActual && column < columnaActual + grupo.getSubencabezados().size()) {
                // Esta columna pertenece a este grupo
                setBackground(grupo.getColorFondo());
                setForeground(grupo.getColorTexto());
                
                // Determinar si mostrar título del grupo o subencabezado
                int indexEnGrupo = column - columnaActual;
                if (indexEnGrupo == 0 && grupo.getSubencabezados().size() > 1) {
                    // Primera columna del grupo: mostrar título del grupo
                    setText(grupo.getTitulo());
                } else {
                    // Mostrar subencabezado correspondiente
                    setText(grupo.getSubencabezados().get(indexEnGrupo));
                }
                
                break;
            }
            columnaActual += grupo.getSubencabezados().size();
        }
        
        return this;
    }
    
    /**
     * Genera los nombres de columnas para el modelo de tabla
     */
    public static String[] generarNombresColumnas(int cantidadMuelles, int cantidadGruas) {
        List<String> nombres = new ArrayList<>();
        
        // Control
        nombres.add("Evento");
        nombres.add("Reloj");
        
        // Llegada
        nombres.add("RND_Llegada");
        nombres.add("Prox_Llegada");
        
        // Descarga por muelle
        for (int i = 1; i <= cantidadMuelles; i++) {
            nombres.add("RND_Desc_M" + i);
            nombres.add("Fin_Desc_M" + i);
        }
        
        // Bahía
        nombres.add("Cola");
        
        // Estados muelles
        for (int i = 1; i <= cantidadMuelles; i++) {
            nombres.add("Estado_M" + i);
            nombres.add("Ocupado_M" + i);
            nombres.add("Inicio_M" + i);
            nombres.add("Fin_M" + i);
        }
        
        // Estados grúas
        for (int i = 1; i <= cantidadGruas; i++) {
            nombres.add("Estado_G" + i);
            nombres.add("Barco_G" + i);
        }
        
        // Estadísticas
        nombres.add("Ac_Permanencia");
        nombres.add("Barcos");
        
        return nombres.toArray(new String[0]);
    }
}
