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
 * Tabla mejorada con encabezados jerárquicos reales integrados.
 * Mantiene toda la funcionalidad original pero con encabezados jerárquicos verdaderos.
 */
public class TablaMejorada extends JPanel {
    
    private JTable tabla;
    private DefaultTableModel modelo;
    private JScrollPane scrollPane;
    private GeneradorColumnasTabla generadorColumnas;
    private EncabezadosJerarquicos encabezadosJerarquicos;
    
    public TablaMejorada(int cantidadMuelles, int cantidadGruas) {
        setLayout(new BorderLayout());
        generadorColumnas = new GeneradorColumnasTabla();
        crearTablaConEncabezadosJerarquicos(cantidadMuelles, cantidadGruas);
    }
    
    /**
     * Crea la tabla con encabezados jerárquicos reales integrados
     */
    private void crearTablaConEncabezadosJerarquicos(int cantidadMuelles, int cantidadGruas) {
        // Crear panel que contenga tanto encabezados jerárquicos como tabla
        JPanel panelCompleto = new JPanel(new BorderLayout());
        panelCompleto.setBackground(new Color(25, 25, 25));
        
        // 1. CREAR ENCABEZADOS JERÁRQUICOS REALES
        encabezadosJerarquicos = new EncabezadosJerarquicos();
        panelCompleto.add(encabezadosJerarquicos, BorderLayout.NORTH);
        
        // 2. CREAR TABLA SIN ENCABEZADO NORMAL (usamos el jerárquico)
        String[] columnasBase = generadorColumnas.generarEncabezados(0);
        
        modelo = new DefaultTableModel(columnasBase, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabla = new JTable(modelo);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabla.setRowHeight(28);
        tabla.setTableHeader(null); // IMPORTANTE: Sin encabezado normal, usamos el jerárquico
        
        // Aplicar estilo oscuro consistente
        tabla.setBackground(new Color(50, 50, 50));
        tabla.setForeground(new Color(240, 240, 240));
        tabla.setGridColor(new Color(100, 100, 100));
        tabla.setSelectionBackground(new Color(100, 149, 237));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setShowGrid(true);
        tabla.setIntercellSpacing(new Dimension(1, 1));
        tabla.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Configurar renderer personalizado para colorear celdas por grupos
        configurarRendererColoreado();
        
        // 3. AGREGAR TABLA AL PANEL COMPLETO
        panelCompleto.add(tabla, BorderLayout.CENTER);
        
        // 4. CREAR SCROLL PANE CON TODO EL CONTENIDO
        scrollPane = new JScrollPane(panelCompleto);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(1200, 720));
        scrollPane.setBackground(new Color(50, 50, 50));
        scrollPane.getViewport().setBackground(new Color(50, 50, 50));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Configura un renderer personalizado para colorear las celdas de datos
     * basándose en los colores de los encabezados padre pero más suaves
     */
    private void configurarRendererColoreado() {
        tabla.setDefaultRenderer(Object.class, new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                // Crear label para la celda
                JLabel label = new JLabel(value != null ? value.toString() : "");
                label.setOpaque(true);
                // Usar fuente bold para dar más volumen
                Font fuenteOriginal = table.getFont();
                label.setFont(new Font(fuenteOriginal.getName(), Font.BOLD, fuenteOriginal.getSize()));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                
                // Determinar color de fondo basado en la columna
                Color colorFondo = obtenerColorSuavePorColumna(column);
                
                if (isSelected) {
                    label.setBackground(table.getSelectionBackground());
                    label.setForeground(table.getSelectionForeground());
                } else {
                    label.setBackground(colorFondo);
                    label.setForeground(new Color(240, 240, 240)); // Texto claro sobre fondo oscuro
                }
                
                // Borde sutil
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, 
                    new Color(100, 100, 100)));
                
                return label;
            }
        });
    }
    
    /**
     * Obtiene un color suave para una columna basado en los grupos de encabezados
     */
    private Color obtenerColorSuavePorColumna(int columna) {
        // Definir rangos de columnas y sus colores suaves
        // Basado en la estructura de EncabezadosJerarquicos
        
        if (columna >= 0 && columna <= 2) {
            // CONTROL (3): Fila, Evento, Reloj - Gris oscuro suave
            return new Color(70, 70, 70);
        } else if (columna >= 3 && columna <= 4) {
            // LLEGADA BARCO (2) - Beige oscuro suave
            return new Color(80, 75, 65);
        } else if (columna >= 5 && columna <= 7) {
            // DESCARGA MUELLE 1 (3) - Azul oscuro suave
            return new Color(65, 70, 80);
        } else if (columna >= 8 && columna <= 10) {
            // DESCARGA MUELLE 2 (3) - Azul más oscuro suave
            return new Color(60, 65, 75);
        } else if (columna == 11) {
            // BAHÍA (1) - Rosa oscuro suave
            return new Color(75, 65, 75);
        } else if (columna >= 12 && columna <= 15) {
            // ESTADOS MUELLES (4) - Verde oscuro suave
            return new Color(65, 80, 65);
        } else if (columna >= 16 && columna <= 19) {
            // ESTADOS GRÚAS (4) - Rosa oscuro suave
            return new Color(80, 70, 75);
        } else if (columna >= 20 && columna <= 24) {
            // T. PERMANENCIA (5) - Amarillo oscuro suave
            return new Color(80, 80, 65);
        } else if (columna >= 25 && columna <= 28) {
            // UTILIZACIÓN MUELLE (4) - Azul claro oscuro suave
            return new Color(65, 75, 85);
        } else if (columna >= 29 && columna <= 32) {
            // UTILIZACIÓN GRÚA (4) - Verde oliva oscuro suave
            return new Color(75, 80, 65);
        } else if (columna == 33) {
            // BARCOS SISTEMA (1) - Naranja oscuro suave
            return new Color(85, 75, 65);
        } else {
            // BARCOS DINÁMICOS - Variaciones oscuras de naranja
            int indiceBanco = (columna - 34) / 3 + 1;
            int base = Math.max(60, 85 - (indiceBanco * 2));
            return new Color(base + 5, base, base - 5);
        }
    }
    
    // Métodos auxiliares removidos - ahora usamos encabezados jerárquicos reales
    
    /**
     * Actualiza las columnas dinámicamente cuando hay más barcos
     * NUEVA VERSIÓN: También actualiza los encabezados jerárquicos
     */
    public void actualizarColumnasSegunBarcos(int maxBarcosEnSistema) {
        String[] nuevasColumnas = generadorColumnas.generarEncabezados(maxBarcosEnSistema);
        
        // Solo actualizar si hay cambios en el número de columnas
        if (modelo.getColumnCount() != nuevasColumnas.length) {
            // Actualizar modelo de tabla (SIN formato de colores, usamos columnas normales)
            modelo.setColumnIdentifiers(nuevasColumnas);
            
            // IMPORTANTE: Actualizar encabezados jerárquicos
            if (encabezadosJerarquicos != null) {
                encabezadosJerarquicos.actualizarParaBarcos(maxBarcosEnSistema);
            }
            
            ajustarAnchoColumnas();
            
            // IMPORTANTE: Sincronizar encabezados jerárquicos con anchos reales
            if (encabezadosJerarquicos != null) {
                encabezadosJerarquicos.sincronizarConTabla(tabla);
            }
        }
    }
    
    // Renderer antiguo removido - ahora usamos EncabezadosJerarquicos
    
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
        int ancho = 60; // Ancho mínimo base
        
        // NOTA: No podemos usar el header normal porque es null (usamos encabezados jerárquicos)
        // En su lugar, usamos anchos predefinidos según el índice de columna
        
        // Verificar ancho del contenido (primeras 5 filas) si hay datos
        int filasAVerificar = Math.min(5, tabla.getRowCount());
        for (int row = 0; row < filasAVerificar; row++) {
            try {
                TableCellRenderer renderer = tabla.getCellRenderer(row, columnIndex);
                Component component = renderer.getTableCellRendererComponent(
                    tabla, tabla.getValueAt(row, columnIndex), false, false, row, columnIndex);
                ancho = Math.max(ancho, component.getPreferredSize().width);
            } catch (Exception e) {
                // Si hay error, usar ancho base
                break;
            }
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
     * NUEVA VERSIÓN: También actualiza los encabezados jerárquicos
     */
    public void actualizarVista() {
        SwingUtilities.invokeLater(() -> {
            modelo.fireTableDataChanged();
            ajustarAnchoColumnas();
            tabla.revalidate();
            tabla.repaint();
            
            // IMPORTANTE: También actualizar encabezados jerárquicos
            if (encabezadosJerarquicos != null) {
                // Esperar a que la tabla se actualice completamente antes de sincronizar
                SwingUtilities.invokeLater(() -> {
                    encabezadosJerarquicos.sincronizarConTabla(tabla);
                    encabezadosJerarquicos.revalidate();
                    encabezadosJerarquicos.repaint();
                });
            }
        });
    }
}
