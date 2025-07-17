package com.facu.simulation.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Versión simplificada de tabla con encabezados mejorados
 * que simula encabezados jerárquicos usando colores y texto.
 */
public class TablaMejorada extends JPanel {
    
    private JTable tabla;
    private DefaultTableModel modelo;
    private JScrollPane scrollPane;
    
    public TablaMejorada(int cantidadMuelles, int cantidadGruas) {
        setLayout(new BorderLayout());
        crearTabla(cantidadMuelles, cantidadGruas);
        configurarEncabezados();
        
        scrollPane = new JScrollPane(tabla);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Crea la tabla con columnas mejoradas
     */
    private void crearTabla(int cantidadMuelles, int cantidadGruas) {
        String[] columnas = generarColumnasmejoradas(cantidadMuelles, cantidadGruas);
        
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabla = new JTable(modelo);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabla.setRowHeight(25);
        tabla.getTableHeader().setReorderingAllowed(false);
        
        // Estilo de la tabla
        tabla.setGridColor(Color.GRAY);
        tabla.setShowGrid(true);
        tabla.setIntercellSpacing(new Dimension(1, 1));
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    /**
     * Configura los encabezados con colores y estilos
     */
    private void configurarEncabezados() {
        JTableHeader header = tabla.getTableHeader();
        header.setDefaultRenderer(new EncabezadoMejoradoRenderer());
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
    }
    
    /**
     * Genera nombres de columnas mejorados con indicadores de grupo
     */
    private String[] generarColumnasmejoradas(int cantidadMuelles, int cantidadGruas) {
        List<String> columnas = new ArrayList<>();
        
        // Grupo Control
        columnas.add("CTRL | Evento");
        columnas.add("CTRL | Reloj");
        
        // Grupo Llegada
        columnas.add("LLEGADA | RND");
        columnas.add("LLEGADA | Prox");
        
        // Grupo Descarga por Muelle
        for (int i = 1; i <= cantidadMuelles; i++) {
            columnas.add("DESC-M" + i + " | RND");
            columnas.add("DESC-M" + i + " | Fin");
        }
        
        // Grupo Bahía
        columnas.add("BAHÍA | Cola");
        
        // Grupo Estados Muelles
        for (int i = 1; i <= cantidadMuelles; i++) {
            columnas.add("M" + i + " | Estado");
            columnas.add("M" + i + " | Ocupado");
            columnas.add("M" + i + " | Inicio");
            columnas.add("M" + i + " | Fin");
        }
        
        // Grupo Estados Grúas
        for (int i = 1; i <= cantidadGruas; i++) {
            columnas.add("G" + i + " | Estado");
            columnas.add("G" + i + " | Barco");
        }
        
        // Grupo Estadísticas
        columnas.add("STATS | Ac.Perm");
        columnas.add("STATS | Barcos");
        
        return columnas.toArray(new String[0]);
    }
    
    /**
     * Renderer personalizado para encabezados con colores por grupo
     */
    private class EncabezadoMejoradoRenderer extends JLabel implements TableCellRenderer {
        
        public EncabezadoMejoradoRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
            setBorder(BorderFactory.createRaisedBevelBorder());
            setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            String texto = value.toString();
            
            // Determinar color según el grupo
            Color colorFondo = determinarColorGrupo(texto);
            Color colorTexto = Color.BLACK;
            
            setBackground(colorFondo);
            setForeground(colorTexto);
            
            // Formatear el texto para mostrar en múltiples líneas
            String textoFormateado = formatearTextoEncabezado(texto);
            setText(textoFormateado);
            
            return this;
        }
        
        private Color determinarColorGrupo(String texto) {
            if (texto.startsWith("CTRL")) {
                return new Color(240, 240, 240); // Gris claro
            } else if (texto.startsWith("LLEGADA")) {
                return new Color(255, 228, 196); // Naranja claro
            } else if (texto.startsWith("DESC-")) {
                return new Color(176, 196, 222); // Azul claro
            } else if (texto.startsWith("BAHÍA")) {
                return new Color(221, 160, 221); // Violeta claro
            } else if (texto.startsWith("M")) {
                return new Color(152, 251, 152); // Verde claro
            } else if (texto.startsWith("G")) {
                return new Color(255, 182, 193); // Rosa claro
            } else if (texto.startsWith("STATS")) {
                return new Color(255, 255, 224); // Amarillo claro
            }
            return Color.WHITE;
        }
        
        private String formatearTextoEncabezado(String texto) {
            // Convertir "GRUPO | Subencabezado" a HTML con múltiples líneas
            if (texto.contains(" | ")) {
                String[] partes = texto.split(" \\| ");
                return "<html><center><b>" + partes[0] + "</b><br>" + partes[1] + "</center></html>";
            }
            return "<html><center>" + texto + "</center></html>";
        }
    }
    
    /**
     * Ajusta el ancho de las columnas automáticamente
     */
    public void ajustarAnchoColumnas() {
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            TableColumn column = tabla.getColumnModel().getColumn(i);
            
            // Obtener ancho preferido basado en el contenido
            int anchoPreferido = obtenerAnchoPreferido(column, i);
            
            // Establecer ancho mínimo y preferido
            column.setMinWidth(60);
            column.setPreferredWidth(anchoPreferido);
            column.setMaxWidth(150);
        }
    }
    
    private int obtenerAnchoPreferido(TableColumn column, int columnIndex) {
        int ancho = 0;
        
        // Verificar ancho del encabezado
        TableCellRenderer headerRenderer = column.getHeaderRenderer();
        if (headerRenderer == null) {
            headerRenderer = tabla.getTableHeader().getDefaultRenderer();
        }
        
        Component headerComponent = headerRenderer.getTableCellRendererComponent(
            tabla, column.getHeaderValue(), false, false, -1, columnIndex);
        ancho = Math.max(ancho, headerComponent.getPreferredSize().width);
        
        // Verificar ancho del contenido (primeras 5 filas)
        int filasAVerificar = Math.min(5, tabla.getRowCount());
        for (int row = 0; row < filasAVerificar; row++) {
            TableCellRenderer renderer = tabla.getCellRenderer(row, columnIndex);
            Component component = renderer.getTableCellRendererComponent(
                tabla, tabla.getValueAt(row, columnIndex), false, false, row, columnIndex);
            ancho = Math.max(ancho, component.getPreferredSize().width);
        }
        
        return ancho + 10; // Agregar padding
    }
    
    // Métodos públicos para acceso externo
    public JTable getTabla() {
        return tabla;
    }
    
    public DefaultTableModel getModelo() {
        return modelo;
    }
    
    public JScrollPane getScrollPane() {
        return scrollPane;
    }
    
    /**
     * Actualiza la tabla después de agregar nuevos datos
     */
    public void actualizarVista() {
        SwingUtilities.invokeLater(() -> {
            modelo.fireTableDataChanged();
            ajustarAnchoColumnas();
            tabla.revalidate();
            tabla.repaint();
        });
    }
}
