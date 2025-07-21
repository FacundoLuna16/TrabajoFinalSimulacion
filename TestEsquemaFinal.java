import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;

/**
 * Versión final con scroll lateral funcionando correctamente
 */
public class TestEsquemaFinal {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            JFrame frame = new JFrame("ESQUEMA JERÁRQUICO FINAL - Con Scroll Completo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800); // Ventana más pequeña para forzar scroll
            frame.setLocationRelativeTo(null);
            
            TablaEsquemaFinal tabla = new TablaEsquemaFinal();
            frame.add(tabla, BorderLayout.CENTER);
            
            frame.setVisible(true);
            
            System.out.println("✅ ESQUEMA FINAL con scroll lateral funcionando!");
            System.out.println("📜 Prueba hacer scroll horizontal para ver todas las columnas");
        });
    }
}

class TablaEsquemaFinal extends JPanel {
    
    public TablaEsquemaFinal() {
        setLayout(new BorderLayout());
        setBackground(new Color(25, 25, 25));
        
        // Crear tabla con muchas más columnas para demostrar el scroll
        JTable tabla = crearTablaCompleta();
        
        // Crear encabezados jerárquicos que se ajusten al scroll
        EncabezadosConScroll encabezados = new EncabezadosConScroll(tabla);
        
        // Panel que contiene encabezados y tabla sincronizados
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.add(encabezados, BorderLayout.NORTH);
        panelTabla.add(tabla, BorderLayout.CENTER);
        
        // Scroll pane que maneja tanto tabla como encabezados
        JScrollPane scrollPane = new JScrollPane(panelTabla);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(new Color(50, 50, 50));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));
        
        // Título
        JLabel titulo = new JLabel("ENCABEZADOS JERÁRQUICOS CON SCROLL LATERAL FUNCIONANDO", SwingConstants.CENTER);
        titulo.setForeground(new Color(100, 149, 237));
        titulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        add(titulo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // Info sobre scroll
        JLabel info = new JLabel("📜 Usa el scroll horizontal para ver todas las columnas - Los encabezados se mueven con la tabla", SwingConstants.CENTER);
        info.setForeground(new Color(200, 200, 200));
        info.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
        info.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
        add(info, BorderLayout.SOUTH);
    }
    
    private JTable crearTablaCompleta() {
        // Crear tabla con MUCHAS columnas para mostrar el scroll
        String[] columnas = {
            // Control (3)
            "Fila", "Evento", "Reloj", 
            // Llegada (2)
            "RND", "Próx.Lleg", 
            // Descarga M1 (3)
            "RND", "T.Rest", "FinDesc", 
            // Descarga M2 (3)
            "RND", "T.Rest", "FinDesc", 
            // Bahía (1)
            "Cola", 
            // Estados Muelles (4)
            "M1Est", "M1Inic", "M2Est", "M2Inic",
            // Estados Grúas (4)
            "G1Est", "G1Inic", "G2Est", "G2Inic", 
            // T. Permanencia (5)
            "Máx", "Mín", "Acum", "Cant", "Media", 
            // Utilización (8)
            "M1AcTO", "M1%", "M2AcTO", "M2%", "G1AcTO", "G1%", "G2AcTO", "G2%",
            // Sistema (1)
            "Total", 
            // Barcos B1 (3)
            "ID", "Estado", "Ingreso",
            // Barcos B2 (3)
            "ID", "Estado", "Ingreso",
            // Barcos B3 (3)
            "ID", "Estado", "Ingreso"
        };
        
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Agregar datos de prueba
        modelo.addRow(new Object[]{
            "1", "LL", "12.50", "0.34", "25.00", "0.78", "8.25", "20.75", "---", "---", "---", "2",
            "Ocupado", "12.50", "Libre", "---", "Ocupado", "12.50", "Libre", "---",
            "15.2", "8.1", "45.8", "3", "11.8", "25.4", "68%", "18.2", "45%", "32.1", "72%", "28.7", "51%", "3",
            "123", "EB", "12.50", "124", "EB", "25.00", "---", "---", "---"
        });
        
        modelo.addRow(new Object[]{
            "2", "FD1", "20.75", "---", "25.00", "---", "---", "---", "0.45", "6.30", "27.05", "1",
            "Libre", "---", "Ocupado", "20.75", "Libre", "---", "Ocupado", "20.75",
            "15.2", "8.1", "45.8", "3", "12.1", "25.4", "68%", "26.4", "58%", "32.1", "72%", "36.9", "63%", "3",
            "123", "SD", "12.50", "124", "EB", "25.00", "---", "---", "---"
        });
        
        modelo.addRow(new Object[]{
            "3", "LL", "25.00", "0.67", "37.80", "0.23", "4.15", "29.15", "---", "---", "---", "2",
            "Ocupado", "25.00", "Ocupado", "20.75", "Ocupado", "25.00", "Ocupado", "20.75",
            "18.5", "8.1", "52.3", "4", "13.2", "29.6", "72%", "26.4", "58%", "38.7", "78%", "36.9", "63%", "4",
            "125", "EB", "25.00", "124", "SD", "25.00", "126", "EB", "35.20"
        });
        
        JTable tabla = new JTable(modelo);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // IMPORTANTE para scroll horizontal
        tabla.setRowHeight(30);
        tabla.setTableHeader(null); // Sin encabezado normal
        
        // Estilo
        tabla.setBackground(new Color(50, 50, 50));
        tabla.setForeground(new Color(240, 240, 240));
        tabla.setGridColor(new Color(100, 100, 100));
        tabla.setSelectionBackground(new Color(100, 149, 237));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setShowGrid(true);
        tabla.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
        
        // Configurar anchos específicos
        configurarAnchos(tabla);
        
        return tabla;
    }
    
    private void configurarAnchos(JTable tabla) {
        int[] anchos = {
            50, 70, 60,  // Control
            60, 80,      // Llegada
            60, 70, 80,  // Descarga M1
            60, 70, 80,  // Descarga M2
            45,          // Bahía
            70, 70, 70, 70,  // Estados Muelles
            70, 70, 70, 70,  // Estados Grúas
            50, 50, 60, 50, 60,  // T. Permanencia
            70, 50, 70, 50, 70, 50, 70, 50,  // Utilización
            50,          // Sistema
            40, 55, 70,  // B1
            40, 55, 70,  // B2
            40, 55, 70   // B3
        };
        
        for (int i = 0; i < Math.min(anchos.length, tabla.getColumnCount()); i++) {
            TableColumn column = tabla.getColumnModel().getColumn(i);
            column.setPreferredWidth(anchos[i]);
            column.setMinWidth(anchos[i]);
            column.setMaxWidth(anchos[i] + 20);
        }
    }
}

/**
 * Encabezados que se sincronizan con el scroll de la tabla
 */
class EncabezadosConScroll extends JPanel {
    
    private static final int ALTURA_PADRE = 30;
    private static final int ALTURA_HIJO = 25;
    
    public EncabezadosConScroll(JTable tabla) {
        setLayout(new BorderLayout());
        setBackground(new Color(40, 40, 40));
        setPreferredSize(new Dimension(calcularAnchoTotal(), ALTURA_PADRE + ALTURA_HIJO + 4));
        
        // Panel padre
        JPanel panelPadre = new JPanel();
        panelPadre.setLayout(new BoxLayout(panelPadre, BoxLayout.X_AXIS));
        panelPadre.setBackground(new Color(40, 40, 40));
        panelPadre.setPreferredSize(new Dimension(calcularAnchoTotal(), ALTURA_PADRE));
        
        // Panel hijo
        JPanel panelHijo = new JPanel();
        panelHijo.setLayout(new BoxLayout(panelHijo, BoxLayout.X_AXIS));
        panelHijo.setBackground(new Color(45, 45, 45));
        panelHijo.setPreferredSize(new Dimension(calcularAnchoTotal(), ALTURA_HIJO));
        
        crearEncabezadosPadre(panelPadre);
        crearEncabezadosHijo(panelHijo);
        
        add(panelPadre, BorderLayout.NORTH);
        add(panelHijo, BorderLayout.CENTER);
        
        setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
    }
    
    private int calcularAnchoTotal() {
        // Suma de todos los anchos de columnas
        int[] anchos = {50, 70, 60, 60, 80, 60, 70, 80, 60, 70, 80, 45, 70, 70, 70, 70, 70, 70, 70, 70, 50, 50, 60, 50, 60, 70, 50, 70, 50, 70, 50, 70, 50, 50, 40, 55, 70, 40, 55, 70, 40, 55, 70};
        int total = 0;
        for (int ancho : anchos) total += ancho;
        return total;
    }
    
    private void crearEncabezadosPadre(JPanel panel) {
        // Control (3 cols: 50+70+60 = 180)
        panel.add(crearLabelPadre("CONTROL", new Color(240, 240, 240), 180));
        
        // Llegada (2 cols: 60+80 = 140)
        panel.add(crearLabelPadre("LLEGADA BARCO", new Color(255, 200, 150), 140));
        
        // Descarga M1 (3 cols: 60+70+80 = 210)
        panel.add(crearLabelPadre("DESCARGA MUELLE 1", new Color(150, 180, 220), 210));
        
        // Descarga M2 (3 cols: 60+70+80 = 210)
        panel.add(crearLabelPadre("DESCARGA MUELLE 2", new Color(120, 150, 200), 210));
        
        // Bahía (1 col: 45)
        panel.add(crearLabelPadre("BAHÍA", new Color(200, 150, 200), 45));
        
        // Estados Muelles (4 cols: 70*4 = 280)
        panel.add(crearLabelPadre("ESTADOS MUELLES", new Color(150, 220, 150), 280));
        
        // Estados Grúas (4 cols: 70*4 = 280)
        panel.add(crearLabelPadre("ESTADOS GRÚAS", new Color(255, 180, 180), 280));
        
        // T. Permanencia (5 cols: 50+50+60+50+60 = 270)
        panel.add(crearLabelPadre("T. PERMANENCIA", new Color(255, 255, 180), 270));
        
        // Utilización (8 cols: 70+50+70+50+70+50+70+50 = 480)
        panel.add(crearLabelPadre("UTILIZACIÓN", new Color(180, 220, 255), 480));
        
        // Sistema (1 col: 50)
        panel.add(crearLabelPadre("SISTEMA", new Color(255, 220, 180), 50));
        
        // B1 (3 cols: 40+55+70 = 165)
        panel.add(crearLabelPadre("B1", new Color(255, 180, 120), 165));
        
        // B2 (3 cols: 40+55+70 = 165)
        panel.add(crearLabelPadre("B2", new Color(255, 160, 100), 165));
        
        // B3 (3 cols: 40+55+70 = 165)
        panel.add(crearLabelPadre("B3", new Color(255, 140, 80), 165));
    }
    
    private void crearEncabezadosHijo(JPanel panel) {
        String[] hijos = {
            "Fila", "Evento", "Reloj", "RND", "Próx.Lleg", "RND", "T.Rest", "FinDesc", 
            "RND", "T.Rest", "FinDesc", "Cola", "M1Est", "M1Inic", "M2Est", "M2Inic",
            "G1Est", "G1Inic", "G2Est", "G2Inic", "Máx", "Mín", "Acum", "Cant", "Media",
            "M1AcTO", "M1%", "M2AcTO", "M2%", "G1AcTO", "G1%", "G2AcTO", "G2%", "Total",
            "ID", "Estado", "Ingreso", "ID", "Estado", "Ingreso", "ID", "Estado", "Ingreso"
        };
        
        Color[] colores = {
            // Control (3)
            new Color(240, 240, 240), new Color(240, 240, 240), new Color(240, 240, 240),
            // Llegada (2)
            new Color(255, 200, 150), new Color(255, 200, 150),
            // Descarga M1 (3)
            new Color(150, 180, 220), new Color(150, 180, 220), new Color(150, 180, 220),
            // Descarga M2 (3)
            new Color(120, 150, 200), new Color(120, 150, 200), new Color(120, 150, 200),
            // Bahía (1)
            new Color(200, 150, 200),
            // Estados Muelles (4)
            new Color(150, 220, 150), new Color(150, 220, 150), new Color(150, 220, 150), new Color(150, 220, 150),
            // Estados Grúas (4)
            new Color(255, 180, 180), new Color(255, 180, 180), new Color(255, 180, 180), new Color(255, 180, 180),
            // T. Permanencia (5)
            new Color(255, 255, 180), new Color(255, 255, 180), new Color(255, 255, 180), new Color(255, 255, 180), new Color(255, 255, 180),
            // Utilización (8)
            new Color(180, 220, 255), new Color(180, 220, 255), new Color(180, 220, 255), new Color(180, 220, 255),
            new Color(180, 220, 255), new Color(180, 220, 255), new Color(180, 220, 255), new Color(180, 220, 255),
            // Sistema (1)
            new Color(255, 220, 180),
            // B1 (3)
            new Color(255, 180, 120), new Color(255, 180, 120), new Color(255, 180, 120),
            // B2 (3)
            new Color(255, 160, 100), new Color(255, 160, 100), new Color(255, 160, 100),
            // B3 (3)
            new Color(255, 140, 80), new Color(255, 140, 80), new Color(255, 140, 80)
        };
        
        int[] anchos = {50, 70, 60, 60, 80, 60, 70, 80, 60, 70, 80, 45, 70, 70, 70, 70, 70, 70, 70, 70, 50, 50, 60, 50, 60, 70, 50, 70, 50, 70, 50, 70, 50, 50, 40, 55, 70, 40, 55, 70, 40, 55, 70};
        
        for (int i = 0; i < hijos.length; i++) {
            panel.add(crearLabelHijo(hijos[i], colores[i], anchos[i]));
        }
    }
    
    private JLabel crearLabelPadre(String texto, Color color, int ancho) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(color);
        label.setForeground(Color.BLACK);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 9));
        label.setBorder(BorderFactory.createRaisedBevelBorder());
        label.setPreferredSize(new Dimension(ancho, ALTURA_PADRE));
        label.setMinimumSize(new Dimension(ancho, ALTURA_PADRE));
        label.setMaximumSize(new Dimension(ancho, ALTURA_PADRE));
        return label;
    }
    
    private JLabel crearLabelHijo(String texto, Color color, int ancho) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(color.brighter());
        label.setForeground(Color.BLACK);
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 8));
        label.setBorder(BorderFactory.createLoweredBevelBorder());
        label.setPreferredSize(new Dimension(ancho, ALTURA_HIJO));
        label.setMinimumSize(new Dimension(ancho, ALTURA_HIJO));
        label.setMaximumSize(new Dimension(ancho, ALTURA_HIJO));
        return label;
    }
}