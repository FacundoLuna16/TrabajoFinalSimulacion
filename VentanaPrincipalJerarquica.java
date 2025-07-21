import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

// Importar las clases de encabezados jerárquicos
import src.main.java.com.facu.simulation.ui.TablaConEncabezadosReales;

/**
 * Versión de VentanaPrincipal que integra los encabezados jerárquicos reales
 * sin dependencias externas para poder compilar y probar.
 */
public class VentanaPrincipalJerarquica extends JFrame {

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
    private JTextField txtMostrarDesde;
    private JTextField txtMostrarHasta;
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
            "0.5", "1.5", "1.25", "90", "0", "15"
    };

    public VentanaPrincipalJerarquica() {
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
        crearTablaJerarquica(); // NUEVA tabla con encabezados jerárquicos

        // Panel superior horizontal
        JPanel panelSuperior = new JPanel(new BorderLayout(15, 0));
        panelSuperior.setBackground(COLOR_FONDO_PRINCIPAL);
        panelSuperior.add(panelConfiguracion, BorderLayout.WEST);
        panelSuperior.add(panelResultados, BorderLayout.CENTER);

        // Agregar al panel principal
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(tablaJerarquica, BorderLayout.CENTER); // USAR la nueva tabla
    }

    private void crearPanelConfiguracion() {
        panelConfiguracion = new JPanel();
        panelConfiguracion.setLayout(new BoxLayout(panelConfiguracion, BoxLayout.Y_AXIS));
        panelConfiguracion.setBackground(COLOR_FONDO_CARD);
        panelConfiguracion.setBorder(crearBordeCard("Configuración de Simulación"));
        panelConfiguracion.setPreferredSize(new Dimension(800, 180));

        // Panel de parámetros principales
        JPanel panelParametros = crearCardSection("Parámetros de Simulación");
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

        panelParametros.add(gridParametros);
        panelConfiguracion.add(panelParametros);
    }

    private void crearPanelResultados() {
        panelResultados = new JPanel(new BorderLayout(0, 10));
        panelResultados.setBackground(COLOR_FONDO_PRINCIPAL);

        // Panel de tiempo de permanencia
        JPanel cardTiempos = crearCardSection("Tiempo de Permanencia en Bahía");
        JPanel contenidoTiempos = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        contenidoTiempos.setBackground(COLOR_FONDO_CARD);
        contenidoTiempos.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        contenidoTiempos.add(crearMetrica("Mínimo", lblTiempoPermanciaMin = crearLabelResultado("--"), "días"));
        contenidoTiempos.add(crearSeparadorVertical());
        contenidoTiempos.add(crearMetrica("Máximo", lblTiempoPermanciaMax = crearLabelResultado("--"), "días"));
        contenidoTiempos.add(crearSeparadorVertical());
        contenidoTiempos.add(crearMetrica("Promedio", lblTiempoPermanciaMedia = crearLabelResultado("--"), "días"));

        cardTiempos.add(contenidoTiempos);

        // Panel de utilizaciones
        crearPanelUtilizaciones();

        panelResultados.add(cardTiempos, BorderLayout.NORTH);
        panelResultados.add(panelResultados, BorderLayout.CENTER);
    }

    private void crearPanelUtilizaciones() {
        JPanel panelUtilizaciones = crearCardSection("Grado de Utilización de Recursos");
        JPanel contenidoUtil = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 10));
        contenidoUtil.setBackground(COLOR_FONDO_CARD);
        contenidoUtil.setBorder(BorderFactory.createEmptyBorder(8, 10, 10, 10));

        lblUtilizacionMuelles = new JLabel[cantidadMuellesActual];
        lblUtilizacionGruas = new JLabel[cantidadGruasActual];

        // Crear métricas para muelles
        for (int i = 0; i < cantidadMuellesActual; i++) {
            contenidoUtil.add(crearMetrica("Muelle " + (i + 1),
                    lblUtilizacionMuelles[i] = crearLabelResultado("--"), "%"));
            if (i < cantidadMuellesActual - 1) contenidoUtil.add(crearSeparadorVertical());
        }

        contenidoUtil.add(crearSeparadorVertical());

        // Crear métricas para grúas
        for (int i = 0; i < cantidadGruasActual; i++) {
            contenidoUtil.add(crearMetrica("Grúa " + (i + 1),
                    lblUtilizacionGruas[i] = crearLabelResultado("--"), "%"));
            if (i < cantidadGruasActual - 1) contenidoUtil.add(crearSeparadorVertical());
        }

        panelUtilizaciones.add(contenidoUtil);
        panelResultados.add(panelUtilizaciones, BorderLayout.CENTER);
    }

    /**
     * CREA LA NUEVA TABLA CON ENCABEZADOS JERÁRQUICOS
     */
    private void crearTablaJerarquica() {
        // USAR LA NUEVA TABLA CON ENCABEZADOS JERÁRQUICOS REALES
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

    private Component crearSeparadorVertical() {
        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setBackground(COLOR_BORDE);
        sep.setForeground(COLOR_BORDE);
        sep.setPreferredSize(new Dimension(1, 40));
        return sep;
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
        setTitle("Simulación Puerto - Con Encabezados Jerárquicos");
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

    /**
     * SIMULACIÓN DEMO para mostrar los encabezados jerárquicos funcionando
     */
    private void ejecutarSimulacionDemo() {
        // Simular datos de ejemplo para mostrar los encabezados jerárquicos
        List<Object[]> filasDemo = new ArrayList<>();

        // Fila 1
        filasDemo.add(new Object[]{
                // Control (3)
                "1", "LL", "12.50",
                // Llegada (2)
                "0.3421", "25.00",
                // Descarga M1 (3)
                "0.7834", "8.25", "20.75",
                // Descarga M2 (3)
                "---", "---", "---",
                // Bahía (1)
                "2",
                // Estados Muelles (4)
                "Ocupado", "12.50", "Libre", "---",
                // Estados Grúas (4)
                "Ocupado", "12.50", "Libre", "---",
                // T. Permanencia (5)
                "15.2", "8.1", "45.8", "3", "11.8",
                // Utilización (8)
                "25.4", "68%", "18.2", "45%", "32.1", "72%", "28.7", "51%",
                // Sistema (1)
                "3",
                // Barco B1 (3)
                "123", "EB", "12.50",
                // Barco B2 (3)
                "124", "EB", "25.00"
        });

        // Fila 2
        filasDemo.add(new Object[]{
                "2", "FD1", "20.75", "---", "25.00", "---", "---", "---",
                "0.4521", "6.30", "27.05", "1", "Libre", "---", "Ocupado", "20.75",
                "Libre", "---", "Ocupado", "20.75", "15.2", "8.1", "45.8", "3", "12.1",
                "25.4", "68%", "26.4", "58%", "32.1", "72%", "36.9", "63%", "3",
                "123", "SD", "12.50", "124", "EB", "25.00"
        });

        // Fila 3
        filasDemo.add(new Object[]{
                "3", "LL", "25.00", "0.6789", "37.80", "0.2341", "4.15", "29.15",
                "---", "---", "---", "2", "Ocupado", "25.00", "Ocupado", "20.75",
                "Ocupado", "25.00", "Ocupado", "20.75", "18.5", "8.1", "52.3", "4", "13.2",
                "29.6", "72%", "26.4", "58%", "38.7", "78%", "36.9", "63%", "4",
                "125", "EB", "25.00", "124", "SD", "25.00"
        });

        // POBLAR LA TABLA CON ENCABEZADOS JERÁRQUICOS
        int maxBarcosEnSistema = 2; // B1 y B2
        tablaJerarquica.poblarDatos(filasDemo, maxBarcosEnSistema);

        // Actualizar resultados de ejemplo
        lblTiempoPermanciaMin.setText("8.10");
        lblTiempoPermanciaMax.setText("18.50");
        lblTiempoPermanciaMedia.setText("13.20");

        lblUtilizacionMuelles[0].setText("72.0%");
        lblUtilizacionMuelles[1].setText("58.0%");
        lblUtilizacionGruas[0].setText("78.0%");
        lblUtilizacionGruas[1].setText("63.0%");

        JOptionPane.showMessageDialog(this,
                "¡Simulación completada!\n\n" +
                        "🎯 Observe los encabezados jerárquicos funcionando:\n" +
                        "• Grupos padre abarcan múltiples columnas\n" +
                        "• Colores diferenciados por sección\n" +
                        "• Scroll lateral para ver todas las columnas\n" +
                        "• Estructura exacta como el esquema diseñado",
                "Encabezados Jerárquicos - Demo",
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

        // Limpiar tabla
        tablaJerarquica.getModelo().setRowCount(0);
    }

    // =============== MAIN PARA PRUEBA ===============
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            new VentanaPrincipalJerarquica().setVisible(true);

            System.out.println("✅ VentanaPrincipal con encabezados jerárquicos funcionando!");
            System.out.println("🎯 Presiona 'Simular' para ver los encabezados en acción");
        });
    }
}