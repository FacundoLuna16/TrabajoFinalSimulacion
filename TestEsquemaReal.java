import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;

/**
 * Test que implementa el VERDADERO esquema jerárquico como el diseño mostrado,
 * donde los grupos padre abarcan múltiples columnas hijas.
 */
public class TestEsquemaReal {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            JFrame frame = new JFrame("ESQUEMA JERÁRQUICO REAL - Como el Diseño");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1900, 900);
            frame.setLocationRelativeTo(null);
            
            // Crear el panel con la tabla que implementa el esquema real
            TablaEsquemaReal tabla = new TablaEsquemaReal();
            frame.add(tabla, BorderLayout.CENTER);
            
            frame.setVisible(true);
            
            System.out.println("✅ ESQUEMA JERÁRQUICO REAL implementado!");
            System.out.println("🎯 Ahora los grupos padre abarcan múltiples columnas como en el diseño");
        });
    }
}

/**
 * Tabla que implementa el verdadero esquema jerárquico con encabezados
 * padre que abarcan múltiples columnas hijas.
 */
class TablaEsquemaReal extends JPanel {
    
    public TablaEsquemaReal() {
        setLayout(new BorderLayout());
        setBackground(new Color(25, 25, 25));
        
        // Crear un panel que contenga tanto los encabezados jerárquicos como la tabla
        JPanel panelCompleto = new JPanel(new BorderLayout());
        panelCompleto.setBackground(new Color(25, 25, 25));
        
        // 1. CREAR ENCABEZADOS JERÁRQUICOS REALES
        EncabezadosJerarquicos encabezados = new EncabezadosJerarquicos();
        panelCompleto.add(encabezados, BorderLayout.NORTH);
        
        // 2. CREAR TABLA CON DATOS
        JTable tabla = crearTablaConDatos();
        
        // IMPORTANTE: Ocultar el encabezado normal de la tabla porque usamos el personalizado
        tabla.setTableHeader(null);
        
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBackground(new Color(50, 50, 50));
        scrollPane.getViewport().setBackground(new Color(50, 50, 50));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));
        
        panelCompleto.add(scrollPane, BorderLayout.CENTER);
        
        // 3. TÍTULO EXPLICATIVO
        JLabel titulo = new JLabel("TABLA CON ENCABEZADOS JERÁRQUICOS REALES", SwingConstants.CENTER);
        titulo.setForeground(new Color(100, 149, 237));
        titulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        add(titulo, BorderLayout.NORTH);
        add(panelCompleto, BorderLayout.CENTER);
        
        // Info
        JLabel info = new JLabel("🎯 Grupos padre abarcan múltiples columnas - Implementación real del esquema", SwingConstants.CENTER);
        info.setForeground(new Color(200, 200, 200));
        info.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
        info.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
        add(info, BorderLayout.SOUTH);
    }
    
    private JTable crearTablaConDatos() {
        String[] columnas = {
            "Fila", "Evento", "Reloj", "RND", "Próx.Lleg", "RND", "T.Rest", "FinDesc", 
            "RND", "T.Rest", "FinDesc", "Cola", "M1Est", "M1Inic", "M2Est", "M2Inic",
            "G1Est", "G1Inic", "G2Est", "G2Inic", "Máx", "Mín", "Media", "M1%", "M2%", "G1%", "G2%", "Total", "ID", "Estado", "Ingreso"
        };
        
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Datos realistas de simulación
        modelo.addRow(new Object[]{
            "1", "LL", "12.50", "0.34", "25.00", "0.78", "8.25", "20.75", "---", "---", "---", "2",
            "Ocupado", "12.50", "Libre", "---", "Ocupado", "12.50", "Libre", "---",
            "15.2", "8.1", "11.8", "68%", "45%", "72%", "51%", "3", "123", "EB", "12.50"
        });
        
        modelo.addRow(new Object[]{
            "2", "FD1", "20.75", "---", "25.00", "---", "---", "---", "0.45", "6.30", "27.05", "1",
            "Libre", "---", "Ocupado", "20.75", "Libre", "---", "Ocupado", "20.75",
            "15.2", "8.1", "12.1", "68%", "58%", "72%", "63%", "3", "123", "SD", "12.50"
        });
        
        modelo.addRow(new Object[]{
            "3", "LL", "25.00", "0.67", "37.80", "0.23", "4.15", "29.15", "---", "---", "---", "2",
            "Ocupado", "25.00", "Ocupado", "20.75", "Ocupado", "25.00", "Ocupado", "20.75",
            "18.5", "8.1", "13.2", "72%", "58%", "78%", "63%", "4", "124", "EB", "25.00"
        });
        
        JTable tabla = new JTable(modelo);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabla.setRowHeight(30);
        
        // Estilo oscuro
        tabla.setBackground(new Color(50, 50, 50));
        tabla.setForeground(new Color(240, 240, 240));
        tabla.setGridColor(new Color(100, 100, 100));
        tabla.setSelectionBackground(new Color(100, 149, 237));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setShowGrid(true);
        tabla.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        
        // Configurar anchos
        configurarAnchos(tabla);
        
        return tabla;
    }
    
    private void configurarAnchos(JTable tabla) {
        int[] anchos = {60, 80, 70, 70, 90, 70, 80, 90, 70, 80, 90, 50, 80, 80, 80, 80, 80, 80, 80, 80, 60, 60, 70, 60, 60, 60, 60, 60, 50, 60, 80};
        
        for (int i = 0; i < Math.min(anchos.length, tabla.getColumnCount()); i++) {
            TableColumn column = tabla.getColumnModel().getColumn(i);
            column.setPreferredWidth(anchos[i]);
            column.setMinWidth(40);
            column.setMaxWidth(120);
        }
    }
}

/**
 * Panel que crea los verdaderos encabezados jerárquicos como en el esquema,
 * donde cada grupo padre abarca múltiples columnas hijas.
 */
class EncabezadosJerarquicos extends JPanel {
    
    private static final int ALTURA_PADRE = 35;
    private static final int ALTURA_HIJO = 30;
    
    public EncabezadosJerarquicos() {
        setLayout(new BorderLayout());
        setBackground(new Color(40, 40, 40));
        setPreferredSize(new Dimension(0, ALTURA_PADRE + ALTURA_HIJO + 4));
        
        // Panel para encabezados padre (nivel superior)
        JPanel panelPadre = new JPanel();
        panelPadre.setLayout(new BoxLayout(panelPadre, BoxLayout.X_AXIS));
        panelPadre.setBackground(new Color(40, 40, 40));
        panelPadre.setPreferredSize(new Dimension(0, ALTURA_PADRE));
        
        // Panel para encabezados hijo (nivel inferior)
        JPanel panelHijo = new JPanel();
        panelHijo.setLayout(new BoxLayout(panelHijo, BoxLayout.X_AXIS));
        panelHijo.setBackground(new Color(45, 45, 45));
        panelHijo.setPreferredSize(new Dimension(0, ALTURA_HIJO));
        
        // DEFINIR LOS GRUPOS COMO EN EL ESQUEMA ORIGINAL
        definirEncabezadosPadre(panelPadre);
        definirEncabezadosHijo(panelHijo);
        
        add(panelPadre, BorderLayout.NORTH);
        add(panelHijo, BorderLayout.CENTER);
        
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
            BorderFactory.createEmptyBorder(1, 1, 1, 1)
        ));
    }
    
    private void definirEncabezadosPadre(JPanel panel) {
        // CONTROL (3 columnas)
        panel.add(crearLabelPadre("CONTROL", new Color(240, 240, 240), 210));
        
        // LLEGADA BARCO (2 columnas)
        panel.add(crearLabelPadre("LLEGADA BARCO", new Color(255, 200, 150), 160));
        
        // DESCARGA MUELLE 1 (3 columnas)
        panel.add(crearLabelPadre("DESCARGA MUELLE 1", new Color(150, 180, 220), 240));
        
        // DESCARGA MUELLE 2 (3 columnas)
        panel.add(crearLabelPadre("DESCARGA MUELLE 2", new Color(120, 150, 200), 240));
        
        // BAHÍA (1 columna)
        panel.add(crearLabelPadre("BAHÍA", new Color(200, 150, 200), 50));
        
        // ESTADOS MUELLES (4 columnas)
        panel.add(crearLabelPadre("ESTADOS MUELLES", new Color(150, 220, 150), 320));
        
        // ESTADOS GRÚAS (4 columnas)
        panel.add(crearLabelPadre("ESTADOS GRÚAS", new Color(255, 180, 180), 320));
        
        // T. PERMANENCIA (3 columnas)
        panel.add(crearLabelPadre("T. PERMANENCIA", new Color(255, 255, 180), 190));
        
        // UTILIZACIÓN (4 columnas)
        panel.add(crearLabelPadre("UTILIZACIÓN", new Color(180, 220, 255), 240));
        
        // BARCOS SISTEMA (1 columna)
        panel.add(crearLabelPadre("BARCOS SISTEMA", new Color(255, 220, 180), 60));
        
        // B1 (3 columnas)
        panel.add(crearLabelPadre("B1", new Color(255, 180, 120), 190));
    }
    
    private void definirEncabezadosHijo(JPanel panel) {
        // Control
        panel.add(crearLabelHijo("Fila", new Color(240, 240, 240), 60));
        panel.add(crearLabelHijo("Evento", new Color(240, 240, 240), 80));
        panel.add(crearLabelHijo("Reloj", new Color(240, 240, 240), 70));
        
        // Llegada Barco
        panel.add(crearLabelHijo("RND", new Color(255, 200, 150), 70));
        panel.add(crearLabelHijo("Próx.Lleg", new Color(255, 200, 150), 90));
        
        // Descarga Muelle 1
        panel.add(crearLabelHijo("RND", new Color(150, 180, 220), 70));
        panel.add(crearLabelHijo("T.Rest", new Color(150, 180, 220), 80));
        panel.add(crearLabelHijo("FinDesc", new Color(150, 180, 220), 90));
        
        // Descarga Muelle 2
        panel.add(crearLabelHijo("RND", new Color(120, 150, 200), 70));
        panel.add(crearLabelHijo("T.Rest", new Color(120, 150, 200), 80));
        panel.add(crearLabelHijo("FinDesc", new Color(120, 150, 200), 90));
        
        // Bahía
        panel.add(crearLabelHijo("Cola", new Color(200, 150, 200), 50));
        
        // Estados Muelles
        panel.add(crearLabelHijo("M1Est", new Color(150, 220, 150), 80));
        panel.add(crearLabelHijo("M1Inic", new Color(150, 220, 150), 80));
        panel.add(crearLabelHijo("M2Est", new Color(150, 220, 150), 80));
        panel.add(crearLabelHijo("M2Inic", new Color(150, 220, 150), 80));
        
        // Estados Grúas
        panel.add(crearLabelHijo("G1Est", new Color(255, 180, 180), 80));
        panel.add(crearLabelHijo("G1Inic", new Color(255, 180, 180), 80));
        panel.add(crearLabelHijo("G2Est", new Color(255, 180, 180), 80));
        panel.add(crearLabelHijo("G2Inic", new Color(255, 180, 180), 80));
        
        // T. Permanencia
        panel.add(crearLabelHijo("Máx", new Color(255, 255, 180), 60));
        panel.add(crearLabelHijo("Mín", new Color(255, 255, 180), 60));
        panel.add(crearLabelHijo("Media", new Color(255, 255, 180), 70));
        
        // Utilización
        panel.add(crearLabelHijo("M1%", new Color(180, 220, 255), 60));
        panel.add(crearLabelHijo("M2%", new Color(180, 220, 255), 60));
        panel.add(crearLabelHijo("G1%", new Color(180, 220, 255), 60));
        panel.add(crearLabelHijo("G2%", new Color(180, 220, 255), 60));
        
        // Barcos Sistema
        panel.add(crearLabelHijo("Total", new Color(255, 220, 180), 60));
        
        // B1
        panel.add(crearLabelHijo("ID", new Color(255, 180, 120), 50));
        panel.add(crearLabelHijo("Estado", new Color(255, 180, 120), 60));
        panel.add(crearLabelHijo("Ingreso", new Color(255, 180, 120), 80));
    }
    
    private JLabel crearLabelPadre(String texto, Color color, int ancho) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(color);
        label.setForeground(Color.BLACK);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
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
        label.setBackground(color.brighter());
        label.setForeground(Color.BLACK);
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLoweredBevelBorder(),
            BorderFactory.createEmptyBorder(2, 3, 2, 3)
        ));
        label.setPreferredSize(new Dimension(ancho, ALTURA_HIJO));
        label.setMinimumSize(new Dimension(ancho, ALTURA_HIJO));
        label.setMaximumSize(new Dimension(ancho, ALTURA_HIJO));
        return label;
    }
}