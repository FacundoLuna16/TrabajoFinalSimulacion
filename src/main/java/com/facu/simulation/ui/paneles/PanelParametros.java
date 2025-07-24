package com.facu.simulation.ui.paneles;

import com.facu.simulation.ui.componentes.FactoryComponentes;
import com.facu.simulation.ui.estilos.TemaOscuro;
import com.facu.simulation.ui.estilos.EfectosVisuales;

import javax.swing.*;
import java.awt.*;

/**
 * ⚙️ PANEL DE PARÁMETROS DE SIMULACIÓN ⚙️
 * Panel moderno con campos de configuración organizados en secciones
 * Mantiene el diseño tipo tarjeta con efectos visuales mejorados
 */
public class PanelParametros extends JPanel {
    
    // Campos de entrada de parámetros
    private JTextField txtTiempoDescargaMin;
    private JTextField txtTiempoDescargaMax;
    private JTextField txtMediaLlegada;
    private JTextField txtDiasSimulacion;
    private JTextField txtMostrarDesde;
    private JTextField txtMostrarHasta;
    private JTextField txtMostrarFilaDesde;
    private JTextField txtMostrarFilaHasta;
    private JTextField txtSemillaSeed;
    
    // Valores por defecto
    private final String[] VALORES_DEFAULT = {
            "0.5",  // TiempoDescargaMin
            "1.5",  // TiempoDescargaMax
            "1.25", // MediaLlegada
            "90",   // DiasSimulacion
            "0",    // MostrarDesde
            "15",   // MostrarHasta
            "0",    // MostrarFilaDesde
            "80",   // MostrarFilaHasta
            "12345" // SemillaSeed
    };
    
    public PanelParametros() {
        inicializar();
    }
    
    /**
     * Inicializa el panel con todos los campos
     */
    private void inicializar() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(TemaOscuro.COLOR_FONDO_PRINCIPAL);
        
        // Título de la sección con ícono
        JPanel tituloContainer = FactoryComponentes.crearTituloSeccion("⚙️ PARÁMETROS DE SIMULACIÓN");
        add(tituloContainer);
        
        // Container principal con borde moderno
        JPanel containerPrincipal = new JPanel();
        containerPrincipal.setLayout(new BoxLayout(containerPrincipal, BoxLayout.Y_AXIS));
        containerPrincipal.setBackground(TemaOscuro.COLOR_FONDO_CARD);
        containerPrincipal.setBorder(BorderFactory.createCompoundBorder(
            EfectosVisuales.crearBordeSimple(),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        crearCamposParametros(containerPrincipal);
        add(containerPrincipal);
        
        // Inicializar estado correcto de campos al crear el panel
        inicializarEstadoCampos();
    }
    
    /**
     * Crea la grilla 3x3 de parámetros con diseño moderno
     */
    private void crearCamposParametros(JPanel container) {
        // Crear campos de texto
        inicializarCampos();
        
        // Grid 3x3 compacto y moderno
        JPanel grid = new JPanel(new GridLayout(3, 3, TemaOscuro.MARGEN_MEDIO, TemaOscuro.ESPACIADO_COMPONENTES));
        grid.setBackground(TemaOscuro.COLOR_FONDO_CARD);
        
        // Fila 1: Tiempo Descarga Min | Media Llegada | Días Simulación
        grid.add(FactoryComponentes.crearGrupoInput("⏱️ Tiempo Min", txtTiempoDescargaMin, "días"));
        grid.add(FactoryComponentes.crearGrupoInput("📈 Media Llegada", txtMediaLlegada, "días"));
        grid.add(FactoryComponentes.crearGrupoInput("📅 Días Simulación", txtDiasSimulacion, "días"));
        
        // Fila 2: Tiempo Descarga Max | Mostrar desde | Mostrar hasta
        grid.add(FactoryComponentes.crearGrupoInput("⏱️ Tiempo Max", txtTiempoDescargaMax, "días"));
        grid.add(FactoryComponentes.crearGrupoInput("📊 Desde Día", txtMostrarDesde, "día"));
        grid.add(FactoryComponentes.crearGrupoInput("📊 Hasta Día", txtMostrarHasta, "día"));
        
        // Fila 3: Semilla Seed | Mostrar desde fila | Mostrar hasta fila
        grid.add(FactoryComponentes.crearGrupoInput("🎲 Semilla Seed", txtSemillaSeed, ""));
        grid.add(FactoryComponentes.crearGrupoInput("📋 Desde Fila", txtMostrarFilaDesde, "fila"));
        grid.add(FactoryComponentes.crearGrupoInput("📋 Hasta Fila", txtMostrarFilaHasta, "fila"));
        
        container.add(grid);
    }
    
    /**
     * Inicializa todos los campos de texto con valores por defecto
     */
    private void inicializarCampos() {
        txtTiempoDescargaMin = FactoryComponentes.crearCampoTexto(VALORES_DEFAULT[0]);
        txtTiempoDescargaMax = FactoryComponentes.crearCampoTexto(VALORES_DEFAULT[1]);
        txtMediaLlegada = FactoryComponentes.crearCampoTexto(VALORES_DEFAULT[2]);
        txtDiasSimulacion = FactoryComponentes.crearCampoTexto(VALORES_DEFAULT[3]);
        txtMostrarDesde = FactoryComponentes.crearCampoTexto(VALORES_DEFAULT[4]);
        txtMostrarHasta = FactoryComponentes.crearCampoTexto(VALORES_DEFAULT[5]);
        txtMostrarFilaDesde = FactoryComponentes.crearCampoTexto(VALORES_DEFAULT[6]);
        txtMostrarFilaHasta = FactoryComponentes.crearCampoTexto(VALORES_DEFAULT[7]);
        txtSemillaSeed = FactoryComponentes.crearCampoTexto(VALORES_DEFAULT[8]);
    }
    
    // =============== MÉTODOS PÚBLICOS PARA ACCESO A DATOS ===============
    
    public double getTiempoDescargaMin() throws NumberFormatException {
        return Double.parseDouble(txtTiempoDescargaMin.getText().trim());
    }
    
    public double getTiempoDescargaMax() throws NumberFormatException {
        return Double.parseDouble(txtTiempoDescargaMax.getText().trim());
    }
    
    public double getMediaLlegada() throws NumberFormatException {
        return Double.parseDouble(txtMediaLlegada.getText().trim());
    }
    
    public double getDiasSimulacion() throws NumberFormatException {
        return Double.parseDouble(txtDiasSimulacion.getText().trim());
    }
    
    public int getMostrarDesde() throws NumberFormatException {
        return Integer.parseInt(txtMostrarDesde.getText().trim());
    }
    
    public int getMostrarHasta() throws NumberFormatException {
        return Integer.parseInt(txtMostrarHasta.getText().trim());
    }
    
    public int getMostrarFilaDesde() throws NumberFormatException {
        return Integer.parseInt(txtMostrarFilaDesde.getText().trim());
    }
    
    public int getMostrarFilaHasta() throws NumberFormatException {
        return Integer.parseInt(txtMostrarFilaHasta.getText().trim());
    }
    
    public long getSemillaSeed() throws NumberFormatException {
        return Long.parseLong(txtSemillaSeed.getText().trim());
    }
    
    /**
     * Resetea todos los campos a valores por defecto
     */
    public void resetearCampos() {
        txtTiempoDescargaMin.setText(VALORES_DEFAULT[0]);
        txtTiempoDescargaMax.setText(VALORES_DEFAULT[1]);
        txtMediaLlegada.setText(VALORES_DEFAULT[2]);
        txtDiasSimulacion.setText(VALORES_DEFAULT[3]);
        txtMostrarDesde.setText(VALORES_DEFAULT[4]);
        txtMostrarHasta.setText(VALORES_DEFAULT[5]);
        txtMostrarFilaDesde.setText(VALORES_DEFAULT[6]);
        txtMostrarFilaHasta.setText(VALORES_DEFAULT[7]);
        txtSemillaSeed.setText(VALORES_DEFAULT[8]);
    }
    
    /**
     * Valida que todos los campos tengan valores válidos
     */
    public void validarCampos() throws NumberFormatException {
        double tiempoDescargaMin = getTiempoDescargaMin();
        double tiempoDescargaMax = getTiempoDescargaMax();
        double mediaLlegada = getMediaLlegada();
        double diasSimulacion = getDiasSimulacion();
        int mostrarDesde = getMostrarDesde();
        int mostrarHasta = getMostrarHasta();
        int mostrarFilaDesde = getMostrarFilaDesde();
        int mostrarFilaHasta = getMostrarFilaHasta();
        
        if (tiempoDescargaMin <= 0 || tiempoDescargaMax <= 0 || tiempoDescargaMin >= tiempoDescargaMax) {
            throw new NumberFormatException("Los tiempos de descarga deben ser positivos y Min < Max");
        }
        if (mediaLlegada <= 0) {
            throw new NumberFormatException("La media de llegada debe ser positiva");
        }
        if (diasSimulacion <= 0) {
            throw new NumberFormatException("Los días de simulación deben ser positivos");
        }
        if (mostrarDesde < 0 || mostrarHasta <= mostrarDesde) {
            throw new NumberFormatException("Los días de visualización deben ser válidos (desde >= 0 y hasta > desde)");
        }
        if (mostrarFilaDesde < 0 || mostrarFilaHasta <= mostrarFilaDesde) {
            throw new NumberFormatException("Las filas de visualización deben ser válidas (desde >= 0 y hasta > desde)");
        }
    }
    
    /**
     * Actualiza el estado de los campos según el filtro por fila
     */
    public void actualizarEstadoCamposFiltrado(boolean filtrarPorFila) {
        txtMostrarDesde.setEnabled(!filtrarPorFila);
        txtMostrarHasta.setEnabled(!filtrarPorFila);
        txtMostrarFilaDesde.setEnabled(filtrarPorFila);
        txtMostrarFilaHasta.setEnabled(filtrarPorFila);

        // Cambiar colores según estado
        txtMostrarDesde.setBackground(filtrarPorFila ? TemaOscuro.COLOR_CAMPO_INACTIVO : TemaOscuro.COLOR_CAMPO_ACTIVO);
        txtMostrarHasta.setBackground(filtrarPorFila ? TemaOscuro.COLOR_CAMPO_INACTIVO : TemaOscuro.COLOR_CAMPO_ACTIVO);
        txtMostrarFilaDesde.setBackground(filtrarPorFila ? TemaOscuro.COLOR_CAMPO_ACTIVO : TemaOscuro.COLOR_CAMPO_INACTIVO);
        txtMostrarFilaHasta.setBackground(filtrarPorFila ? TemaOscuro.COLOR_CAMPO_ACTIVO : TemaOscuro.COLOR_CAMPO_INACTIVO);

        Color textoColor = filtrarPorFila ? TemaOscuro.COLOR_TEXTO_SECUNDARIO : TemaOscuro.COLOR_TEXTO_PRINCIPAL;
        txtMostrarDesde.setForeground(textoColor);
        txtMostrarHasta.setForeground(textoColor);

        textoColor = !filtrarPorFila ? TemaOscuro.COLOR_TEXTO_SECUNDARIO : TemaOscuro.COLOR_TEXTO_PRINCIPAL;
        txtMostrarFilaDesde.setForeground(textoColor);
        txtMostrarFilaHasta.setForeground(textoColor);
    }
    
    /**
     * Inicializa el estado correcto de los campos al inicio
     */
    public void inicializarEstadoCampos() {
        // Al inicio, checkbox sin marcar = filtrar por día (no por fila)
        actualizarEstadoCamposFiltrado(false);
    }
}
