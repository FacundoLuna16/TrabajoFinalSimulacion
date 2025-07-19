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
    private GeneradorColumnasTabla generadorColumnas;
    
    public TablaMejorada(int cantidadMuelles, int cantidadGruas) {
        setLayout(new BorderLayout());
        generadorColumnas = new GeneradorColumnasTabla();
        crearTabla(cantidadMuelles, cantidadGruas);
        configurarEncabezados();
        
        scrollPane = new JScrollPane(tabla);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Crea la tabla con columnas mejoradas usando el generador
     */
    private void crearTabla(int cantidadMuelles, int cantidadGruas) {
        // Usar el generador para obtener columnas base iniciales (sin barcos dinámicos)
        String[] columnasBase = generadorColumnas.generarEncabezados(0);
        String[] columnasConFormato = convertirAFormatoConColores(columnasBase);
        
        modelo = new DefaultTableModel(columnasConFormato, 0) {
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
     * Convierte las columnas base a formato con colores e indicadores de grupo
     */
    private String[] convertirAFormatoConColores(String[] columnasBase) {
        List<String> columnasConFormato = new ArrayList<>();
        
        for (String columna : columnasBase) {
            String columnaFormateada = convertirColumnaAFormatoConColor(columna);
            columnasConFormato.add(columnaFormateada);
        }
        
        return columnasConFormato.toArray(new String[0]);
    }
    
    /**
     * Convierte una columna individual al formato con colores
     */
    private String convertirColumnaAFormatoConColor(String columna) {
        // Mapeo de columnas originales a formato con grupos
        switch (columna) {
            case "Fila": return "CTRL | Fila";
            case "Evento": return "CTRL | Evento";
            case "Reloj": return "CTRL | Reloj";
            case "RNDLleg": return "LLEGADA | RNDLleg";
            case "ProxLleg": return "LLEGADA | ProxLleg";
            case "RNDM1": return "DESCARGA | RNDM1";
            case "TiemRest1": return "DESCARGA | TiemRest1";  // NUEVO
            case "FinDescM1": return "DESCARGA | FinDescM1";
            case "RNDM2": return "DESCARGA | RNDM2";
            case "TiemRest2": return "DESCARGA | TiemRest2";  // NUEVO
            case "FinDescM2": return "DESCARGA | FinDescM2";
            case "Bahía": return "BAHÍA | Cola";
            case "M1Est": return "M1 | Estado";
            case "M1Inic": return "M1 | Inicio";
            case "M2Est": return "M2 | Estado";
            case "M2Inic": return "M2 | Inicio";
            case "G1Est": return "G1 | Estado";
            case "G1Inic": return "G1 | Inicio";
            case "G2Est": return "G2 | Estado";
            case "G2Inic": return "G2 | Inicio";
            case "MaxTPer": return "TPERM | MaxTPer";
            case "MinTPer": return "TPERM | MinTPer";
            case "AcTPer": return "TPERM | AcTPer";
            case "CantB": return "TPERM | CantB";
            case "MedTPer": return "TPERM | MedTPer";
            case "M1AcTOc": return "UTIL | M1AcTOc";
            case "M1Ut%": return "UTIL | M1Ut%";
            case "M2AcTOc": return "UTIL | M2AcTOc";
            case "M2Ut%": return "UTIL | M2Ut%";
            case "G1AcTOc": return "GUTIL | G1AcTOc";
            case "G1Ut%": return "GUTIL | G1Ut%";
            case "G2AcTOc": return "GUTIL | G2AcTOc";
            case "G2Ut%": return "GUTIL | G2Ut%";
            case "BSist": return "BARCOS | BSist";
            default:
                // Para columnas dinámicas de barcos
                if (columna.matches("B\\d+_ID")) {
                    return "BARCOS | " + columna;
                } else if (columna.matches("B\\d+_Estado")) {
                    return "BARCOS | " + columna;
                } else if (columna.matches("B\\d+_Ingr")) {
                    return "BARCOS | " + columna;
                }
                return columna;
        }
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
     * Actualiza las columnas dinámicamente cuando hay más barcos
     */
    public void actualizarColumnasSegunBarcos(int maxBarcosEnSistema) {
        String[] nuevasColumnas = generadorColumnas.generarEncabezados(maxBarcosEnSistema);
        String[] columnasFormateadas = convertirAFormatoConColores(nuevasColumnas);
        
        // Solo actualizar si hay cambios en el número de columnas
        if (modelo.getColumnCount() != columnasFormateadas.length) {
            modelo.setColumnIdentifiers(columnasFormateadas);
            configurarEncabezados();
            ajustarAnchoColumnas();
        }
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
            } else if (texto.startsWith("DESCARGA")) {
                return new Color(176, 196, 222); // Azul claro
            } else if (texto.startsWith("BAHÍA")) {
                return new Color(221, 160, 221); // Violeta claro
            } else if (texto.startsWith("M1") || texto.startsWith("M2")) {
                return new Color(152, 251, 152); // Verde claro
            } else if (texto.startsWith("G1") || texto.startsWith("G2")) {
                return new Color(255, 182, 193); // Rosa claro
            } else if (texto.startsWith("TPERM")) {
                return new Color(255, 255, 224); // Amarillo claro
            } else if (texto.startsWith("UTIL")) {
                return new Color(173, 216, 230); // Azul muy claro
            } else if (texto.startsWith("GUTIL")) {
                return new Color(218, 173, 230);
            } else if (texto.startsWith("BARCOS")) {
                return new Color(255, 218, 185); // Melocotón claro
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
