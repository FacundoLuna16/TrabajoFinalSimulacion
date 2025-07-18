package com.facu.simulation.ui;

import com.facu.simulation.engine.ConfiguracionSimulacion;
import com.facu.simulation.engine.Simulador;
import com.facu.simulation.dto.FilaVectorDTO;
import com.facu.simulation.dto.ResultadosSimulacionDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

/**
 * Ventana principal modernizada con tema oscuro mejorado.
 * Mantiene toda la funcionalidad original con diseño moderno.
 */
public class VentanaPrincipal extends JFrame {

    // =============== COLORES DEL TEMA OSCURO MODERNO ===============
    private static final Color COLOR_FONDO_PRINCIPAL = new Color(25, 25, 25);
    private static final Color COLOR_FONDO_PANEL = new Color(35, 35, 35);
    private static final Color COLOR_FONDO_CARD = new Color(45, 45, 45);
    private static final Color COLOR_BORDE = new Color(70, 70, 70);
    private static final Color COLOR_BORDE_ACTIVO = new Color(100, 149, 237);
    private static final Color COLOR_TEXTO_PRINCIPAL = new Color(240, 240, 240);
    private static final Color COLOR_TEXTO_SECUNDARIO = new Color(180, 180, 180);
    private static final Color COLOR_ACENTO = new Color(100, 149, 237);
    private static final Color COLOR_EXITO = new Color(76, 175, 80);
    private static final Color COLOR_ALERTA = new Color(255, 193, 7);
    private static final Color COLOR_ERROR = new Color(244, 67, 54);
    private static final Color COLOR_CAMPO_ACTIVO = new Color(55, 55, 55);
    private static final Color COLOR_CAMPO_INACTIVO = new Color(40, 40, 40);

    // =============== COMPONENTES DE LA INTERFAZ ===============

    // Panel principal
    private JPanel panelPrincipal;

    // Panel de configuración superior
    private JPanel panelConfiguracion;
    private JTextField txtTiempoDescargaMin;
    private JTextField txtTiempoDescargaMax;
    private JTextField txtMostrarDesde;
    private JTextField txtMostrarHasta;
    private JTextField txtMediaLlegada;
    private JTextField txtDiasSimulacion;
    private JTextField txtMostrarFilaDesde;
    private JTextField txtMostrarFilaHasta;
    private JCheckBox chkFiltrarPorFila;
    private JButton btnSimular;
    private JButton btnReset;

    // Panel de resultados (estadísticas)
    private JPanel panelResultados;
    private JLabel lblTiempoPermanciaMin;
    private JLabel lblTiempoPermanciaMax;
    private JLabel lblTiempoPermanciaMedia;

    // Panel de utilizaciones
    private JPanel panelUtilizaciones;
    private JLabel[] lblUtilizacionMuelles;
    private JLabel[] lblUtilizacionGruas;

    // Tabla de vectores de estado mejorada
    private TablaMejorada tablaMejorada;
    private JTable tablaVectorEstado;
    private DefaultTableModel modeloTabla;
    private GeneradorColumnasTabla generadorColumnas;

    // Variables de configuración actual (fijos para 2 muelles y 2 grúas)
    private int cantidadMuellesActual = 2;
    private int cantidadGruasActual = 2;

    // Valores por defecto para reset
    private final String[] VALORES_DEFAULT = {
            "4",  // TiempoDescargaMin
            "6",  // TiempoDescargaMax
            "1.25",  // MediaLlegada
            "90",   // DiasSimulacion
            "0",    // MostrarDesde
            "15",   // MostrarHasta
            "0",    // MostrarFilaDesde
            "80"    // MostrarFilaHasta
    };

    // Formato para números decimales
    private DecimalFormat formatoDecimal = new DecimalFormat("#.##");

    // =============== CONSTRUCTOR ===============

    public VentanaPrincipal() {
        generadorColumnas = new GeneradorColumnasTabla();
        configurarLookAndFeel();
        inicializarComponentes();
        configurarVentana();
        configurarEventos();
    }

    /**
     * Configura el look and feel para el tema oscuro.
     */
    private void configurarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());

            // Personalizar scroll bars
            UIManager.put("ScrollBar.width", 16);
            UIManager.put("ScrollBar.thumbDarkShadow", COLOR_BORDE);
            UIManager.put("ScrollBar.thumbHighlight", COLOR_ACENTO);
            UIManager.put("ScrollBar.thumbShadow", COLOR_BORDE_ACTIVO);
            UIManager.put("ScrollBar.track", COLOR_FONDO_PANEL);
            UIManager.put("ScrollBar.trackHighlight", COLOR_FONDO_CARD);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Inicializa todos los componentes de la interfaz.
     */
    private void inicializarComponentes() {
        panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBackground(COLOR_FONDO_PRINCIPAL);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        crearPanelConfiguracion();
        crearPanelResultados();
        crearTablaVectorEstado();

        // Panel superior horizontal que combina configuración (izquierda) y resultados (derecha)
        JPanel panelSuperior = new JPanel(new BorderLayout(15, 0));
        panelSuperior.setBackground(COLOR_FONDO_PRINCIPAL);
        panelSuperior.add(panelConfiguracion, BorderLayout.WEST);
        panelSuperior.add(panelResultados, BorderLayout.CENTER);

        // Agregar al panel principal
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(tablaMejorada, BorderLayout.CENTER);
    }

    /**
     * Crea el panel de configuración con diseño moderno tipo card.
     */
    private void crearPanelConfiguracion() {
        panelConfiguracion = new JPanel();
        panelConfiguracion.setLayout(new BoxLayout(panelConfiguracion, BoxLayout.Y_AXIS));
        panelConfiguracion.setBackground(COLOR_FONDO_CARD);
        panelConfiguracion.setBorder(crearBordeCard("Configuración de Simulación"));
        panelConfiguracion.setPreferredSize(new Dimension(1300, 220));

        // Panel de parámetros principales
        JPanel panelParametros = crearCardSection("Parámetros de Simulación");
        JPanel gridParametros = new JPanel(new GridLayout(3, 4, 15, 10));
        gridParametros.setBackground(COLOR_FONDO_CARD);
        gridParametros.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        // Fila 1: TiempoDescargaMin | TiempoDescargaMax | DiasSimulacion | Simular
        gridParametros.add(crearGrupoInput("Tiempo Descarga Mín", txtTiempoDescargaMin = crearTextField(VALORES_DEFAULT[0]), "días"));
        gridParametros.add(crearGrupoInput("Tiempo Descarga Máx", txtTiempoDescargaMax = crearTextField(VALORES_DEFAULT[1]), "días"));
        gridParametros.add(crearGrupoInput("Días Simulación", txtDiasSimulacion = crearTextField(VALORES_DEFAULT[3]), "días"));
        gridParametros.add(btnSimular = crearBotonPrimario("▶ Simular", COLOR_EXITO));

        // Fila 2: MediaLlegada | MostrarDesde | MostrarHasta | Reset
        gridParametros.add(crearGrupoInput("Media Llegada", txtMediaLlegada = crearTextField(VALORES_DEFAULT[2]), "días"));
        gridParametros.add(crearGrupoInput("Mostrar Desde", txtMostrarDesde = crearTextField(VALORES_DEFAULT[4]), "día"));
        gridParametros.add(crearGrupoInput("Mostrar Hasta", txtMostrarHasta = crearTextField(VALORES_DEFAULT[5]), "día"));
        gridParametros.add(btnReset = crearBotonSecundario("🔄 Reset"));

        // Fila 3: Desde Fila | Hasta Fila | Toggle | [vacío]
        gridParametros.add(crearGrupoInput("Desde Fila", txtMostrarFilaDesde = crearTextField(VALORES_DEFAULT[6]), "fila"));
        gridParametros.add(crearGrupoInput("Hasta Fila", txtMostrarFilaHasta = crearTextField(VALORES_DEFAULT[7]), "fila"));
        JPanel panelToggle = crearPanelToggle();
        gridParametros.add(panelToggle);
        gridParametros.add(crearEspacioVacio());

        panelParametros.add(gridParametros);
        panelConfiguracion.add(panelParametros);

        actualizarEstadoCamposFiltrado();
    }

    /**
     * Crea el panel de resultados con diseño moderno - apilado verticalmente.
     */
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

        // Apilar verticalmente: Tiempo de permanencia arriba, Utilizaciones abajo
        panelResultados.add(cardTiempos, BorderLayout.NORTH);
        panelResultados.add(panelUtilizaciones, BorderLayout.CENTER);
    }

    /**
     * Crea el panel de utilizaciones con diseño moderno.
     */
    private void crearPanelUtilizaciones() {
        panelUtilizaciones = crearCardSection("Grado de Utilización de Recursos");
        JPanel contenidoUtil = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        contenidoUtil.setBackground(COLOR_FONDO_CARD);
        contenidoUtil.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

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
    }

    /**
     * Crea la tabla con scroll personalizado.
     */
    private void crearTablaVectorEstado() {
        tablaMejorada = new TablaMejorada(cantidadMuellesActual, cantidadGruasActual);
        tablaVectorEstado = tablaMejorada.getTabla();
        modeloTabla = tablaMejorada.getModelo();

        // Personalizar el scroll pane
        JScrollPane scrollPane = tablaMejorada.getScrollPane();

        // Configurar scroll bars más grandes y con mejor estilo
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setPreferredSize(new Dimension(16, 0));
        verticalScrollBar.setBackground(COLOR_FONDO_PANEL);
        verticalScrollBar.setUI(new ModernScrollBarUI());

        JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
        horizontalScrollBar.setPreferredSize(new Dimension(0, 16));
        horizontalScrollBar.setBackground(COLOR_FONDO_PANEL);
        horizontalScrollBar.setUI(new ModernScrollBarUI());

        // Configurar el scroll pane
        scrollPane.setBorder(null);
        scrollPane.setBackground(COLOR_FONDO_CARD);
        scrollPane.getViewport().setBackground(COLOR_FONDO_CARD);

        tablaVectorEstado.setRowHeight(28);
        tablaVectorEstado.getTableHeader().setReorderingAllowed(false);
        tablaMejorada.setPreferredSize(new Dimension(1200, 450));
        tablaMejorada.ajustarAnchoColumnas();
    }

    // =============== MÉTODOS AUXILIARES DE UI ===============

    /**
     * Crea un borde moderno para cards.
     */
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

    /**
     * Crea una sección card.
     */
    private JPanel crearCardSection(String titulo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO_CARD);
        panel.setBorder(crearBordeCard(titulo));
        return panel;
    }

    /**
     * Crea un grupo de input con label y unidad.
     */
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

    /**
     * Crea una métrica con valor y unidad.
     */
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

    private JPanel crearEspacioVacio() {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_FONDO_CARD);
        return panel;
    }

    private Component crearSeparadorVertical() {
        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setBackground(COLOR_BORDE);
        sep.setForeground(COLOR_BORDE);
        sep.setPreferredSize(new Dimension(1, 40));
        return sep;
    }

    /**
     * Crea un campo de texto moderno.
     */
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

        // Efectos hover y focus
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_BORDE_ACTIVO, 2),
                        BorderFactory.createEmptyBorder(5, 7, 5, 7)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_BORDE, 1),
                        BorderFactory.createEmptyBorder(6, 8, 6, 8)
                ));
            }
        });

        return field;
    }

    /**
     * Crea un checkbox moderno.
     */
    private JCheckBox crearCheckBox(String texto) {
        JCheckBox check = new JCheckBox(texto);
        check.setBackground(COLOR_FONDO_CARD);
        check.setForeground(COLOR_TEXTO_PRINCIPAL);
        check.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        check.setFocusPainted(false);
        return check;
    }

    /**
     * Crea un botón primario moderno.
     */
    private JButton crearBotonPrimario(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        boton.setPreferredSize(new Dimension(120, 40));
        boton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efectos hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(color.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(color);
            }
        });

        return boton;
    }

    /**
     * Crea un botón secundario moderno.
     */
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

        // Efectos hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_CAMPO_ACTIVO);
                boton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_BORDE_ACTIVO, 1),
                        BorderFactory.createEmptyBorder(9, 19, 9, 19)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_FONDO_PANEL);
                boton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_BORDE, 1),
                        BorderFactory.createEmptyBorder(9, 19, 9, 19)
                ));
            }
        });

        return boton;
    }

    /**
     * Crea un label para resultados.
     */
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
        label.setPreferredSize(new Dimension(80, 36));
        return label;
    }

    /**
     * Actualiza el estado de los campos de filtrado.
     */
    private void actualizarEstadoCamposFiltrado() {
        boolean filtrarPorFila = chkFiltrarPorFila.isSelected();

        txtMostrarDesde.setEnabled(!filtrarPorFila);
        txtMostrarHasta.setEnabled(!filtrarPorFila);
        txtMostrarFilaDesde.setEnabled(filtrarPorFila);
        txtMostrarFilaHasta.setEnabled(filtrarPorFila);

        // Cambiar colores según estado
        txtMostrarDesde.setBackground(filtrarPorFila ? COLOR_CAMPO_INACTIVO : COLOR_CAMPO_ACTIVO);
        txtMostrarHasta.setBackground(filtrarPorFila ? COLOR_CAMPO_INACTIVO : COLOR_CAMPO_ACTIVO);
        txtMostrarFilaDesde.setBackground(!filtrarPorFila ? COLOR_CAMPO_INACTIVO : COLOR_CAMPO_ACTIVO);
        txtMostrarFilaHasta.setBackground(!filtrarPorFila ? COLOR_CAMPO_INACTIVO : COLOR_CAMPO_ACTIVO);

        Color textoColor = filtrarPorFila ? COLOR_TEXTO_SECUNDARIO : COLOR_TEXTO_PRINCIPAL;
        txtMostrarDesde.setForeground(textoColor);
        txtMostrarHasta.setForeground(textoColor);

        textoColor = !filtrarPorFila ? COLOR_TEXTO_SECUNDARIO : COLOR_TEXTO_PRINCIPAL;
        txtMostrarFilaDesde.setForeground(textoColor);
        txtMostrarFilaHasta.setForeground(textoColor);
    }
    private JCheckBox crearToggleSwitch(String texto) {
        JCheckBox toggle = new JCheckBox(texto);
        toggle.setBackground(COLOR_FONDO_CARD);
        toggle.setForeground(COLOR_TEXTO_PRINCIPAL);
        toggle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        toggle.setFocusPainted(false);
        toggle.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        toggle.setOpaque(true);
        return toggle;
    }

    private JPanel crearPanelToggle() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO_CARD);

        JLabel lblTop = new JLabel("Tipo de Filtro", SwingConstants.CENTER);
        lblTop.setForeground(COLOR_TEXTO_SECUNDARIO);
        lblTop.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

        chkFiltrarPorFila = crearToggleSwitch("Por Fila");
        chkFiltrarPorFila.addActionListener(e -> actualizarEstadoCamposFiltrado());

        panel.add(lblTop, BorderLayout.NORTH);
        panel.add(chkFiltrarPorFila, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Resetea todos los campos a valores por defecto.
     */
    private void resetearCampos() {
        txtTiempoDescargaMin.setText(VALORES_DEFAULT[0]);
        txtTiempoDescargaMax.setText(VALORES_DEFAULT[1]);
        txtMediaLlegada.setText(VALORES_DEFAULT[2]);
        txtDiasSimulacion.setText(VALORES_DEFAULT[3]);
        txtMostrarDesde.setText(VALORES_DEFAULT[4]);
        txtMostrarHasta.setText(VALORES_DEFAULT[5]);
        txtMostrarFilaDesde.setText(VALORES_DEFAULT[6]);
        txtMostrarFilaHasta.setText(VALORES_DEFAULT[7]);
        chkFiltrarPorFila.setSelected(false);
        actualizarEstadoCamposFiltrado();

        // Limpiar resultados
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
        modeloTabla.setRowCount(0);
    }

    // =============== CONFIGURACIÓN DE VENTANA ===============

    private void configurarVentana() {
        setTitle("Simulación de Barcos en Bahía - Versión Moderna");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panelPrincipal);
        setSize(1500, 1000);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1300, 800));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(COLOR_FONDO_PRINCIPAL);
    }

    private void configurarEventos() {
        btnSimular.addActionListener(e -> ejecutarSimulacion());
        btnReset.addActionListener(e -> resetearCampos());
    }

    // =============== LÓGICA DE SIMULACIÓN (SIN CAMBIOS) ===============

    private void ejecutarSimulacion() {
        try {
            ConfiguracionSimulacion config = capturarConfiguracionDeUI();
            mostrarEstadoSimulando(true);

            Simulador simulador = new Simulador(config);
            ResultadosSimulacionDTO resultados = simulador.run();

            actualizarPanelDeResultados(resultados);
            poblarTablaVectorDeEstado(resultados.getFilasTabla(), resultados.getMaxBarcosEnSistema());

            mostrarEstadoSimulando(false);

        } catch (NumberFormatException ex) {
            mostrarErrorConfiguracion("Error en los parámetros: " + ex.getMessage());
        } catch (Exception ex) {
            mostrarErrorConfiguracion("Error durante la simulación: " + ex.getMessage());
        }
    }

    private ConfiguracionSimulacion capturarConfiguracionDeUI() throws NumberFormatException {
        double tiempoDescargaMin = Double.parseDouble(txtTiempoDescargaMin.getText().trim());
        double tiempoDescargaMax = Double.parseDouble(txtTiempoDescargaMax.getText().trim());
        double mediaLlegada = Double.parseDouble(txtMediaLlegada.getText().trim());
        int diasSimulacion = Integer.parseInt(txtDiasSimulacion.getText().trim());
        int mostrarDesde = Integer.parseInt(txtMostrarDesde.getText().trim());
        int mostrarHasta = Integer.parseInt(txtMostrarHasta.getText().trim());
        int mostrarFilaDesde = Integer.parseInt(txtMostrarFilaDesde.getText().trim());
        int mostrarFilaHasta = Integer.parseInt(txtMostrarFilaHasta.getText().trim());
        boolean filtrarPorFila = chkFiltrarPorFila.isSelected();

        int cantidadMuelles = 2;
        int cantidadGruas = 2;

        if (tiempoDescargaMin <= 0 || tiempoDescargaMax <= 0 || tiempoDescargaMin >= tiempoDescargaMax) {
            throw new NumberFormatException("Los tiempos de descarga deben ser positivos y Min < Max");
        }
        if (mediaLlegada <= 0) {
            throw new NumberFormatException("La media de llegada debe ser positiva");
        }
        if (diasSimulacion <= 0) {
            throw new NumberFormatException("Los días de simulación deben ser positivos");
        }

        if (filtrarPorFila) {
            if (mostrarFilaDesde < 0 || mostrarFilaHasta <= mostrarFilaDesde) {
                throw new NumberFormatException("Las filas de visualización deben ser válidas (desde >= 0 y hasta > desde)");
            }
            return new ConfiguracionSimulacion(
                    mediaLlegada, tiempoDescargaMin, tiempoDescargaMax,
                    cantidadMuelles, cantidadGruas, diasSimulacion,
                    mostrarFilaDesde, mostrarFilaHasta, true
            );
        } else {
            if (mostrarDesde < 0 || mostrarHasta <= mostrarDesde) {
                throw new NumberFormatException("Los días de visualización deben ser válidos (desde >= 0 y hasta > desde)");
            }
            return new ConfiguracionSimulacion(
                    mediaLlegada, tiempoDescargaMin, tiempoDescargaMax,
                    cantidadMuelles, cantidadGruas, diasSimulacion,
                    mostrarDesde, mostrarHasta
            );
        }
    }

    private void actualizarPanelDeResultados(ResultadosSimulacionDTO resultados) {
        lblTiempoPermanciaMin.setText(formatoDecimal.format(resultados.getTiempoPermanciaMinimo()));
        lblTiempoPermanciaMax.setText(formatoDecimal.format(resultados.getTiempoPermanciaMaximo()));
        lblTiempoPermanciaMedia.setText(formatoDecimal.format(resultados.getTiempoPermaneciaMedio()));

        double[] utilizacionMuelles = resultados.getUtilizacionMuelles();
        for (int i = 0; i < lblUtilizacionMuelles.length && i < utilizacionMuelles.length; i++) {
            lblUtilizacionMuelles[i].setText(formatoDecimal.format(utilizacionMuelles[i]) + "%");
        }

        double[] utilizacionGruas = resultados.getUtilizacionGruas();
        for (int i = 0; i < lblUtilizacionGruas.length && i < utilizacionGruas.length; i++) {
            lblUtilizacionGruas[i].setText(formatoDecimal.format(utilizacionGruas[i]) + "%");
        }
    }

    private void poblarTablaVectorDeEstado(java.util.List<FilaVectorDTO> filasDTO, int maxBarcosEnSistema) {
        modeloTabla.setRowCount(0);
        tablaMejorada.actualizarColumnasSegunBarcos(maxBarcosEnSistema);

        for (FilaVectorDTO filaDTO : filasDTO) {
            Object[] datosFila = generadorColumnas.generarDatosFila(filaDTO, maxBarcosEnSistema);
            modeloTabla.addRow(datosFila);
        }

        tablaMejorada.actualizarVista();
    }

    private void mostrarEstadoSimulando(boolean simulando) {
        if (simulando) {
            btnSimular.setText("Simulando...");
            btnSimular.setEnabled(false);
            btnSimular.setBackground(COLOR_ALERTA);
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        } else {
            btnSimular.setText("Simular");
            btnSimular.setEnabled(true);
            btnSimular.setBackground(COLOR_EXITO);
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private void mostrarErrorConfiguracion(String mensaje) {
        mostrarEstadoSimulando(false);
        JOptionPane.showMessageDialog(this, mensaje, "Error en la Configuración", JOptionPane.ERROR_MESSAGE);
    }
}

/**
 * UI personalizada para las barras de scroll modernas.
 */
class ModernScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
    private static final Color THUMB_COLOR = new Color(100, 149, 237);
    private static final Color THUMB_HOVER_COLOR = new Color(120, 169, 255);
    private static final Color TRACK_COLOR = new Color(35, 35, 35);

    @Override
    protected void configureScrollBarColors() {
        this.thumbColor = THUMB_COLOR;
        this.thumbHighlightColor = THUMB_HOVER_COLOR;
        this.thumbDarkShadowColor = THUMB_COLOR;
        this.thumbLightShadowColor = THUMB_COLOR;
        this.trackColor = TRACK_COLOR;
        this.trackHighlightColor = TRACK_COLOR;
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    private JButton createZeroButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color color = isDragging ? THUMB_HOVER_COLOR : THUMB_COLOR;
        g2.setPaint(color);
        g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2,
                thumbBounds.width - 4, thumbBounds.height - 4, 8, 8);
        g2.dispose();
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setPaint(TRACK_COLOR);
        g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        g2.dispose();
    }
}