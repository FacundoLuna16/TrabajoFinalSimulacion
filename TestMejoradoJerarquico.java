import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;

/**
 * Test mejorado para mostrar encabezados jerárquicos más claros y visibles
 */
public class TestMejoradoJerarquico {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            JFrame frame = new JFrame("Encabezados Jerárquicos Mejorados - Puerto");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1800, 900);
            frame.setLocationRelativeTo(null);
            
            // Crear panel principal
            JPanel panelPrincipal = new JPanel(new BorderLayout());
            panelPrincipal.setBackground(new Color(25, 25, 25));
            
            // Crear la tabla mejorada
            TablaJerarquicaMejorada tablaPanel = new TablaJerarquicaMejorada();
            panelPrincipal.add(tablaPanel, BorderLayout.CENTER);
            
            // Título más visible
            JLabel titulo = new JLabel("SIMULACIÓN PUERTO - ENCABEZADOS JERÁRQUICOS", SwingConstants.CENTER);
            titulo.setForeground(new Color(100, 149, 237));
            titulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
            titulo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            panelPrincipal.add(titulo, BorderLayout.NORTH);
            
            frame.add(panelPrincipal);
            frame.setVisible(true);
            
            System.out.println("✅ Tabla jerárquica mejorada funcionando!");
            System.out.println("🔍 Observa los encabezados de dos niveles con colores distintos");
        });
    }
}

/**
 * Panel con tabla que tiene encabezados jerárquicos realmente visibles
 */
class TablaJerarquicaMejorada extends JPanel {
    
    public TablaJerarquicaMejorada() {
        setLayout(new BorderLayout());
        setBackground(new Color(25, 25, 25));
        
        // Crear modelo con datos más realistas
        String[] columnas = {
            "Fila", "Evento", "Reloj", 
            "RND", "Próx. Llegada", 
            "RND", "T. Restante", "Fin Descarga", 
            "RND", "T. Restante", "Fin Descarga",
            "Cola",
            "M1 Estado", "M1 Inicio", "M2 Estado", "M2 Inicio",
            "G1 Estado", "G1 Inicio", "G2 Estado", "G2 Inicio",
            "Máx", "Mín", "Promedio",
            "M1 %", "M2 %", "G1 %", "G2 %",
            "Total",
            "ID", "Estado", "Ingreso"
        };
        
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Datos de simulación más realistas
        modelo.addRow(new Object[]{
            "1", "LL", "12.50", 
            "0.3421", "25.00", 
            "0.7834", "8.25", "20.75", 
            "---", "---", "---",
            "2",
            "Ocupado", "12.50", "Libre", "---",
            "Ocupado", "12.50", "Libre", "---",
            "15.2", "8.1", "11.8",
            "68%", "45%", "72%", "51%",
            "3",
            "123", "EB", "12.50"
        });
        
        modelo.addRow(new Object[]{
            "2", "FD1", "20.75", 
            "---", "25.00", 
            "---", "---", "---", 
            "0.4521", "6.30", "27.05",
            "1",
            "Libre", "---", "Ocupado", "20.75",
            "Libre", "---", "Ocupado", "20.75",
            "15.2", "8.1", "12.1",
            "68%", "58%", "72%", "63%",
            "3",
            "123", "SD", "12.50"
        });
        
        modelo.addRow(new Object[]{
            "3", "LL", "25.00", 
            "0.6789", "37.80", 
            "0.2341", "4.15", "29.15", 
            "---", "---", "---",
            "2",
            "Ocupado", "25.00", "Ocupado", "20.75",
            "Ocupado", "25.00", "Ocupado", "20.75",
            "18.5", "8.1", "13.2",
            "72%", "58%", "78%", "63%",
            "4",
            "124", "EB", "25.00"
        });
        
        JTable tabla = new JTable(modelo);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabla.setRowHeight(30);
        
        // CLAVE: Usar el renderer mejorado con altura suficiente
        JTableHeader header = tabla.getTableHeader();
        header.setDefaultRenderer(new RendererJerarquicoMejorado());
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 80)); // Altura mayor
        header.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        
        // Estilo de la tabla
        tabla.setBackground(new Color(50, 50, 50));
        tabla.setForeground(new Color(240, 240, 240));
        tabla.setGridColor(new Color(100, 100, 100));
        tabla.setSelectionBackground(new Color(100, 149, 237));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setShowGrid(true);
        tabla.setIntercellSpacing(new Dimension(1, 1));
        tabla.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        
        // Configurar anchos específicos
        configurarAnchoColumnas(tabla);
        
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBackground(new Color(45, 45, 45));
        scrollPane.getViewport().setBackground(new Color(45, 45, 45));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));
        
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel de información
        JPanel panelInfo = new JPanel(new FlowLayout());
        panelInfo.setBackground(new Color(25, 25, 25));
        
        JLabel info = new JLabel("🔸 Observe los encabezados jerárquicos con grupos padre e hijo claramente diferenciados");
        info.setForeground(new Color(200, 200, 200));
        info.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
        panelInfo.add(info);
        
        add(panelInfo, BorderLayout.SOUTH);
    }
    
    private void configurarAnchoColumnas(JTable tabla) {
        int[] anchos = {
            // Control
            50, 80, 70,
            // Llegada
            70, 90,
            // Descarga M1
            70, 90, 90,
            // Descarga M2 
            70, 90, 90,
            // Bahía
            50,
            // Estados Muelles
            80, 80, 80, 80,
            // Estados Grúas
            80, 80, 80, 80,
            // T. Permanencia
            60, 60, 80,
            // Utilización
            60, 60, 60, 60,
            // Sistema
            60,
            // Barco
            50, 60, 70
        };
        
        for (int i = 0; i < Math.min(anchos.length, tabla.getColumnCount()); i++) {
            TableColumn column = tabla.getColumnModel().getColumn(i);
            column.setPreferredWidth(anchos[i]);
            column.setMinWidth(40);
            column.setMaxWidth(150);
        }
    }
}

/**
 * Renderer que crea encabezados jerárquicos MUY VISIBLES
 */
class RendererJerarquicoMejorado extends JPanel implements TableCellRenderer {
    
    private final Map<Integer, GrupoDefinicion> grupos = new HashMap<>();
    private JLabel labelGrupo;
    private JLabel labelColumna;
    
    public RendererJerarquicoMejorado() {
        setLayout(new BorderLayout());
        definirGrupos();
        
        labelGrupo = new JLabel();
        labelGrupo.setHorizontalAlignment(SwingConstants.CENTER);
        labelGrupo.setVerticalAlignment(SwingConstants.CENTER);
        labelGrupo.setOpaque(true);
        labelGrupo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        labelGrupo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(2, 4, 2, 4)
        ));
        
        labelColumna = new JLabel();
        labelColumna.setHorizontalAlignment(SwingConstants.CENTER);
        labelColumna.setVerticalAlignment(SwingConstants.CENTER);
        labelColumna.setOpaque(true);
        labelColumna.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        labelColumna.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLoweredBevelBorder(),
            BorderFactory.createEmptyBorder(2, 3, 2, 3)
        ));
        
        add(labelGrupo, BorderLayout.NORTH);
        add(labelColumna, BorderLayout.CENTER);
    }
    
    private void definirGrupos() {
        // Control (0-2) - Gris claro
        Color colorControl = new Color(220, 220, 220);
        grupos.put(0, new GrupoDefinicion("CONTROL", colorControl));
        grupos.put(1, new GrupoDefinicion("CONTROL", colorControl));
        grupos.put(2, new GrupoDefinicion("CONTROL", colorControl));
        
        // Llegada Barco (3-4) - Naranja
        Color colorLlegada = new Color(255, 200, 150);
        grupos.put(3, new GrupoDefinicion("LLEGADA BARCO", colorLlegada));
        grupos.put(4, new GrupoDefinicion("LLEGADA BARCO", colorLlegada));
        
        // Descarga Muelle 1 (5-7) - Azul claro
        Color colorM1 = new Color(150, 180, 220);
        grupos.put(5, new GrupoDefinicion("DESCARGA MUELLE 1", colorM1));
        grupos.put(6, new GrupoDefinicion("DESCARGA MUELLE 1", colorM1));
        grupos.put(7, new GrupoDefinicion("DESCARGA MUELLE 1", colorM1));
        
        // Descarga Muelle 2 (8-10) - Azul más oscuro
        Color colorM2 = new Color(120, 150, 200);
        grupos.put(8, new GrupoDefinicion("DESCARGA MUELLE 2", colorM2));
        grupos.put(9, new GrupoDefinicion("DESCARGA MUELLE 2", colorM2));
        grupos.put(10, new GrupoDefinicion("DESCARGA MUELLE 2", colorM2));
        
        // Bahía (11) - Violeta
        Color colorBahia = new Color(200, 150, 200);
        grupos.put(11, new GrupoDefinicion("BAHÍA", colorBahia));
        
        // Estados Muelles (12-15) - Verde
        Color colorEstMuelles = new Color(150, 220, 150);
        grupos.put(12, new GrupoDefinicion("ESTADOS MUELLES", colorEstMuelles));
        grupos.put(13, new GrupoDefinicion("ESTADOS MUELLES", colorEstMuelles));
        grupos.put(14, new GrupoDefinicion("ESTADOS MUELLES", colorEstMuelles));
        grupos.put(15, new GrupoDefinicion("ESTADOS MUELLES", colorEstMuelles));
        
        // Estados Grúas (16-19) - Rosa
        Color colorEstGruas = new Color(255, 180, 180);
        grupos.put(16, new GrupoDefinicion("ESTADOS GRÚAS", colorEstGruas));
        grupos.put(17, new GrupoDefinicion("ESTADOS GRÚAS", colorEstGruas));
        grupos.put(18, new GrupoDefinicion("ESTADOS GRÚAS", colorEstGruas));
        grupos.put(19, new GrupoDefinicion("ESTADOS GRÚAS", colorEstGruas));
        
        // T. Permanencia (20-22) - Amarillo
        Color colorTPerm = new Color(255, 255, 180);
        grupos.put(20, new GrupoDefinicion("T. PERMANENCIA", colorTPerm));
        grupos.put(21, new GrupoDefinicion("T. PERMANENCIA", colorTPerm));
        grupos.put(22, new GrupoDefinicion("T. PERMANENCIA", colorTPerm));
        
        // Utilización (23-26) - Cian
        Color colorUtil = new Color(180, 220, 255);
        grupos.put(23, new GrupoDefinicion("UTILIZACIÓN", colorUtil));
        grupos.put(24, new GrupoDefinicion("UTILIZACIÓN", colorUtil));
        grupos.put(25, new GrupoDefinicion("UTILIZACIÓN", colorUtil));
        grupos.put(26, new GrupoDefinicion("UTILIZACIÓN", colorUtil));
        
        // Barcos Sistema (27) - Melocotón
        Color colorSistema = new Color(255, 220, 180);
        grupos.put(27, new GrupoDefinicion("BARCOS SISTEMA", colorSistema));
        
        // Barco B1 (28-30) - Naranja oscuro
        Color colorB1 = new Color(255, 180, 120);
        grupos.put(28, new GrupoDefinicion("BARCO B1", colorB1));
        grupos.put(29, new GrupoDefinicion("BARCO B1", colorB1));
        grupos.put(30, new GrupoDefinicion("BARCO B1", colorB1));
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        GrupoDefinicion grupo = grupos.get(column);
        
        if (grupo != null) {
            // Configurar label del grupo (padre)
            labelGrupo.setText(grupo.nombre);
            labelGrupo.setBackground(grupo.color);
            labelGrupo.setForeground(Color.BLACK);
            
            // Configurar label de la columna (hijo)
            labelColumna.setText(value != null ? value.toString() : "");
            labelColumna.setBackground(grupo.color.brighter());
            labelColumna.setForeground(Color.BLACK);
            
            setBackground(grupo.color);
            setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        } else {
            // Fallback
            labelGrupo.setText("SIN GRUPO");
            labelGrupo.setBackground(Color.LIGHT_GRAY);
            labelGrupo.setForeground(Color.BLACK);
            
            labelColumna.setText(value != null ? value.toString() : "");
            labelColumna.setBackground(Color.WHITE);
            labelColumna.setForeground(Color.BLACK);
            
            setBackground(Color.LIGHT_GRAY);
            setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
        
        setOpaque(true);
        return this;
    }
    
    private static class GrupoDefinicion {
        final String nombre;
        final Color color;
        
        GrupoDefinicion(String nombre, Color color) {
            this.nombre = nombre;
            this.color = color;
        }
    }
}