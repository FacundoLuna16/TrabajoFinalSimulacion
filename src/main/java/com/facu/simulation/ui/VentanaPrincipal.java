package com.facu.simulation.ui;

import com.facu.simulation.engine.ConfiguracionSimulacion;
import com.facu.simulation.engine.Simulador;
import com.facu.simulation.dto.FilaVectorDTO;
import com.facu.simulation.dto.ResultadosSimulacionDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

/**
 * Ventana principal de la aplicación de simulación portuaria.
 * Diseño basado en las especificaciones de la imagen de referencia.
 */
public class VentanaPrincipal extends JFrame {
    
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
    private GeneradorColumnasTabla generadorColumnas; // NUEVO: Generador de columnas dinámicas
    
    // Variables de configuración actual (fijos para 2 muelles y 2 grúas)
    private int cantidadMuellesActual = 2;
    private int cantidadGruasActual = 2;
    
    // Formato para números decimales
    private DecimalFormat formatoDecimal = new DecimalFormat("#.##");
    
    // =============== CONSTRUCTOR ===============
    
    public VentanaPrincipal() {
        // Inicializar generador de columnas
        generadorColumnas = new GeneradorColumnasTabla();
        
        inicializarComponentes();
        configurarVentana();
        configurarEventos();
    }
    
    /**
     * Inicializa todos los componentes de la interfaz.
     */
    private void inicializarComponentes() {
        // Panel principal
        panelPrincipal = new JPanel(new BorderLayout());
        
        // Crear los paneles
        crearPanelConfiguracion();
        crearPanelResultados();
        crearTablaVectorEstado();
        
        // Panel superior que combina configuración y resultados
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelConfiguracion, BorderLayout.NORTH);
        panelSuperior.add(panelResultados, BorderLayout.CENTER);
        
        // Agregar al panel principal
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(tablaMejorada, BorderLayout.CENTER);
    }
    
    /**
     * Crea el panel de configuración con el diseño de la imagen.
     */
    private void crearPanelConfiguracion() {
        panelConfiguracion = new JPanel();
        panelConfiguracion.setLayout(new GridBagLayout());
        panelConfiguracion.setBorder(BorderFactory.createTitledBorder("Simulación de Barcos en Bahía"));
        panelConfiguracion.setBackground(new Color(45, 45, 45)); // Fondo oscuro como en la imagen
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Primera fila
        gbc.gridx = 0; gbc.gridy = 0;
        panelConfiguracion.add(crearLabel("TiempoDescargaMin"), gbc);
        gbc.gridx = 1;
        txtTiempoDescargaMin = crearTextField("0.5");
        panelConfiguracion.add(txtTiempoDescargaMin, gbc);
        
        gbc.gridx = 2;
        panelConfiguracion.add(crearLabel("Media Llegada (Días)"), gbc);
        gbc.gridx = 3;
        txtMediaLlegada = crearTextField("1.5");
        panelConfiguracion.add(txtMediaLlegada, gbc);
        
        gbc.gridx = 4;
        panelConfiguracion.add(crearLabel("Días de Simulación"), gbc);
        gbc.gridx = 5;
        txtDiasSimulacion = crearTextField("90");
        panelConfiguracion.add(txtDiasSimulacion, gbc);
        
        // Segunda fila
        gbc.gridx = 0; gbc.gridy = 1;
        panelConfiguracion.add(crearLabel("TiempoDescargaMax"), gbc);
        gbc.gridx = 1;
        txtTiempoDescargaMax = crearTextField("1.5");
        panelConfiguracion.add(txtTiempoDescargaMax, gbc);
        
        gbc.gridx = 2;
        panelConfiguracion.add(crearLabel("Mostrar desde (día)"), gbc);
        gbc.gridx = 3;
        txtMostrarDesde = crearTextField("5");
        panelConfiguracion.add(txtMostrarDesde, gbc);
        
        gbc.gridx = 4;
        panelConfiguracion.add(crearLabel("Mostrar Hasta (día)"), gbc);
        gbc.gridx = 5;
        txtMostrarHasta = crearTextField("80");
        panelConfiguracion.add(txtMostrarHasta, gbc);
        
        // Tercera fila
        gbc.gridx = 0; gbc.gridy = 2;
        panelConfiguracion.add(crearLabel("Mostrar desde (fila)"), gbc);
        gbc.gridx = 1;
        txtMostrarFilaDesde = crearTextField("5");
        panelConfiguracion.add(txtMostrarFilaDesde, gbc);
        
        gbc.gridx = 2;
        panelConfiguracion.add(crearLabel("Mostrar Hasta (fila)"), gbc);
        gbc.gridx = 3;
        txtMostrarFilaHasta = crearTextField("80");
        panelConfiguracion.add(txtMostrarFilaHasta, gbc);
        
        // Switch para tipo de filtrado
        gbc.gridx = 4; gbc.gridy = 2;
        gbc.gridwidth = 1;
        chkFiltrarPorFila = new JCheckBox("Filtrar por fila");
        chkFiltrarPorFila.setBackground(new Color(45, 45, 45));
        chkFiltrarPorFila.setForeground(Color.WHITE);
        chkFiltrarPorFila.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        chkFiltrarPorFila.addActionListener(e -> actualizarEstadoCamposFiltrado());
        panelConfiguracion.add(chkFiltrarPorFila, gbc);
        
        // Botón Simular (verde como en la imagen)
        gbc.gridx = 5; gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        btnSimular = new JButton("Simular");
        btnSimular.setBackground(new Color(0, 150, 0));
        btnSimular.setForeground(Color.WHITE);
        btnSimular.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        panelConfiguracion.add(btnSimular, gbc);
        
        // Configurar estado inicial
        actualizarEstadoCamposFiltrado();
    }
    
    /**
     * Crea el panel de resultados con tiempo de permanencia y utilizaciones.
     */
    private void crearPanelResultados() {
        panelResultados = new JPanel(new BorderLayout());
        panelResultados.setBorder(BorderFactory.createTitledBorder("Resultados de la simulación"));
        panelResultados.setBackground(new Color(45, 45, 45));
        
        // Panel de tiempo de permanencia
        JPanel panelTiempos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTiempos.setBackground(new Color(45, 45, 45));
        panelTiempos.add(crearLabel("tiempo de permanencia de barcos en bahía:"));
        
        panelTiempos.add(crearLabel("Min"));
        lblTiempoPermanciaMin = crearLabelResultado("--");
        panelTiempos.add(lblTiempoPermanciaMin);
        
        panelTiempos.add(crearLabel("Max"));
        lblTiempoPermanciaMax = crearLabelResultado("--");
        panelTiempos.add(lblTiempoPermanciaMax);
        
        panelTiempos.add(crearLabel("Media"));
        lblTiempoPermanciaMedia = crearLabelResultado("--");
        panelTiempos.add(lblTiempoPermanciaMedia);
        
        // Panel de utilizaciones
        crearPanelUtilizaciones();
        
        panelResultados.add(panelTiempos, BorderLayout.NORTH);
        panelResultados.add(panelUtilizaciones, BorderLayout.CENTER);
    }
    
    /**
     * Crea el panel de utilizaciones dinámico según cantidad de recursos.
     */
    private void crearPanelUtilizaciones() {
        panelUtilizaciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelUtilizaciones.setBackground(new Color(45, 45, 45));
        
        // Inicializar arrays de labels
        lblUtilizacionMuelles = new JLabel[cantidadMuellesActual];
        lblUtilizacionGruas = new JLabel[cantidadGruasActual];
        
        // Crear labels para muelles
        for (int i = 0; i < cantidadMuellesActual; i++) {
            panelUtilizaciones.add(crearLabel("Grado de Utilización Muelle " + (i + 1)));
            lblUtilizacionMuelles[i] = crearLabelResultado("--");
            panelUtilizaciones.add(lblUtilizacionMuelles[i]);
        }
        
        // Crear labels para grúas
        for (int i = 0; i < cantidadGruasActual; i++) {
            panelUtilizaciones.add(crearLabel("Grado de Utilización Grúa " + (i + 1)));
            lblUtilizacionGruas[i] = crearLabelResultado("--");
            panelUtilizaciones.add(lblUtilizacionGruas[i]);
        }
    }
    
    /**
     * Actualiza el estado de los campos de filtrado según el modo seleccionado.
     */
    private void actualizarEstadoCamposFiltrado() {
        boolean filtrarPorFila = chkFiltrarPorFila.isSelected();
        
        // Habilitar/deshabilitar campos según el modo
        txtMostrarDesde.setEnabled(!filtrarPorFila);
        txtMostrarHasta.setEnabled(!filtrarPorFila);
        txtMostrarFilaDesde.setEnabled(filtrarPorFila);
        txtMostrarFilaHasta.setEnabled(filtrarPorFila);
        
        // Cambiar color de fondo para indicar visualmente el estado
        Color colorHabilitado = Color.BLACK;
        Color colorDeshabilitado = new Color(60, 60, 60);
        Color textoHabilitado = Color.WHITE;
        Color textoDeshabilitado = Color.GRAY;
        
        txtMostrarDesde.setBackground(filtrarPorFila ? colorDeshabilitado : colorHabilitado);
        txtMostrarHasta.setBackground(filtrarPorFila ? colorDeshabilitado : colorHabilitado);
        txtMostrarFilaDesde.setBackground(!filtrarPorFila ? colorDeshabilitado : colorHabilitado);
        txtMostrarFilaHasta.setBackground(!filtrarPorFila ? colorDeshabilitado : colorHabilitado);
        
        txtMostrarDesde.setForeground(filtrarPorFila ? textoDeshabilitado : textoHabilitado);
        txtMostrarHasta.setForeground(filtrarPorFila ? textoDeshabilitado : textoHabilitado);
        txtMostrarFilaDesde.setForeground(!filtrarPorFila ? textoDeshabilitado : textoHabilitado);
        txtMostrarFilaHasta.setForeground(!filtrarPorFila ? textoDeshabilitado : textoHabilitado);
    }
    
    /**
     * Actualiza el panel de utilizaciones cuando cambia la cantidad de recursos.
     */
    private void actualizarPanelUtilizaciones(int nuevosMuelles, int nuevasGruas) {
        // Como los valores son fijos (2 muelles, 2 grúas), no necesitamos actualizar el panel
        // pero mantenemos el método para compatibilidad
    }
    
    /**
     * Crea la tabla para mostrar los vectores de estado con encabezados mejorados.
     */
    private void crearTablaVectorEstado() {
        // Crear la tabla mejorada
        tablaMejorada = new TablaMejorada(cantidadMuellesActual, cantidadGruasActual);
        
        // Obtener referencias a la tabla y modelo
        tablaVectorEstado = tablaMejorada.getTabla();
        modeloTabla = tablaMejorada.getModelo();
        
        // Configurar propiedades adicionales
        tablaVectorEstado.setRowHeight(25);
        tablaVectorEstado.getTableHeader().setReorderingAllowed(false);
        
        // Configurar tamaño preferido
        tablaMejorada.setPreferredSize(new Dimension(1200, 400));
        
        // Ajustar ancho de columnas
        tablaMejorada.ajustarAnchoColumnas();
    }
    
    /**
     * Actualiza la tabla cuando cambia la configuración de recursos.
     */
    private void actualizarTabla(int nuevosMuelles, int nuevasGruas) {
        // Como los valores son fijos (2 muelles, 2 grúas), no necesitamos actualizar la tabla
        // pero mantenemos el método para compatibilidad
    }
    
    /**
     * Métodos auxiliares para crear componentes con estilo consistente.
     */
    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(Color.WHITE);
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        return label;
    }
    
    private JLabel crearLabelResultado(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(Color.CYAN);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        label.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        label.setOpaque(true);
        label.setBackground(Color.BLACK);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(60, 25));
        return label;
    }
    
    private JTextField crearTextField(String valorInicial) {
        JTextField field = new JTextField(valorInicial);
        field.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        field.setBackground(Color.BLACK);
        field.setForeground(Color.WHITE);
        field.setHorizontalAlignment(SwingConstants.CENTER);
        field.setPreferredSize(new Dimension(60, 25));
        return field;
    }
    
    /**
     * Configura las propiedades básicas de la ventana.
     */
    private void configurarVentana() {
        setTitle("Simulación de Barcos en Bahía");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panelPrincipal);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1200, 700));
        
        // Maximizar la ventana automáticamente
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Aplicar tema oscuro
        getContentPane().setBackground(new Color(45, 45, 45));
    }
    
    /**
     * Configura los eventos de los componentes.
     */
    private void configurarEventos() {
        btnSimular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ejecutarSimulacion();
            }
        });
    }
    
    // =============== LÓGICA DE SIMULACIÓN ===============
    
    /**
     * Método principal que ejecuta la simulación según los parámetros de la interfaz.
     */
    private void ejecutarSimulacion() {
        try {
            // Capturar y validar parámetros
            ConfiguracionSimulacion config = capturarConfiguracionDeUI();
            
            // Actualizar interfaz con valores fijos (2 muelles, 2 grúas)
            actualizarPanelUtilizaciones(2, 2);
            actualizarTabla(2, 2);
            
            // Mostrar estado de simulando
            mostrarEstadoSimulando(true);
            
            // Ejecutar simulación (esto debería ejecutarse en un thread separado en una app real)
            Simulador simulador = new Simulador(config);
            ResultadosSimulacionDTO resultados = simulador.run();
            
            // Mostrar resultados
            actualizarPanelDeResultados(resultados);
            poblarTablaVectorDeEstado(resultados.getFilasTabla(), resultados.getMaxBarcosEnSistema());
            
            // Restaurar estado normal
            mostrarEstadoSimulando(false);
            
        } catch (NumberFormatException ex) {
            mostrarErrorConfiguracion("Error en los parámetros: " + ex.getMessage());
        } catch (Exception ex) {
            mostrarErrorConfiguracion("Error durante la simulación: " + ex.getMessage());
        }
    }
    
    /**
     * Captura los parámetros de configuración de la interfaz.
     */
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
        
        // Valores fijos para muelles y grúas
        int cantidadMuelles = 2;
        int cantidadGruas = 2;
        
        // Validaciones básicas
        if (tiempoDescargaMin <= 0 || tiempoDescargaMax <= 0 || tiempoDescargaMin >= tiempoDescargaMax) {
            throw new NumberFormatException("Los tiempos de descarga deben ser positivos y Min < Max");
        }
        if (mediaLlegada <= 0) {
            throw new NumberFormatException("La media de llegada debe ser positiva");
        }
        if (diasSimulacion <= 0) {
            throw new NumberFormatException("Los días de simulación deben ser positivos");
        }
        
        // Validar según el modo de filtrado seleccionado
        if (filtrarPorFila) {
            if (mostrarFilaDesde < 0 || mostrarFilaHasta <= mostrarFilaDesde) {
                throw new NumberFormatException("Las filas de visualización deben ser válidas (desde >= 0 y hasta > desde)");
            }
            // Usar constructor para filtro por fila
            return new ConfiguracionSimulacion(
                mediaLlegada, 
                tiempoDescargaMin, 
                tiempoDescargaMax,
                cantidadMuelles, 
                cantidadGruas, 
                diasSimulacion,
                mostrarFilaDesde,
                mostrarFilaHasta,
                true // esPorFila = true
            );
        } else {
            if (mostrarDesde < 0 || mostrarHasta <= mostrarDesde) {
                throw new NumberFormatException("Los días de visualización deben ser válidos (desde >= 0 y hasta > desde)");
            }
            // Usar constructor para filtro por día
            return new ConfiguracionSimulacion(
                mediaLlegada, 
                tiempoDescargaMin, 
                tiempoDescargaMax,
                cantidadMuelles, 
                cantidadGruas, 
                diasSimulacion,
                mostrarDesde,
                mostrarHasta
            );
        }
    }
    
    /**
     * Actualiza el panel de resultados con las estadísticas finales.
     */
    private void actualizarPanelDeResultados(ResultadosSimulacionDTO resultados) {
        lblTiempoPermanciaMin.setText(formatoDecimal.format(resultados.getTiempoPermanciaMinimo()));
        lblTiempoPermanciaMax.setText(formatoDecimal.format(resultados.getTiempoPermanciaMaximo()));
        lblTiempoPermanciaMedia.setText(formatoDecimal.format(resultados.getTiempoPermaneciaMedio()));
        
        // Actualizar utilizaciones de muelles
        double[] utilizacionMuelles = resultados.getUtilizacionMuelles();
        for (int i = 0; i < lblUtilizacionMuelles.length && i < utilizacionMuelles.length; i++) {
            lblUtilizacionMuelles[i].setText(formatoDecimal.format(utilizacionMuelles[i]) + "%");
        }
        
        // Actualizar utilizaciones de grúas
        double[] utilizacionGruas = resultados.getUtilizacionGruas();
        for (int i = 0; i < lblUtilizacionGruas.length && i < utilizacionGruas.length; i++) {
            lblUtilizacionGruas[i].setText(formatoDecimal.format(utilizacionGruas[i]) + "%");
        }
    }
    
    /**
     * Puebla la tabla principal con los vectores de estado de la simulación.
     */
    private void poblarTablaVectorDeEstado(java.util.List<FilaVectorDTO> filasDTO, int maxBarcosEnSistema) {
        // Limpiar resultados anteriores
        modeloTabla.setRowCount(0);
        
        // Actualizar columnas dinámicas según barcos en sistema
        tablaMejorada.actualizarColumnasSegunBarcos(maxBarcosEnSistema);
        
        // Añadir filas usando el generador
        for (FilaVectorDTO filaDTO : filasDTO) {
            Object[] datosFila = generadorColumnas.generarDatosFila(filaDTO, maxBarcosEnSistema);
            modeloTabla.addRow(datosFila);
        }
        
        // Actualizar vista con los nuevos datos
        tablaMejorada.actualizarVista();
    }
    
    /**
     * Muestra/oculta el estado de "Simulando..." en la interfaz.
     */
    private void mostrarEstadoSimulando(boolean simulando) {
        if (simulando) {
            btnSimular.setText("Simulando...");
            btnSimular.setEnabled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        } else {
            btnSimular.setText("Simular");
            btnSimular.setEnabled(true);
            setCursor(Cursor.getDefaultCursor());
        }
    }
    
    /**
     * Muestra un mensaje de error relacionado con la configuración.
     */
    private void mostrarErrorConfiguracion(String mensaje) {
        mostrarEstadoSimulando(false);
        JOptionPane.showMessageDialog(
            this,
            mensaje,
            "Error en la Configuración",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
