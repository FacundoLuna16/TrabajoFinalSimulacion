import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Test simple para verificar que funciona el encabezado jerárquico
 */
public class TestTablaJerarquica {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            JFrame frame = new JFrame("Test Tabla Jerárquica");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1400, 800);
            frame.setLocationRelativeTo(null);
            
            // Crear tabla de prueba
            TablaTest tabla = new TablaTest();
            frame.add(tabla, BorderLayout.CENTER);
            
            frame.setVisible(true);
        });
    }
}

/**
 * Panel de prueba con tabla jerárquica
 */
class TablaTest extends JPanel {
    
    public TablaTest() {
        setLayout(new BorderLayout());
        setBackground(new Color(25, 25, 25));
        
        // Crear datos de prueba
        String[] columnas = {
            "Fila", "Evento", "Reloj", "RND", "Próx. Llegada", 
            "RND", "T. Restante", "Fin Descarga", "RND", "T. Restante", "Fin Descarga",
            "Cola", "M1 Estado", "M1 Inicio Ocup.", "M2 Estado", "M2 Inicio Ocup.",
            "Total", "ID", "Estado", "T. Ingreso"
        };
        
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Agregar datos de prueba
        modelo.addRow(new Object[]{"1", "LL", "12.50", "0.34", "25.00", "0.78", "8.25", "20.75", "---", "---", "---", "2", "Ocupado", "12.50", "Libre", "---", "3", "123", "EB", "12.50"});
        modelo.addRow(new Object[]{"2", "FD1", "20.75", "---", "25.00", "---", "---", "---", "0.45", "6.30", "27.05", "1", "Libre", "---", "Ocupado", "20.75", "3", "123", "SD", "12.50"});
        modelo.addRow(new Object[]{"3", "LL", "25.00", "0.67", "37.80", "0.23", "4.15", "29.15", "---", "---", "---", "2", "Ocupado", "25.00", "Ocupado", "20.75", "4", "124", "EB", "25.00"});
        
        JTable tabla = new JTable(modelo);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabla.setRowHeight(28);
        
        // Usar nuestro encabezado jerárquico personalizado
        GroupableTableHeaderTest header = new GroupableTableHeaderTest(tabla.getColumnModel());
        tabla.setTableHeader(header);
        
        // Estilo oscuro
        tabla.setBackground(new Color(45, 45, 45));
        tabla.setForeground(new Color(240, 240, 240));
        tabla.setGridColor(new Color(70, 70, 70));
        tabla.setSelectionBackground(new Color(100, 149, 237));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setShowGrid(true);
        tabla.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBackground(new Color(45, 45, 45));
        scrollPane.getViewport().setBackground(new Color(45, 45, 45));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 1));
        
        add(scrollPane, BorderLayout.CENTER);
    }
}

/**
 * Encabezado jerárquico simplificado para prueba
 */
class GroupableTableHeaderTest extends JTableHeader {
    
    private static final int ALTURA_PADRE = 35;
    private static final int ALTURA_HIJO = 30;
    private static final int ALTURA_TOTAL = ALTURA_PADRE + ALTURA_HIJO;
    
    private final Map<Integer, GrupoInfo> mapaGrupos = new HashMap<>();
    
    public GroupableTableHeaderTest(TableColumnModel cm) {
        super(cm);
        definirGrupos();
        setPreferredSize(new Dimension(getPreferredSize().width, ALTURA_TOTAL));
        setDefaultRenderer(new RendererJerarquico());
    }
    
    private void definirGrupos() {
        // Control (columnas 0-2)
        mapaGrupos.put(0, new GrupoInfo("Control", new Color(240, 240, 240)));
        mapaGrupos.put(1, new GrupoInfo("Control", new Color(240, 240, 240)));
        mapaGrupos.put(2, new GrupoInfo("Control", new Color(240, 240, 240)));
        
        // Llegada Barco (columnas 3-4)
        mapaGrupos.put(3, new GrupoInfo("Llegada Barco", new Color(255, 228, 196)));
        mapaGrupos.put(4, new GrupoInfo("Llegada Barco", new Color(255, 228, 196)));
        
        // Descarga Muelle 1 (columnas 5-7)
        mapaGrupos.put(5, new GrupoInfo("Descarga Muelle 1", new Color(176, 196, 222)));
        mapaGrupos.put(6, new GrupoInfo("Descarga Muelle 1", new Color(176, 196, 222)));
        mapaGrupos.put(7, new GrupoInfo("Descarga Muelle 1", new Color(176, 196, 222)));
        
        // Descarga Muelle 2 (columnas 8-10)
        mapaGrupos.put(8, new GrupoInfo("Descarga Muelle 2", new Color(176, 196, 222)));
        mapaGrupos.put(9, new GrupoInfo("Descarga Muelle 2", new Color(176, 196, 222)));
        mapaGrupos.put(10, new GrupoInfo("Descarga Muelle 2", new Color(176, 196, 222)));
        
        // Bahía (columna 11)
        mapaGrupos.put(11, new GrupoInfo("Bahía", new Color(221, 160, 221)));
        
        // Estados Muelles (columnas 12-15)
        mapaGrupos.put(12, new GrupoInfo("Estados Muelles", new Color(152, 251, 152)));
        mapaGrupos.put(13, new GrupoInfo("Estados Muelles", new Color(152, 251, 152)));
        mapaGrupos.put(14, new GrupoInfo("Estados Muelles", new Color(152, 251, 152)));
        mapaGrupos.put(15, new GrupoInfo("Estados Muelles", new Color(152, 251, 152)));
        
        // Barcos Sistema (columna 16)
        mapaGrupos.put(16, new GrupoInfo("Barcos en Sistema", new Color(255, 218, 185)));
        
        // Barco B1 (columnas 17-19)
        mapaGrupos.put(17, new GrupoInfo("B1", new Color(255, 218, 185)));
        mapaGrupos.put(18, new GrupoInfo("B1", new Color(255, 218, 185)));
        mapaGrupos.put(19, new GrupoInfo("B1", new Color(255, 218, 185)));
    }
    
    private class RendererJerarquico implements TableCellRenderer {
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            JPanel panel = new JPanel(new BorderLayout());
            panel.setOpaque(true);
            
            GrupoInfo grupo = mapaGrupos.get(column);
            
            if (grupo != null) {
                // Encabezado padre
                JLabel labelPadre = new JLabel(grupo.nombre, SwingConstants.CENTER);
                labelPadre.setBackground(grupo.color);
                labelPadre.setForeground(Color.BLACK);
                labelPadre.setOpaque(true);
                labelPadre.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
                labelPadre.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createRaisedBevelBorder(),
                    BorderFactory.createEmptyBorder(2, 4, 2, 4)
                ));
                labelPadre.setPreferredSize(new Dimension(0, ALTURA_PADRE));
                
                // Encabezado hijo
                JLabel labelHijo = new JLabel(value.toString(), SwingConstants.CENTER);
                labelHijo.setBackground(grupo.color.brighter());
                labelHijo.setForeground(Color.BLACK);
                labelHijo.setOpaque(true);
                labelHijo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
                labelHijo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLoweredBevelBorder(),
                    BorderFactory.createEmptyBorder(2, 4, 2, 4)
                ));
                labelHijo.setPreferredSize(new Dimension(0, ALTURA_HIJO));
                
                panel.add(labelPadre, BorderLayout.NORTH);
                panel.add(labelHijo, BorderLayout.CENTER);
                panel.setBackground(grupo.color);
            } else {
                // Fallback
                JLabel label = new JLabel(value.toString(), SwingConstants.CENTER);
                label.setBackground(Color.LIGHT_GRAY);
                label.setForeground(Color.BLACK);
                label.setOpaque(true);
                label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
                label.setBorder(BorderFactory.createRaisedBevelBorder());
                panel.add(label, BorderLayout.CENTER);
                panel.setBackground(Color.LIGHT_GRAY);
            }
            
            return panel;
        }
    }
    
    private static class GrupoInfo {
        final String nombre;
        final Color color;
        
        GrupoInfo(String nombre, Color color) {
            this.nombre = nombre;
            this.color = color;
        }
    }
}