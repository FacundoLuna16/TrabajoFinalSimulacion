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
    private JTextField txtCantidadMuelles;
    private JTextField txtMediaLlegada;
    private JTextField txtMostrarHasta;
    private JTextField txtDiasSimulacion;
    private JTextField txtCantidadGruas;
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
    
    // Variables de configuración actual
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
        panelConfiguracion.add(crearLabel("Cantidad de muelles"), gbc);
        gbc.gridx = 3;
        txtCantidadMuelles = crearTextField("2");
        panelConfiguracion.add(txtCantidadMuelles, gbc);
        
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
        panelConfiguracion.add(crearLabel("Media Llegada (Días)"), gbc);
        gbc.gridx = 3;
        txtMediaLlegada = crearTextField("1.5");
        panelConfiguracion.add(txtMediaLlegada, gbc);
        
        gbc.gridx = 4;
        panelConfiguracion.add(crearLabel("Cantidad de Grúas"), gbc);
        gbc.gridx = 5;
        txtCantidadGruas = crearTextField("2");
        panelConfiguracion.add(txtCantidadGruas, gbc);
        
        // Tercera fila
        gbc.gridx = 0; gbc.gridy = 2;
        panelConfiguracion.add(crearLabel("Mostrar desde (día)"), gbc);
        gbc.gridx = 1;
        txtMostrarDesde = crearTextField("5");
        panelConfiguracion.add(txtMostrarDesde, gbc);
        
        gbc.gridx = 2;
        panelConfiguracion.add(crearLabel("Mostrar Hasta (día)"), gbc);
        gbc.gridx = 3;
        txtMostrarHasta = crearTextField("80");
        panelConfiguracion.add(txtMostrarHasta, gbc);
        
        // Botón Simular (verde como en la imagen)
        gbc.gridx = 4; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        btnSimular = new JButton("Simular");
        btnSimular.setBackground(new Color(0, 150, 0));
        btnSimular.setForeground(Color.WHITE);
        btnSimular.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        panelConfiguracion.add(btnSimular, gbc);
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
     * Actualiza el panel de utilizaciones cuando cambia la cantidad de recursos.
     */
    private void actualizarPanelUtilizaciones(int nuevosMuelles, int nuevasGruas) {
        if (nuevosMuelles != cantidadMuellesActual || nuevasGruas != cantidadGruasActual) {
            cantidadMuellesActual = nuevosMuelles;
            cantidadGruasActual = nuevasGruas;
            
            panelUtilizaciones.removeAll();
            crearPanelUtilizaciones();
            panelUtilizaciones.revalidate();
            panelUtilizaciones.repaint();
        }
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
        if (nuevosMuelles != cantidadMuellesActual || nuevasGruas != cantidadGruasActual) {
            // Recrear la tabla con nueva configuración
            remove(tablaMejorada);
            cantidadMuellesActual = nuevosMuelles;
            cantidadGruasActual = nuevasGruas;
            
            crearTablaVectorEstado();
            add(tablaMejorada, BorderLayout.CENTER);
            
            revalidate();
            repaint();
        }
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
            
            // Actualizar interfaz según nueva configuración
            actualizarPanelUtilizaciones(config.getCantidadMuelles(), config.getCantidadGruas());
            actualizarTabla(config.getCantidadMuelles(), config.getCantidadGruas());
            
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
        int cantidadMuelles = Integer.parseInt(txtCantidadMuelles.getText().trim());
        int cantidadGruas = Integer.parseInt(txtCantidadGruas.getText().trim());
        int diasSimulacion = Integer.parseInt(txtDiasSimulacion.getText().trim());
        int mostrarDesde = Integer.parseInt(txtMostrarDesde.getText().trim());
        int mostrarHasta = Integer.parseInt(txtMostrarHasta.getText().trim());
        
        // Validaciones básicas
        if (tiempoDescargaMin <= 0 || tiempoDescargaMax <= 0 || tiempoDescargaMin >= tiempoDescargaMax) {
            throw new NumberFormatException("Los tiempos de descarga deben ser positivos y Min < Max");
        }
        if (mediaLlegada <= 0) {
            throw new NumberFormatException("La media de llegada debe ser positiva");
        }
        if (cantidadMuelles <= 0 || cantidadGruas <= 0) {
            throw new NumberFormatException("Las cantidades de recursos deben ser positivas");
        }
        if (diasSimulacion <= 0 || mostrarDesde < 0 || mostrarHasta <= mostrarDesde) {
            throw new NumberFormatException("Los días de simulación deben ser válidos");
        }
        
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
        
        // Actualizar encabezados si es necesario
        String[] nuevasColumnas = generadorColumnas.generarEncabezados(maxBarcosEnSistema);
        if (modeloTabla.getColumnCount() != nuevasColumnas.length) {
            // Recrear modelo con nuevas columnas
            modeloTabla.setColumnIdentifiers(nuevasColumnas);
        }
        
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
