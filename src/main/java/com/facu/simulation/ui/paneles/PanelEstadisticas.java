package com.facu.simulation.ui.paneles;

import com.facu.simulation.ui.componentes.TarjetaEstadistica;
import com.facu.simulation.ui.componentes.FactoryComponentes;
import com.facu.simulation.ui.estilos.TemaOscuro;
import com.facu.simulation.dto.ResultadosSimulacionDTO;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * ⭐ PANEL DE ESTADÍSTICAS MODERNO TIPO DASHBOARD ⭐
 * Contiene todas las métricas de la simulación en tarjetas atractivas
 */
public class PanelEstadisticas extends JPanel {
    
    // Tarjetas de tiempo de permanencia
    private TarjetaEstadistica tarjetaTiempoMin;
    private TarjetaEstadistica tarjetaTiempoMax;
    private TarjetaEstadistica tarjetaTiempoMedia;
    
    // Tarjetas de utilización
    private TarjetaEstadistica[] tarjetasMuelles;
    private TarjetaEstadistica[] tarjetasGruas;
    
    private DecimalFormat formatoDecimal = new DecimalFormat("0.00");
    
    public PanelEstadisticas() {
        inicializar();
    }
    
    /**
     * Inicializa el panel con toda la estructura
     */
    private void inicializar() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(TemaOscuro.COLOR_FONDO_PRINCIPAL);
        
        crearSeccionTiempoPermanencia();
        crearEspaciador();
        crearSeccionUtilizacion();
    }
    
    /**
     * Crea la sección de tiempo de permanencia (ocupando todo el ancho)
     */
    private void crearSeccionTiempoPermanencia() {
        // Título de la sección
        JPanel tituloContainer = FactoryComponentes.crearTituloSeccion("📊 TIEMPO DE PERMANENCIA");
        add(tituloContainer);
        
        // Container en grid 1x3 para ocupar todo el ancho
        JPanel containerTarjetas = new JPanel(new GridLayout(1, 3, 8, 0));
        containerTarjetas.setBackground(TemaOscuro.COLOR_FONDO_PRINCIPAL);
        
        // Crear tarjetas
        tarjetaTiempoMin = new TarjetaEstadistica(
                "MÍNIMO", "días",
                TemaOscuro.COLOR_ACENTO_VERDE,
                "🟢"
        );
        
        tarjetaTiempoMax = new TarjetaEstadistica(
                "MÁXIMO", "días",
                TemaOscuro.COLOR_ACENTO_NARANJA,
                "🟠"
        );
        
        tarjetaTiempoMedia = new TarjetaEstadistica(
                "PROMEDIO", "días",
                TemaOscuro.COLOR_ACENTO,
                "🔵"
        );
        
        // Agregar al container en grid
        containerTarjetas.add(tarjetaTiempoMin);
        containerTarjetas.add(tarjetaTiempoMax);
        containerTarjetas.add(tarjetaTiempoMedia);
        
        add(containerTarjetas);
    }
    
    /**
     * Crea la sección de utilización de recursos (grid 2x2)
     */
    private void crearSeccionUtilizacion() {
        // Título de la sección
        JPanel tituloContainer = FactoryComponentes.crearTituloSeccion("⚡ UTILIZACIÓN DE RECURSOS");
        add(tituloContainer);
        
        // Container en grid 2x2
        JPanel containerGrid = new JPanel(new GridLayout(2, 2, 8, 8));
        containerGrid.setBackground(TemaOscuro.COLOR_FONDO_PRINCIPAL);
        
        // Inicializar arrays
        tarjetasMuelles = new TarjetaEstadistica[2];
        tarjetasGruas = new TarjetaEstadistica[2];
        
        // Crear tarjetas de muelles
        tarjetasMuelles[0] = new TarjetaEstadistica(
                "MUELLE 1", "%",
                TemaOscuro.COLOR_ACENTO_PURPURA,
                "🟣"
        );
        
        tarjetasMuelles[1] = new TarjetaEstadistica(
                "MUELLE 2", "%",
                TemaOscuro.COLOR_ACENTO_PURPURA_CLARO,
                "🟪"
        );
        
        // Crear tarjetas de grúas
        tarjetasGruas[0] = new TarjetaEstadistica(
                "GRÚA 1", "%",
                TemaOscuro.COLOR_ACENTO_CYAN,
                "🔷"
        );
        
        tarjetasGruas[1] = new TarjetaEstadistica(
                "GRÚA 2", "%",
                TemaOscuro.COLOR_ACENTO_CYAN_CLARO,
                "💎"
        );
        
        // Agregar en orden: Muelle1, Grúa1, Muelle2, Grúa2
        containerGrid.add(tarjetasMuelles[0]);
        containerGrid.add(tarjetasGruas[0]);
        containerGrid.add(tarjetasMuelles[1]);
        containerGrid.add(tarjetasGruas[1]);
        
        add(containerGrid);
    }
    
    /**
     * Crea un espaciador entre secciones
     */
    private void crearEspaciador() {
        add(Box.createRigidArea(new Dimension(0, TemaOscuro.MARGEN_GRANDE)));
    }
    
    // =============== MÉTODOS PÚBLICOS PARA ACTUALIZAR DATOS ===============
    
    /**
     * Actualiza todas las estadísticas con los resultados de la simulación
     */
    public void actualizarEstadisticas(ResultadosSimulacionDTO resultados) {
        SwingUtilities.invokeLater(() -> {
            // Actualizar tiempo de permanencia
            tarjetaTiempoMin.actualizarValor(resultados.getTiempoPermanciaMinimo(), formatoDecimal);
            tarjetaTiempoMax.actualizarValor(resultados.getTiempoPermanciaMaximo(), formatoDecimal);
            tarjetaTiempoMedia.actualizarValor(resultados.getTiempoPermaneciaMedio(), formatoDecimal);
            
            // Actualizar utilización de muelles
            double[] utilizacionMuelles = resultados.getUtilizacionMuelles();
            for (int i = 0; i < tarjetasMuelles.length && i < utilizacionMuelles.length; i++) {
                tarjetasMuelles[i].actualizarValor(utilizacionMuelles[i], formatoDecimal);
            }
            
            // Actualizar utilización de grúas
            double[] utilizacionGruas = resultados.getUtilizacionGruas();
            for (int i = 0; i < tarjetasGruas.length && i < utilizacionGruas.length; i++) {
                tarjetasGruas[i].actualizarValor(utilizacionGruas[i], formatoDecimal);
            }
        });
    }
    
    /**
     * Actualiza solo los valores de tiempo de permanencia
     */
    public void actualizarTiempoPermanencia(String minimo, String maximo, String medio) {
        SwingUtilities.invokeLater(() -> {
            tarjetaTiempoMin.actualizarValor(minimo);
            tarjetaTiempoMax.actualizarValor(maximo);
            tarjetaTiempoMedia.actualizarValor(medio);
        });
    }
    
    /**
     * Actualiza las utilizaciones de muelles y grúas
     */
    public void actualizarUtilizaciones(String[] muelles, String[] gruas) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < tarjetasMuelles.length && i < muelles.length; i++) {
                tarjetasMuelles[i].actualizarValor(muelles[i]);
            }
            
            for (int i = 0; i < tarjetasGruas.length && i < gruas.length; i++) {
                tarjetasGruas[i].actualizarValor(gruas[i]);
            }
        });
    }
    
    /**
     * Limpia todos los resultados
     */
    public void limpiarResultados() {
        SwingUtilities.invokeLater(() -> {
            // Resetear tiempo de permanencia
            tarjetaTiempoMin.resetear();
            tarjetaTiempoMax.resetear();
            tarjetaTiempoMedia.resetear();
            
            // Resetear utilización
            for (TarjetaEstadistica tarjeta : tarjetasMuelles) {
                if (tarjeta != null) tarjeta.resetear();
            }
            for (TarjetaEstadistica tarjeta : tarjetasGruas) {
                if (tarjeta != null) tarjeta.resetear();
            }
        });
    }
    
    /**
     * Resetea todas las tarjetas a su estado inicial
     * @deprecated Use limpiarResultados() instead
     */
    @Deprecated
    public void resetearTodas() {
        limpiarResultados();
    }
    
    // =============== GETTERS PARA ACCESO DIRECTO (SI ES NECESARIO) ===============
    
    public TarjetaEstadistica getTarjetaTiempoMin() { return tarjetaTiempoMin; }
    public TarjetaEstadistica getTarjetaTiempoMax() { return tarjetaTiempoMax; }
    public TarjetaEstadistica getTarjetaTiempoMedia() { return tarjetaTiempoMedia; }
    public TarjetaEstadistica[] getTarjetasMuelles() { return tarjetasMuelles; }
    public TarjetaEstadistica[] getTarjetasGruas() { return tarjetasGruas; }
}
