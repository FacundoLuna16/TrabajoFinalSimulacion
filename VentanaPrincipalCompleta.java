import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

/**
 * Aplicación completa con encabezados jerárquicos funcionando.
 * Incluye todas las clases necesarias en un solo archivo para facilitar la compilación.
 */
public class VentanaPrincipalCompleta extends JFrame {

    // =============== COLORES DEL TEMA OSCURO ===============
    private static final Color COLOR_FONDO_PRINCIPAL = new Color(25, 25, 25);
    private static final Color COLOR_FONDO_PANEL = new Color(35, 35, 35);
    private static final Color COLOR_FONDO_CARD = new Color(45, 45, 45);
    private static final Color COLOR_BORDE = new Color(70, 70, 70);
    private static final Color COLOR_BORDE_ACTIVO = new Color(100, 149, 237);
    private static final Color COLOR_TEXTO_PRINCIPAL = new Color(240, 240, 240);
    private static final Color COLOR_TEXTO_SECUNDARIO = new Color(180, 180, 180);
    private static final Color COLOR_ACENTO = new Color(100, 149, 237);
    private static final Color COLOR_EXITO = new Color(76, 175, 80);
    private static final Color COLOR_CAMPO_ACTIVO = new Color(55, 55, 55);

    // =============== COMPONENTES DE LA INTERFAZ ===============
    private JPanel panelPrincipal;
    private JPanel panelConfiguracion;
    private JTextField txtTiempoDescargaMin;
    private JTextField txtTiempoDescargaMax;
    private JTextField txtMediaLlegada;
    private JTextField txtDiasSimulacion;
    private JButton btnSimular;
    private JButton btnReset;

    // Panel de resultados
    private JPanel panelResultados;
    private JLabel lblTiempoPermanciaMin;
    private JLabel lblTiempoPermanciaMax;
    private JLabel lblTiempoPermanciaMedia;
    private JLabel[] lblUtilizacionMuelles;
    private JLabel[] lblUtilizacionGruas;

    // NUEVA TABLA CON ENCABEZADOS JERÁRQUICOS
    private TablaConEncabezadosReales tablaJerarquica;

    // Variables de configuración
    private int cantidadMuellesActual = 2;
    private int cantidadGruasActual = 2;
    private DecimalFormat formatoDecimal = new DecimalFormat("0.00");

    // Valores por defecto
    private final String[] VALORES_DEFAULT = {
            "0.5", "1.5", "1.25", "90"
    };

    public VentanaPrincipalCompleta() {
        inicializarComponentes();
        configurarVentana();
        configurarEventos();
    }

    private void inicializarComponentes() {
        panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBackground(COLOR_FONDO_PRINCIPAL);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        crearPanelConfiguracion();
        crearPanelResultados();
        crearTablaJerarquica();

        // Panel superior horizontal
        JPanel panelSuperior = new JPanel(new BorderLayout(15, 0));
        panelSuperior.setBackground(COLOR_FONDO_PRINCIPAL);
        panelSuperior.add(panelConfiguracion, BorderLayout.WEST);
        panelSuperior.add(panelResultados, BorderLayout.CENTER);

        // Agregar al panel principal
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(tablaJerarquica, BorderLayout.CENTER);
    }

    private void crearPanelConfiguracion() {
        panelConfiguracion = new JPanel();
        panelConfiguracion.setLayout(new BoxLayout(panelConfiguracion, BoxLayout.Y_AXIS));
        panelConfiguracion.setBackground(COLOR_FONDO_CARD);
        panelConfiguracion.setBorder(crearBordeCard("Configuración de Simulación"));
        panelConfiguracion.setPreferredSize(new Dimension(800, 150));

        JPanel gridParametros = new JPanel(new GridLayout(2, 3, 15, 10));
        gridParametros.setBackground(COLOR_FONDO_CARD);
        gridParametros.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        // Fila 1
        gridParametros.add(crearGrupoInput("Tiempo Descarga Mín", txtTiempoDescargaMin = crearTextField(VALORES_DEFAULT[0]), "días"));
        gridParametros.add(crearGrupoInput("Tiempo Descarga Máx", txtTiempoDescargaMax = crearTextField(VALORES_DEFAULT[1]), "días"));
        gridParametros.add(btnSimular = crearBotonPrimario("▶ Simular", COLOR_EXITO));

        // Fila 2
        gridParametros.add(crearGrupoInput("Media Llegada", txtMediaLlegada = crearTextField(VALORES_DEFAULT[2]), "días"));
        gridParametros.add(crearGrupoInput("Días Simulación", txtDiasSimulacion = crearTextField(VALORES_DEFAULT[3]), "días"));
        gridParametros.add(btnReset = crearBotonSecundario("🔄 Reset"));

        panelConfiguracion.add(gridParametros);
    }

    private void crearPanelResultados() {
        panelResultados = new JPanel(new BorderLayout(0, 10));
        panelResultados.setBackground(COLOR_FONDO_PRINCIPAL);

        // Panel de tiempo de permanencia
        JPanel cardTiempos = crearCardSection("Tiempo de Permanencia en Bahía");
        JPanel contenidoTiempos = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        contenidoTiempos.setBackground(COLOR_FONDO_CARD);

        contenidoTiempos.add(crearMetrica("Mínimo", lblTiempoPermanciaMin = crearLabelResultado("--"), "días"));
        contenidoTiempos.add(crearMetrica("Máximo", lblTiempoPermanciaMax = crearLabelResultado("--"), "días"));
        contenidoTiempos.add(crearMetrica("Promedio", lblTiempoPermanciaMedia = crearLabelResultado("--"), "días"));

        cardTiempos.add(contenidoTiempos);

        // Panel de utilizaciones
        JPanel panelUtilizaciones = crearCardSection("Grado de Utilización de Recursos");
        JPanel contenidoUtil = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 10));
        contenidoUtil.setBackground(COLOR_FONDO_CARD);

        lblUtilizacionMuelles = new JLabel[cantidadMuellesActual];
        lblUtilizacionGruas = new JLabel[cantidadGruasActual];

        for (int i = 0; i < cantidadMuellesActual; i++) {
            contenidoUtil.add(crearMetrica("Muelle " + (i + 1), lblUtilizacionMuelles[i] = crearLabelResultado("--"), "%"));
        }

        for (int i = 0; i < cantidadGruasActual; i++) {
            contenidoUtil.add(crearMetrica("Grúa " + (i + 1), lblUtilizacionGruas[i] = crearLabelResultado("--"), "%"));
        }

        panelUtilizaciones.add(contenidoUtil);

        panelResultados.add(cardTiempos, BorderLayout.NORTH);
        panelResultados.add(panelUtilizaciones, BorderLayout.CENTER);
    }

    private void crearTablaJerarquica() {
        tablaJerarquica = new TablaConEncabezadosReales();
        tablaJerarquica.setPreferredSize(new Dimension(1200, 450));
    }

    // =============== MÉTODOS AUXILIARES DE UI ===============

    private javax.swing.border.Border crearBordeCard(String titulo) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(COLOR_BORDE, 1),
                        titulo,
                        javax.swing.border.TitledBorder.LEFT,
                        javax.swing.border.TitledBorder.TOP,
                        new Font(Font.SANS_SERIF, Font.BOLD, 14),
                        COLOR_ACENTO
                ),
                BorderFactory.createEmptyBorder(5, 10, 10, 10)
        );
    }

    private JPanel crearCardSection(String titulo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO_CARD);
        panel.setBorder(crearBordeCard(titulo));
        return panel;
    }

    private JPanel crearGrupoInput(String label, JTextField campo, String unidad) {
        JPanel panel = new JPanel(new BorderLayout(5, 3));
        panel.setBackground(COLOR_FONDO_CARD);

        JLabel lblTop = new JLabel(label, SwingConstants.CENTER);
        lblTop.setForeground(COLOR_TEXTO_SECUNDARIO);
        lblTop.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

        JLabel lblUnidad = new JLabel(unidad, SwingConstants.CENTER);
        lblUnidad.setForeground(COLOR_TEXTO_SECUNDARIO);
        lblUnidad.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 11));

        panel.add(lblTop, BorderLayout.NORTH);
        panel.add(campo, BorderLayout.CENTER);
        panel.add(lblUnidad, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearMetrica(String label, JLabel valor, String unidad) {
        JPanel panel = new JPanel(new BorderLayout(3, 2));
        panel.setBackground(COLOR_FONDO_CARD);

        JLabel lblTop = new JLabel(label, SwingConstants.CENTER);
        lblTop.setForeground(COLOR_TEXTO_SECUNDARIO);
        lblTop.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

        JLabel lblUnidad = new JLabel(unidad, SwingConstants.CENTER);
        lblUnidad.setForeground(COLOR_TEXTO_SECUNDARIO);
        lblUnidad.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 11));

        panel.add(lblTop, BorderLayout.NORTH);
        panel.add(valor, BorderLayout.CENTER);
        panel.add(lblUnidad, BorderLayout.SOUTH);

        return panel;
    }

    private JTextField crearTextField(String valorInicial) {
        JTextField field = new JTextField(valorInicial);
        field.setBackground(COLOR_CAMPO_ACTIVO);
        field.setForeground(COLOR_TEXTO_PRINCIPAL);
        field.setCaretColor(COLOR_ACENTO);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        field.setHorizontalAlignment(SwingConstants.CENTER);
        field.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(80, 32));
        return field;
    }

    private JButton crearBotonPrimario(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(6, 12, 6, 11)
        ));
        boton.setPreferredSize(new Dimension(90, 32));
        boton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private JButton crearBotonSecundario(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(COLOR_FONDO_PANEL);
        boton.setForeground(COLOR_TEXTO_PRINCIPAL);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1),
                BorderFactory.createEmptyBorder(9, 19, 9, 19)
        ));
        boton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private JLabel crearLabelResultado(String texto) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setForeground(COLOR_ACENTO);
        label.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        label.setOpaque(true);
        label.setBackground(COLOR_FONDO_PANEL);
        label.setPreferredSize(new Dimension(120, 36));
        return label;
    }

    private void configurarVentana() {
        setTitle("🎯 SIMULACIÓN PUERTO - ENCABEZADOS JERÁRQUICOS INTEGRADOS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panelPrincipal);
        setSize(1600, 1000);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1400, 800));
        getContentPane().setBackground(COLOR_FONDO_PRINCIPAL);
    }

    private void configurarEventos() {
        btnSimular.addActionListener(e -> ejecutarSimulacionDemo());
        btnReset.addActionListener(e -> resetearCampos());
    }

    private void ejecutarSimulacionDemo() {
        List<Object[]> filasDemo = new ArrayList<>();

        // Generar datos de simulación más realistas
        for (int fila = 1; fila <= 5; fila++) {
            Object[] datos = new Object[]{
                    // Control (3)
                    String.valueOf(fila),
                    (fila % 2 == 1) ? "LL" : "FD" + (fila % 2 + 1),
                    String.format("%.2f", fila * 12.5),

                    // Llegada Barco (2)
                    (fila % 2 == 1) ? String.format("%.4f", 0.3 + fila * 0.1) : "---",
                    String.format("%.2f", 25.0 + fila * 12.5),

                    // Descarga Muelle 1 (3)
                    (fila % 3 == 1) ? String.format("%.4f", 0.7 + fila * 0.05) : "---",
                    (fila % 3 == 1) ? String.format("%.2f", 8.25 - fila * 0.5) : "---",
                    (fila % 3 == 1) ? String.format("%.2f", 20.75 + fila * 5) : "---",

                    // Descarga Muelle 2 (3)
                    (fila % 3 == 2) ? String.format("%.4f", 0.4 + fila * 0.03) : "---",
                    (fila % 3 == 2) ? String.format("%.2f", 6.30 + fila * 0.8) : "---",
                    (fila % 3 == 2) ? String.format("%.2f", 27.05 + fila * 3) : "---",

                    // Bahía (1)
                    String.valueOf(Math.max(0, 3 - fila % 3)),

                    // Estados Muelles (4)
                    (fila % 2 == 1) ? "Ocupado" : "Libre",
                    (fila % 2 == 1) ? String.format("%.2f", fila * 12.5) : "---",
                    (fila % 2 == 0) ? "Ocupado" : "Libre",
                    (fila % 2 == 0) ? String.format("%.2f", fila * 10.0) : "---",

                    // Estados Grúas (4)
                    (fila % 2 == 1) ? "Ocupado" : "Libre",
                    (fila % 2 == 1) ? String.format("%.2f", fila * 12.5) : "---",
                    (fila % 2 == 0) ? "Ocupado" : "Libre",
                    (fila % 2 == 0) ? String.format("%.2f", fila * 10.0) : "---",

                    // T. Permanencia (5)
                    String.format("%.1f", 15.2 + fila * 0.8),
                    String.format("%.1f", 8.1),
                    String.format("%.1f", 45.8 + fila * 2.1),
                    String.valueOf(3 + fila % 2),
                    String.format("%.1f", 11.8 + fila * 0.4),

                    // Utilización (8)
                    String.format("%.1f", 25.4 + fila * 1.2),
                    String.format("%.0f%%", 68 + fila * 2),
                    String.format("%.1f", 18.2 + fila * 1.8),
                    String.format("%.0f%%", 45 + fila * 3),
                    String.format("%.1f", 32.1 + fila * 1.5),
                    String.format("%.0f%%", 72 + fila),
                    String.format("%.1f", 28.7 + fila * 1.1),
                    String.format("%.0f%%", 51 + fila * 2),

                    // Barcos Sistema (1)
                    String.valueOf(Math.min(4, 2 + fila % 3)),

                    // Barcos dinámicos B1 (3)
                    String.valueOf(120 + fila),
                    (fila % 2 == 1) ? "EB" : "SD",
                    String.format("%.2f", fila * 12.5),

                    // Barcos dinámicos B2 (3)
                    fila > 1 ? String.valueOf(120 + fila + 10) : "---",
                    fila > 1 ? ((fila % 2 == 0) ? "EB" : "SD") : "---",
                    fila > 1 ? String.format("%.2f", (fila - 1) * 12.5) : "---"
            };
            filasDemo.add(datos);
        }

        // USAR LA TABLA CON ENCABEZADOS JERÁRQUICOS
        int maxBarcosEnSistema = 2;
        tablaJerarquica.poblarDatos(filasDemo, maxBarcosEnSistema);

        // Actualizar resultados
        lblTiempoPermanciaMin.setText("8.10");
        lblTiempoPermanciaMax.setText("18.50");
        lblTiempoPermanciaMedia.setText("13.20");

        lblUtilizacionMuelles[0].setText("72.0%");
        lblUtilizacionMuelles[1].setText("58.0%");
        lblUtilizacionGruas[0].setText("78.0%");
        lblUtilizacionGruas[1].setText("63.0%");

        JOptionPane.showMessageDialog(this,
                "🎯 ¡ENCABEZADOS JERÁRQUICOS INTEGRADOS EXITOSAMENTE!\n\n" +
                        "✅ Grupos padre abarcan múltiples columnas\n" +
                        "✅ Colores diferenciados por sección\n" +
                        "✅ Scroll lateral funcionando\n" +
                        "✅ Integración completa con la aplicación\n" +
                        "✅ Estructura real como el esquema diseñado\n\n" +
                        "La tabla ahora muestra los encabezados jerárquicos\n" +
                        "tal como fueron diseñados originalmente.",
                "¡Integración Completa!",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void resetearCampos() {
        txtTiempoDescargaMin.setText(VALORES_DEFAULT[0]);
        txtTiempoDescargaMax.setText(VALORES_DEFAULT[1]);
        txtMediaLlegada.setText(VALORES_DEFAULT[2]);
        txtDiasSimulacion.setText(VALORES_DEFAULT[3]);

        lblTiempoPermanciaMin.setText("--");
        lblTiempoPermanciaMax.setText("--");
        lblTiempoPermanciaMedia.setText("--");

        for (JLabel label : lblUtilizacionMuelles) {
            if (label != null) label.setText("--");
        }
        for (JLabel label : lblUtilizacionGruas) {
            if (label != null) label.setText("--");
        }

        tablaJerarquica.getModelo().setRowCount(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            new VentanaPrincipalCompleta().setVisible(true);

            System.out.println("🎯 ¡APLICACIÓN CON ENCABEZADOS JERÁRQUICOS LISTA!");
            System.out.println("📊 Presiona 'Simular' para ver la integración completa");
            System.out.println("✨ Los encabezados jerárquicos están completamente integrados");
        });
    }
}

// =============== CLASES DE ENCABEZADOS JERÁRQUICOS ===============

/**
 * Tabla que integra los encabezados jerárquicos reales con los datos de simulación
 */
class TablaConEncabezadosReales extends JPanel {
    
    private JTable tabla;
    private DefaultTableModel modelo;
    private JScrollPane scrollPane;
    private EncabezadosJerarquicosReales encabezados;
    
    private static final Color COLOR_FONDO_TABLA = new Color(45, 45, 45);
    private static final Color COLOR_TEXTO = new Color(240, 240, 240);
    private static final Color COLOR_SELECCION = new Color(100, 149, 237);
    private static final Color COLOR_GRID = new Color(70, 70, 70);
    
    public TablaConEncabezadosReales() {
        setLayout(new BorderLayout());
        setBackground(new Color(25, 25, 25));
        inicializar();
    }
    
    private void inicializar() {
        JPanel panelCompleto = new JPanel(new BorderLayout());
        panelCompleto.setBackground(new Color(25, 25, 25));
        
        encabezados = new EncabezadosJerarquicosReales();
        panelCompleto.add(encabezados, BorderLayout.NORTH);
        
        crearTabla();
        panelCompleto.add(tabla, BorderLayout.CENTER);
        
        scrollPane = new JScrollPane(panelCompleto);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBackground(COLOR_FONDO_TABLA);
        scrollPane.getViewport().setBackground(COLOR_FONDO_TABLA);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_GRID, 1));
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void crearTabla() {
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabla = new JTable(modelo);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabla.setRowHeight(28);
        tabla.setTableHeader(null);
        
        tabla.setBackground(COLOR_FONDO_TABLA);
        tabla.setForeground(COLOR_TEXTO);
        tabla.setGridColor(COLOR_GRID);
        tabla.setSelectionBackground(COLOR_SELECCION);
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setShowGrid(true);
        tabla.setIntercellSpacing(new Dimension(1, 1));
        tabla.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    public void actualizarColumnasSegunBarcos(int maxBarcosEnSistema) {
        encabezados.actualizarParaBarcos(maxBarcosEnSistema);
        
        String[] columnasCompletas = generarNombresColumnas(maxBarcosEnSistema);
        modelo.setColumnIdentifiers(columnasCompletas);
        
        ajustarAnchoColumnas(maxBarcosEnSistema);
    }
    
    private String[] generarNombresColumnas(int maxBarcosEnSistema) {
        List<String> columnas = new ArrayList<>();
        
        String[] columnasBase = {
            "Fila", "Evento", "Reloj", "RND", "Próx. Llegada", "RND", "T. Restante", "Fin Descarga",
            "RND", "T. Restante", "Fin Descarga", "Cola", "M1 Estado", "M1 Inicio Ocup.", "M2 Estado", "M2 Inicio Ocup.",
            "G1 Estado", "G1 Inicio Ocup.", "G2 Estado", "G2 Inicio Ocup.", "Máx", "Mín", "Acum.", "Cant. Barcos", "Media",
            "M1 Ac. T.O.", "M1 %", "M2 Ac. T.O.", "M2 %", "G1 Ac. T.O.", "G1 %", "G2 Ac. T.O.", "G2 %", "Total"
        };
        
        for (String col : columnasBase) {
            columnas.add(col);
        }
        
        for (int i = 1; i <= maxBarcosEnSistema; i++) {
            columnas.add("B" + i + "_ID");
            columnas.add("B" + i + "_Estado");
            columnas.add("B" + i + "_Ingreso");
        }
        
        return columnas.toArray(new String[0]);
    }
    
    private void ajustarAnchoColumnas(int maxBarcosEnSistema) {
        int[] anchosBase = {50, 80, 70, 70, 90, 70, 80, 90, 70, 80, 90, 45, 80, 80, 80, 80, 80, 80, 80, 80, 60, 60, 80, 60, 70, 70, 50, 70, 50, 70, 50, 70, 50, 60};
        
        for (int i = 0; i < anchosBase.length && i < tabla.getColumnCount(); i++) {
            TableColumn column = tabla.getColumnModel().getColumn(i);
            column.setPreferredWidth(anchosBase[i]);
            column.setMinWidth(anchosBase[i]);
            column.setMaxWidth(anchosBase[i] + 20);
        }
        
        int indiceInicial = anchosBase.length;
        for (int i = 0; i < maxBarcosEnSistema; i++) {
            int indiceBase = indiceInicial + (i * 3);
            
            if (indiceBase < tabla.getColumnCount()) {
                TableColumn colID = tabla.getColumnModel().getColumn(indiceBase);
                colID.setPreferredWidth(50);
                colID.setMinWidth(50);
                colID.setMaxWidth(70);
            }
            
            if (indiceBase + 1 < tabla.getColumnCount()) {
                TableColumn colEstado = tabla.getColumnModel().getColumn(indiceBase + 1);
                colEstado.setPreferredWidth(60);
                colEstado.setMinWidth(60);
                colEstado.setMaxWidth(80);
            }
            
            if (indiceBase + 2 < tabla.getColumnCount()) {
                TableColumn colIngreso = tabla.getColumnModel().getColumn(indiceBase + 2);
                colIngreso.setPreferredWidth(80);
                colIngreso.setMinWidth(80);
                colIngreso.setMaxWidth(100);
            }
        }
    }
    
    public void poblarDatos(List<Object[]> filas, int maxBarcosEnSistema) {
        modelo.setRowCount(0);
        actualizarColumnasSegunBarcos(maxBarcosEnSistema);
        
        for (Object[] fila : filas) {
            modelo.addRow(fila);
        }
        
        actualizarVista();
    }
    
    public void actualizarVista() {
        SwingUtilities.invokeLater(() -> {
            modelo.fireTableDataChanged();
            tabla.revalidate();
            tabla.repaint();
            encabezados.revalidate();
            encabezados.repaint();
        });
    }
    
    public JTable getTabla() { return tabla; }
    public DefaultTableModel getModelo() { return modelo; }
    public JScrollPane getScrollPane() { return scrollPane; }
}

/**
 * Clase que crea encabezados jerárquicos reales donde los grupos padre
 * abarcan múltiples columnas hijas
 */
class EncabezadosJerarquicosReales extends JPanel {
    
    private static final int ALTURA_PADRE = 30;
    private static final int ALTURA_HIJO = 25;
    
    private int maxBarcosEnSistema = 0;
    
    public EncabezadosJerarquicosReales() {
        setLayout(new BorderLayout());
        setBackground(new Color(40, 40, 40));
        inicializar(0);
    }
    
    public void actualizarParaBarcos(int maxBarcosEnSistema) {
        if (this.maxBarcosEnSistema != maxBarcosEnSistema) {
            this.maxBarcosEnSistema = maxBarcosEnSistema;
            removeAll();
            inicializar(maxBarcosEnSistema);
            revalidate();
            repaint();
        }
    }
    
    private void inicializar(int maxBarcosEnSistema) {
        setPreferredSize(new Dimension(calcularAnchoTotal(maxBarcosEnSistema), ALTURA_PADRE + ALTURA_HIJO + 4));
        
        JPanel panelPadre = new JPanel();
        panelPadre.setLayout(new BoxLayout(panelPadre, BoxLayout.X_AXIS));
        panelPadre.setBackground(new Color(40, 40, 40));
        panelPadre.setPreferredSize(new Dimension(calcularAnchoTotal(maxBarcosEnSistema), ALTURA_PADRE));
        
        JPanel panelHijo = new JPanel();
        panelHijo.setLayout(new BoxLayout(panelHijo, BoxLayout.X_AXIS));
        panelHijo.setBackground(new Color(45, 45, 45));
        panelHijo.setPreferredSize(new Dimension(calcularAnchoTotal(maxBarcosEnSistema), ALTURA_HIJO));
        
        crearEncabezadosPadre(panelPadre, maxBarcosEnSistema);
        crearEncabezadosHijo(panelHijo, maxBarcosEnSistema);
        
        add(panelPadre, BorderLayout.NORTH);
        add(panelHijo, BorderLayout.CENTER);
        
        setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
    }
    
    private int calcularAnchoTotal(int maxBarcosEnSistema) {
        int anchoBase = 50 + 80 + 70 + 70 + 90 + 70 + 80 + 90 + 70 + 80 + 90 + 45 + 
                       80 + 80 + 80 + 80 + 80 + 80 + 80 + 80 + 60 + 60 + 80 + 60 + 70 +
                       70 + 50 + 70 + 50 + 70 + 50 + 70 + 50 + 60;
        
        int anchoBarcos = maxBarcosEnSistema * (50 + 60 + 80);
        return anchoBase + anchoBarcos;
    }
    
    private void crearEncabezadosPadre(JPanel panel, int maxBarcosEnSistema) {
        panel.add(crearLabelPadre("CONTROL", new Color(240, 240, 240), 200));
        panel.add(crearLabelPadre("LLEGADA BARCO", new Color(255, 200, 150), 160));
        panel.add(crearLabelPadre("DESCARGA MUELLE 1", new Color(150, 180, 220), 240));
        panel.add(crearLabelPadre("DESCARGA MUELLE 2", new Color(120, 150, 200), 240));
        panel.add(crearLabelPadre("BAHÍA", new Color(200, 150, 200), 45));
        panel.add(crearLabelPadre("ESTADOS MUELLES", new Color(150, 220, 150), 320));
        panel.add(crearLabelPadre("ESTADOS GRÚAS", new Color(255, 180, 180), 320));
        panel.add(crearLabelPadre("T. PERMANENCIA", new Color(255, 255, 180), 330));
        panel.add(crearLabelPadre("UTILIZACIÓN", new Color(180, 220, 255), 480));
        panel.add(crearLabelPadre("BARCOS SISTEMA", new Color(255, 220, 180), 60));
        
        for (int i = 1; i <= maxBarcosEnSistema; i++) {
            Color colorBarco = new Color(255, 180 - (i * 10), 120 - (i * 5));
            panel.add(crearLabelPadre("B" + i, colorBarco, 190));
        }
    }
    
    private void crearEncabezadosHijo(JPanel panel, int maxBarcosEnSistema) {
        String[] columnasHijas = {
            "Fila", "Evento", "Reloj", "RND", "Próx. Llegada", "RND", "T. Restante", "Fin Descarga",
            "RND", "T. Restante", "Fin Descarga", "Cola", "M1 Estado", "M1 Inicio Ocup.", "M2 Estado", "M2 Inicio Ocup.",
            "G1 Estado", "G1 Inicio Ocup.", "G2 Estado", "G2 Inicio Ocup.", "Máx", "Mín", "Acum.", "Cant. Barcos", "Media",
            "M1 Ac. T.O.", "M1 %", "M2 Ac. T.O.", "M2 %", "G1 Ac. T.O.", "G1 %", "G2 Ac. T.O.", "G2 %", "Total"
        };
        
        Color[] coloresHijas = {
            new Color(240, 240, 240), new Color(240, 240, 240), new Color(240, 240, 240),
            new Color(255, 200, 150), new Color(255, 200, 150),
            new Color(150, 180, 220), new Color(150, 180, 220), new Color(150, 180, 220),
            new Color(120, 150, 200), new Color(120, 150, 200), new Color(120, 150, 200),
            new Color(200, 150, 200),
            new Color(150, 220, 150), new Color(150, 220, 150), new Color(150, 220, 150), new Color(150, 220, 150),
            new Color(255, 180, 180), new Color(255, 180, 180), new Color(255, 180, 180), new Color(255, 180, 180),
            new Color(255, 255, 180), new Color(255, 255, 180), new Color(255, 255, 180), new Color(255, 255, 180), new Color(255, 255, 180),
            new Color(180, 220, 255), new Color(180, 220, 255), new Color(180, 220, 255), new Color(180, 220, 255),
            new Color(180, 220, 255), new Color(180, 220, 255), new Color(180, 220, 255), new Color(180, 220, 255),
            new Color(255, 220, 180)
        };
        
        int[] anchosHijas = {50, 80, 70, 70, 90, 70, 80, 90, 70, 80, 90, 45, 80, 80, 80, 80, 80, 80, 80, 80, 60, 60, 80, 60, 70, 70, 50, 70, 50, 70, 50, 70, 50, 60};
        
        for (int i = 0; i < columnasHijas.length; i++) {
            panel.add(crearLabelHijo(columnasHijas[i], coloresHijas[i], anchosHijas[i]));
        }
        
        for (int i = 1; i <= maxBarcosEnSistema; i++) {
            Color colorBarco = new Color(255, 180 - (i * 10), 120 - (i * 5));
            panel.add(crearLabelHijo("ID", colorBarco, 50));
            panel.add(crearLabelHijo("Estado", colorBarco, 60));
            panel.add(crearLabelHijo("T. Ingreso", colorBarco, 80));
        }
    }
    
    private JLabel crearLabelPadre(String texto, Color color, int ancho) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(color);
        label.setForeground(Color.BLACK);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
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
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
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