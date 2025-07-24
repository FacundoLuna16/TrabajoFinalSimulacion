package com.facu.simulation.ui.eventos;

import com.facu.simulation.engine.ConfiguracionSimulacion;
import com.facu.simulation.engine.Simulador;
import com.facu.simulation.dto.ResultadosSimulacionDTO;
import com.facu.simulation.ui.paneles.PanelParametros;
import com.facu.simulation.ui.paneles.PanelControles;
import com.facu.simulation.ui.paneles.PanelEstadisticas;
import com.facu.simulation.ui.TablaMejorada;
import com.facu.simulation.ui.GeneradorColumnasTabla;
import com.facu.simulation.ui.estilos.TemaOscuro;
import com.facu.simulation.dto.FilaVectorDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

/**
 * ⭐ MANEJADOR DE EVENTOS DE SIMULACIÓN ⭐
 * Centraliza toda la lógica de eventos de la ventana principal
 */
public class EventHandlerSimulacion implements ActionListener {
    
    private final PanelParametros panelParametros;
    private final PanelControles panelControles;
    private final PanelEstadisticas panelEstadisticas;
    private final TablaMejorada tablaMejorada;
    private final DefaultTableModel modeloTabla;
    private final GeneradorColumnasTabla generadorColumnas;
    private final JFrame ventanaPadre;
    
    private final DecimalFormat formatoDecimal = new DecimalFormat("0.00");
    
    public EventHandlerSimulacion(PanelParametros panelParametros, 
                                 PanelControles panelControles,
                                 PanelEstadisticas panelEstadisticas,
                                 TablaMejorada tablaMejorada,
                                 JFrame ventanaPadre) {
        this.panelParametros = panelParametros;
        this.panelControles = panelControles;
        this.panelEstadisticas = panelEstadisticas;
        this.tablaMejorada = tablaMejorada;
        this.modeloTabla = tablaMejorada.getModelo();
        this.generadorColumnas = new GeneradorColumnasTabla();
        this.ventanaPadre = ventanaPadre;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        
        switch (comando) {
            case "SIMULAR":
                ejecutarSimulacion();
                break;
            case "RESET":
                resetearCampos();
                break;
            case "FILTRAR_POR_FILA":
                actualizarFiltroFila();
                break;
            default:
                System.err.println("Comando no reconocido: " + comando);
        }
    }
    
    /**
     * Ejecuta la simulación completa
     */
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
    
    /**
     * Captura la configuración desde los paneles de UI
     */
    private ConfiguracionSimulacion capturarConfiguracionDeUI() throws NumberFormatException {
        // Validar primero todos los campos
        panelParametros.validarCampos();
        
        double tiempoDescargaMin = panelParametros.getTiempoDescargaMin();
        double tiempoDescargaMax = panelParametros.getTiempoDescargaMax();
        double mediaLlegada = panelParametros.getMediaLlegada();
        double diasSimulacion = panelParametros.getDiasSimulacion();
        long semilla = panelParametros.getSemillaSeed();
        
        boolean filtrarPorFila = panelControles.isFiltrarPorFila();
        
        int cantidadMuelles = 2;  // Fijo por ahora
        int cantidadGruas = 2;   // Fijo por ahora

        if (filtrarPorFila) {
            int mostrarFilaDesde = panelParametros.getMostrarFilaDesde();
            int mostrarFilaHasta = panelParametros.getMostrarFilaHasta();
            
            return new ConfiguracionSimulacion(
                    mediaLlegada, tiempoDescargaMin, tiempoDescargaMax,
                    cantidadMuelles, cantidadGruas, diasSimulacion,
                    mostrarFilaDesde, mostrarFilaHasta, true, semilla
            );
        } else {
            int mostrarDesde = panelParametros.getMostrarDesde();
            int mostrarHasta = panelParametros.getMostrarHasta();
            
            return new ConfiguracionSimulacion(
                    mediaLlegada, tiempoDescargaMin, tiempoDescargaMax,
                    cantidadMuelles, cantidadGruas, diasSimulacion,
                    mostrarDesde, mostrarHasta, semilla
            );
        }
    }
    
    /**
     * Actualiza el panel de resultados con los datos de la simulación
     */
    private void actualizarPanelDeResultados(ResultadosSimulacionDTO resultados) {
        // Actualizar tiempo de permanencia
        panelEstadisticas.actualizarTiempoPermanencia(
            formatoDecimal.format(resultados.getTiempoPermanciaMinimo()),
            formatoDecimal.format(resultados.getTiempoPermanciaMaximo()),
            formatoDecimal.format(resultados.getTiempoPermaneciaMedio())
        );
        
        // Actualizar utilizaciones de muelles
        double[] utilizacionMuelles = resultados.getUtilizacionMuelles();
        String[] muellesStr = new String[utilizacionMuelles.length];
        for (int i = 0; i < utilizacionMuelles.length; i++) {
            muellesStr[i] = formatoDecimal.format(utilizacionMuelles[i]);
        }
        
        // Actualizar utilizaciones de grúas
        double[] utilizacionGruas = resultados.getUtilizacionGruas();
        String[] gruasStr = new String[utilizacionGruas.length];
        for (int i = 0; i < utilizacionGruas.length; i++) {
            gruasStr[i] = formatoDecimal.format(utilizacionGruas[i]);
        }
        
        panelEstadisticas.actualizarUtilizaciones(muellesStr, gruasStr);
    }
    
    /**
     * Puebla la tabla con los vectores de estado
     */
    private void poblarTablaVectorDeEstado(java.util.List<FilaVectorDTO> filasDTO, int maxBarcosEnSistema) {
        modeloTabla.setRowCount(0);
        tablaMejorada.actualizarColumnasSegunBarcos(maxBarcosEnSistema);

        for (FilaVectorDTO filaDTO : filasDTO) {
            Object[] datosFila = generadorColumnas.generarDatosFila(filaDTO, maxBarcosEnSistema);
            modeloTabla.addRow(datosFila);
        }

        tablaMejorada.actualizarVista();
    }
    
    /**
     * Muestra el estado de simulando o normal
     */
    private void mostrarEstadoSimulando(boolean simulando) {
        JButton btnSimular = panelControles.getBotonSimular();
        
        if (simulando) {
            btnSimular.setText("Simulando...");
            btnSimular.setEnabled(false);
            btnSimular.setBackground(TemaOscuro.COLOR_ALERTA);
            ventanaPadre.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        } else {
            btnSimular.setText("▶ Simular");
            btnSimular.setEnabled(true);
            btnSimular.setBackground(TemaOscuro.COLOR_EXITO);
            ventanaPadre.setCursor(Cursor.getDefaultCursor());
        }
    }
    
    /**
     * Muestra un error de configuración
     */
    private void mostrarErrorConfiguracion(String mensaje) {
        mostrarEstadoSimulando(false);
        JOptionPane.showMessageDialog(ventanaPadre, mensaje, "Error en la Configuración", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Resetea todos los campos a valores por defecto
     */
    private void resetearCampos() {
        panelParametros.resetearCampos();
        panelControles.resetearControles();
        panelEstadisticas.limpiarResultados();
        
        // Limpiar tabla
        modeloTabla.setRowCount(0);
    }
    
    /**
     * Actualiza el filtro de fila cuando se cambia el checkbox
     */
    private void actualizarFiltroFila() {
        boolean filtrarPorFila = panelControles.isFiltrarPorFila();
        panelParametros.actualizarEstadoCamposFiltrado(filtrarPorFila);
    }
}
