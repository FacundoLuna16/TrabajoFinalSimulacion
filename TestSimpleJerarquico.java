import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;

/**
 * Test simple para mostrar encabezados jerárquicos simulados
 */
public class TestSimpleJerarquico {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            JFrame frame = new JFrame("Encabezados Jerárquicos - Simulación Puerto");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1600, 800);
            frame.setLocationRelativeTo(null);
            
            // Crear tabla de prueba
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(new Color(25, 25, 25));
            
            // Datos de prueba
            String[] columnas = {
                "Fila", "Evento", "Reloj", "RND", "Próx. Llegada", 
                "RND", "T. Restante", "Fin Descarga", "RND", "T. Restante", "Fin Descarga",
                "Cola", "M1 Estado", "M1 Inicio", "M2 Estado", "M2 Inicio",
                "Total", "ID", "Estado", "T. Ingreso", "ID", "Estado", "T. Ingreso"
            };
            
            DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            
            // Agregar filas de prueba
            modelo.addRow(new Object[]{"1", "LL", "12.50", "0.34", "25.00", "0.78", "8.25", "20.75", "---", "---", "---", "2", "Ocupado", "12.50", "Libre", "---", "3", "123", "EB", "12.50", "124", "EB", "25.00"});
            modelo.addRow(new Object[]{"2", "FD1", "20.75", "---", "25.00", "---", "---", "---", "0.45", "6.30", "27.05", "1", "Libre", "---", "Ocupado", "20.75", "3", "123", "SD", "12.50", "124", "EB", "25.00"});
            modelo.addRow(new Object[]{"3", "LL", "25.00", "0.67", "37.80", "0.23", "4.15", "29.15", "---", "---", "---", "2", "Ocupado", "25.00", "Ocupado", "20.75", "4", "125", "EB", "25.00", "124", "SD", "25.00"});
            
            JTable tabla = new JTable(modelo);
            tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tabla.setRowHeight(28);
            
            // Renderer personalizado para el encabezado
            JTableHeader header = tabla.getTableHeader();
            header.setDefaultRenderer(new EncabezadoJerarquicoRenderer());
            header.setPreferredSize(new Dimension(header.getPreferredSize().width, 65)); // Altura doble
            
            // Estilo oscuro
            tabla.setBackground(new Color(45, 45, 45));
            tabla.setForeground(new Color(240, 240, 240));
            tabla.setGridColor(new Color(70, 70, 70));
            tabla.setSelectionBackground(new Color(100, 149, 237));
            tabla.setSelectionForeground(Color.WHITE);
            tabla.setShowGrid(true);
            tabla.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
            
            // Configurar anchos de columnas
            configurarAnchos(tabla);
            
            JScrollPane scrollPane = new JScrollPane(tabla);
            scrollPane.setBackground(new Color(45, 45, 45));
            scrollPane.getViewport().setBackground(new Color(45, 45, 45));
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 1));
            
            // Agregar título
            JLabel titulo = new JLabel("TABLA CON ENCABEZADOS JERÁRQUICOS - SIMULACIÓN PUERTO", SwingConstants.CENTER);
            titulo.setForeground(new Color(100, 149, 237));
            titulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
            titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            panel.add(titulo, BorderLayout.NORTH);
            panel.add(scrollPane, BorderLayout.CENTER);
            
            frame.add(panel);
            frame.setVisible(true);
            
            System.out.println("✅ Tabla jerárquica funcionando!");
        });
    }
    
    private static void configurarAnchos(JTable tabla) {
        int[] anchos = {60, 100, 80, 80, 100, 80, 100, 100, 80, 100, 100, 60, 100, 100, 100, 100, 80, 60, 80, 100, 60, 80, 100};
        
        for (int i = 0; i < Math.min(anchos.length, tabla.getColumnCount()); i++) {
            TableColumn column = tabla.getColumnModel().getColumn(i);
            column.setPreferredWidth(anchos[i]);
            column.setMinWidth(50);
            column.setMaxWidth(200);
        }
    }
}

/**
 * Renderer que crea encabezados jerárquicos visuales
 */
class EncabezadoJerarquicoRenderer extends DefaultTableCellRenderer {
    
    private final Map<Integer, GrupoInfo> grupos = new HashMap<>();
    
    public EncabezadoJerarquicoRenderer() {
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        
        // Definir grupos por índice de columna
        // Control (0-2)
        grupos.put(0, new GrupoInfo("Control", new Color(240, 240, 240)));
        grupos.put(1, new GrupoInfo("Control", new Color(240, 240, 240)));
        grupos.put(2, new GrupoInfo("Control", new Color(240, 240, 240)));
        
        // Llegada Barco (3-4)
        grupos.put(3, new GrupoInfo("Llegada Barco", new Color(255, 228, 196)));
        grupos.put(4, new GrupoInfo("Llegada Barco", new Color(255, 228, 196)));
        
        // Descarga Muelle 1 (5-7)
        grupos.put(5, new GrupoInfo("Descarga Muelle 1", new Color(176, 196, 222)));
        grupos.put(6, new GrupoInfo("Descarga Muelle 1", new Color(176, 196, 222)));
        grupos.put(7, new GrupoInfo("Descarga Muelle 1", new Color(176, 196, 222)));
        
        // Descarga Muelle 2 (8-10)
        grupos.put(8, new GrupoInfo("Descarga Muelle 2", new Color(135, 155, 180))); // Variación del azul
        grupos.put(9, new GrupoInfo("Descarga Muelle 2", new Color(135, 155, 180)));
        grupos.put(10, new GrupoInfo("Descarga Muelle 2", new Color(135, 155, 180)));
        
        // Bahía (11)
        grupos.put(11, new GrupoInfo("Bahía", new Color(221, 160, 221)));
        
        // Estados Muelles (12-15)
        grupos.put(12, new GrupoInfo("Estados Muelles", new Color(152, 251, 152)));
        grupos.put(13, new GrupoInfo("Estados Muelles", new Color(152, 251, 152)));
        grupos.put(14, new GrupoInfo("Estados Muelles", new Color(152, 251, 152)));
        grupos.put(15, new GrupoInfo("Estados Muelles", new Color(152, 251, 152)));
        
        // Barcos Sistema (16)
        grupos.put(16, new GrupoInfo("Barcos Sistema", new Color(255, 218, 185)));
        
        // Barcos B1 (17-19)
        grupos.put(17, new GrupoInfo("B1", new Color(255, 200, 160)));
        grupos.put(18, new GrupoInfo("B1", new Color(255, 200, 160)));
        grupos.put(19, new GrupoInfo("B1", new Color(255, 200, 160)));
        
        // Barcos B2 (20-22)
        grupos.put(20, new GrupoInfo("B2", new Color(255, 180, 140)));
        grupos.put(21, new GrupoInfo("B2", new Color(255, 180, 140)));
        grupos.put(22, new GrupoInfo("B2", new Color(255, 180, 140)));
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        GrupoInfo grupo = grupos.get(column);
        
        if (grupo != null) {
            // Crear un panel con layout vertical para mostrar grupo y columna
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setOpaque(true);
            panel.setBackground(grupo.color);
            panel.setBorder(BorderFactory.createRaisedBevelBorder());
            
            // Label del grupo (padre)
            JLabel labelGrupo = new JLabel(grupo.nombre);
            labelGrupo.setHorizontalAlignment(SwingConstants.CENTER);
            labelGrupo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
            labelGrupo.setForeground(Color.BLACK);
            labelGrupo.setOpaque(true);
            labelGrupo.setBackground(grupo.color);
            labelGrupo.setBorder(BorderFactory.createEmptyBorder(2, 4, 1, 4));
            
            // Label de la columna (hijo)
            JLabel labelColumna = new JLabel(value.toString());
            labelColumna.setHorizontalAlignment(SwingConstants.CENTER);
            labelColumna.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
            labelColumna.setForeground(Color.BLACK);
            labelColumna.setOpaque(true);
            labelColumna.setBackground(grupo.color.brighter());
            labelColumna.setBorder(BorderFactory.createEmptyBorder(1, 4, 2, 4));
            
            panel.add(labelGrupo);
            panel.add(labelColumna);
            
            return panel;
        } else {
            // Fallback a renderer normal
            setBackground(Color.LIGHT_GRAY);
            setForeground(Color.BLACK);
            setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
            setBorder(BorderFactory.createRaisedBevelBorder());
            setText(value.toString());
            setOpaque(true);
            return this;
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