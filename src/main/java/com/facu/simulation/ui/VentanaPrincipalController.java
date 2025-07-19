package com.facu.simulation.ui;

import com.facu.simulation.engine.ConfiguracionSimulacion;
import com.facu.simulation.engine.Simulador;
import com.facu.simulation.dto.FilaVectorDTO;
import com.facu.simulation.dto.ResultadosSimulacionDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.controlsfx.control.ToggleSwitch;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Controlador para la interfaz de usuario definida en VentanaPrincipal.fxml.
 * Maneja todos los eventos de la UI y la lógica de presentación.
 */
public class VentanaPrincipalController {

    // =============== COMPONENTES FXML ===============
    @FXML private TextField txtTiempoDescargaMin;
    @FXML private TextField txtTiempoDescargaMax;
    @FXML private TextField txtMediaLlegada;
    @FXML private TextField txtDiasSimulacion;
    @FXML private TextField txtMostrarDesde;
    @FXML private TextField txtMostrarHasta;
    @FXML private TextField txtMostrarFilaDesde;
    @FXML private TextField txtMostrarFilaHasta;
    @FXML private ToggleSwitch toggleFiltrarPorFila;
    @FXML private Button btnSimular;
    @FXML private Button btnReset;
    @FXML private Label lblTiempoPermanciaMin;
    @FXML private Label lblTiempoPermanciaMax;
    @FXML private Label lblTiempoPermanciaMedia;
    @FXML private Label lblUtilizacionMuelle1;
    @FXML private Label lblUtilizacionMuelle2;
    @FXML private Label lblUtilizacionGrua1;
    @FXML private Label lblUtilizacionGrua2;
    @FXML private Label lblParamDescMin;
    @FXML private Label lblParamDescMax;
    @FXML private Label lblParamMediaLlegada;
    @FXML private Label lblParamDiasSimulacion;
    @FXML private Label lblParamRango;
    @FXML private TableView<FilaVectorDTO> tablaVectorEstado;
    @FXML private HBox panelFiltroDia;
    @FXML private HBox panelFiltroFila;
    @FXML private ProgressIndicator progressIndicator;
    @FXML private GridPane panelConfiguracion;

    private final GeneradorColumnasFX generadorColumnas = new GeneradorColumnasFX();
    private final DecimalFormat formatoDecimal = new DecimalFormat("0.00");

    // Valores por defecto para el reseteo
    private final String[] VALORES_DEFAULT = {"4", "6", "1.25", "90", "0", "15", "0", "80"};

    /**
     * Se llama automáticamente después de que el archivo FXML ha sido cargado.
     */
    @FXML
    public void initialize() {
        configurarEstadoInicial();
        configurarEventos();
        // Configurar tabla para mejor rendimiento
        tablaVectorEstado.setRowFactory(tv -> {
            TableRow<FilaVectorDTO> row = new TableRow<>();
            row.setStyle("-fx-border-width: 0;"); // Reducir overhead de bordes
            return row;
        });
    }

    /**
     * Configura el estado inicial de los componentes de la UI.
     */
    private void configurarEstadoInicial() {
        resetearCampos();
        generadorColumnas.crearColumnasBase(tablaVectorEstado);
        progressIndicator.setVisible(false);
    }

    /**
     * Configura los listeners y bindings para los eventos de la UI.
     */
    private void configurarEventos() {
        btnSimular.setOnAction(e -> ejecutarSimulacion());
        btnReset.setOnAction(e -> resetearCampos());
        toggleFiltrarPorFila.selectedProperty().addListener((obs, oldVal, newVal) -> actualizarEstadoCamposFiltrado());

        // Validar que solo se ingresen números en los campos de texto
        validarInputNumerico(txtTiempoDescargaMin);
        validarInputNumerico(txtTiempoDescargaMax);
        validarInputNumerico(txtMediaLlegada);
        validarInputNumerico(txtDiasSimulacion);
        validarInputNumerico(txtMostrarDesde);
        validarInputNumerico(txtMostrarHasta);
        validarInputNumerico(txtMostrarFilaDesde);
        validarInputNumerico(txtMostrarFilaHasta);
    }

    /**
     * Añade un listener a un TextField para permitir solo números y un punto decimal.
     */
    private void validarInputNumerico(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                textField.setText(oldValue);
            }
        });
    }

    /**
     * Ejecuta la simulación en un hilo separado para no bloquear la UI.
     */
    private void ejecutarSimulacion() {
        try {
            ConfiguracionSimulacion config = capturarConfiguracionDeUI();
            actualizarPanelParametros(config);
            mostrarEstadoSimulando(true);

            // Tarea en segundo plano para la simulación
            Task<ResultadosSimulacionDTO> task = new Task<>() {
                @Override
                protected ResultadosSimulacionDTO call() {
                    Simulador simulador = new Simulador(config);
                    return simulador.run();
                }
            };

            // Qué hacer cuando la tarea termina exitosamente
            task.setOnSucceeded(event -> {
                ResultadosSimulacionDTO resultados = task.getValue();
                actualizarPanelDeResultados(resultados);
                poblarTablaVectorDeEstado(resultados.getFilasTabla(), resultados.getMaxBarcosEnSistema());
                mostrarEstadoSimulando(false);
            });

            // Qué hacer si la tarea falla
            task.setOnFailed(event -> {
                mostrarEstadoSimulando(false);
                mostrarError("Error en la Simulación", "Ocurrió un error inesperado durante la ejecución: " + task.getException().getMessage());
                task.getException().printStackTrace();
            });

            new Thread(task).start();

        } catch (NumberFormatException ex) {
            mostrarError("Error en Parámetros", "Por favor, revise los valores ingresados. " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            mostrarError("Error de Configuración", ex.getMessage());
        }
    }

    /**
     * Captura la configuración desde la UI. Mantiene la misma lógica de validación.
     */
    private ConfiguracionSimulacion capturarConfiguracionDeUI() throws NumberFormatException, IllegalArgumentException {
        double tiempoDescargaMin = Double.parseDouble(txtTiempoDescargaMin.getText().trim());
        double tiempoDescargaMax = Double.parseDouble(txtTiempoDescargaMax.getText().trim());
        double mediaLlegada = Double.parseDouble(txtMediaLlegada.getText().trim());
        int diasSimulacion = Integer.parseInt(txtDiasSimulacion.getText().trim());
        int mostrarDesde = Integer.parseInt(txtMostrarDesde.getText().trim());
        int mostrarHasta = Integer.parseInt(txtMostrarHasta.getText().trim());
        int mostrarFilaDesde = Integer.parseInt(txtMostrarFilaDesde.getText().trim());
        int mostrarFilaHasta = Integer.parseInt(txtMostrarFilaHasta.getText().trim());
        boolean filtrarPorFila = toggleFiltrarPorFila.isSelected();
        

        if (tiempoDescargaMin <= 0 || tiempoDescargaMax <= 0 || tiempoDescargaMin >= tiempoDescargaMax) {
            throw new IllegalArgumentException("Los tiempos de descarga deben ser positivos y Min < Max.");
        }
        if (mediaLlegada <= 0) {
            throw new IllegalArgumentException("La media de llegada debe ser positiva.");
        }
        if (diasSimulacion <= 0) {
            throw new IllegalArgumentException("Los días de simulación deben ser positivos.");
        }

        if (filtrarPorFila) {
            if (mostrarFilaDesde < 0 || mostrarFilaHasta <= mostrarFilaDesde) {
                throw new IllegalArgumentException("El rango de filas para mostrar no es válido.");
            }
            return new ConfiguracionSimulacion(mediaLlegada, tiempoDescargaMin, tiempoDescargaMax, 2, 2, diasSimulacion, mostrarFilaDesde, mostrarFilaHasta, true);
        } else {
            if (mostrarDesde < 0 || mostrarHasta <= mostrarDesde) {
                throw new IllegalArgumentException("El rango de días para mostrar no es válido.");
            }
            return new ConfiguracionSimulacion(mediaLlegada, tiempoDescargaMin, tiempoDescargaMax, 2, 2, diasSimulacion, mostrarDesde, mostrarHasta);
        }
    }

    /**
     * Actualiza los labels de resultados con los datos de la simulación.
     */
    private void actualizarPanelDeResultados(ResultadosSimulacionDTO resultados) {
        lblTiempoPermanciaMin.setText(formatoDecimal.format(resultados.getTiempoPermanciaMinimo()));
        lblTiempoPermanciaMax.setText(formatoDecimal.format(resultados.getTiempoPermanciaMaximo()));
        lblTiempoPermanciaMedia.setText(formatoDecimal.format(resultados.getTiempoPermaneciaMedio()));
        lblUtilizacionMuelle1.setText(formatoDecimal.format(resultados.getUtilizacionMuelles()[0]));
        lblUtilizacionMuelle2.setText(formatoDecimal.format(resultados.getUtilizacionMuelles()[1]));
        lblUtilizacionGrua1.setText(formatoDecimal.format(resultados.getUtilizacionGruas()[0]));
        lblUtilizacionGrua2.setText(formatoDecimal.format(resultados.getUtilizacionGruas()[1]));
    }

    /**
     * Llena la TableView con las filas de resultados de forma optimizada.
     */
    private void poblarTablaVectorDeEstado(List<FilaVectorDTO> filasDTO, int maxBarcosEnSistema) {
        // Actualizar columnas si es necesario
        generadorColumnas.actualizarColumnasDeBarcos(tablaVectorEstado, maxBarcosEnSistema);

        // Usar setAll para mejor rendimiento en actualizaciones
        ObservableList<FilaVectorDTO> items = tablaVectorEstado.getItems();
        if (items == null) {
            items = FXCollections.observableArrayList();
            tablaVectorEstado.setItems(items);
        }
        items.setAll(filasDTO);
    }

    /**
     * Cambia la UI para mostrar que la simulación está en progreso.
     */
    private void mostrarEstadoSimulando(boolean simulando) {
        progressIndicator.setVisible(simulando);
        btnSimular.setDisable(simulando);
        panelConfiguracion.setDisable(simulando);
    }

    /**
     * Muestra un diálogo de alerta de error.
     */
    private void mostrarError(String titulo, String mensaje) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(titulo);
            alert.setHeaderText(null);
            alert.setContentText(mensaje);
            alert.showAndWait();
        });
    }

    /**
     * Resetea todos los campos a sus valores por defecto.
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
        toggleFiltrarPorFila.setSelected(false);
        actualizarEstadoCamposFiltrado();

        lblTiempoPermanciaMin.setText("--");
        lblTiempoPermanciaMax.setText("--");
        lblTiempoPermanciaMedia.setText("--");
        lblUtilizacionMuelle1.setText("--");
        lblUtilizacionMuelle2.setText("--");
        lblUtilizacionGrua1.setText("--");
        lblUtilizacionGrua2.setText("--");
        lblParamDescMin.setText("--");
        lblParamDescMax.setText("--");
        lblParamMediaLlegada.setText("--");
        lblParamDiasSimulacion.setText("--");
        lblParamRango.setText("--");

        tablaVectorEstado.getItems().clear();
        generadorColumnas.crearColumnasBase(tablaVectorEstado); // Recrea columnas base
    }

    /**
     * Habilita o deshabilita los campos de filtrado según el checkbox.
     */
    private void actualizarEstadoCamposFiltrado() {
        boolean filtrarPorFila = toggleFiltrarPorFila.isSelected();
        panelFiltroDia.setDisable(filtrarPorFila);
        panelFiltroFila.setDisable(!filtrarPorFila);
    }
    
    /**
     * Actualiza el panel de parámetros con los valores de configuración.
     */
    private void actualizarPanelParametros(ConfiguracionSimulacion config) {
        lblParamDescMin.setText(formatoDecimal.format(config.getTiempoDescargaMin()));
        lblParamDescMax.setText(formatoDecimal.format(config.getTiempoDescargaMax()));
        lblParamMediaLlegada.setText(formatoDecimal.format(config.getMediaLlegadas()));
        lblParamDiasSimulacion.setText(String.valueOf(config.getDiasSimulacion()));
        
        String rango;
        if (!config.isMostrarPorDia()) {
            rango = "Filas " + config.getMostrarFilaDesde() + "-" + config.getMostrarFilaHasta();
        } else {
            rango = "Días " + config.getMostrarDesde() + "-" + config.getMostrarHasta();
        }
        lblParamRango.setText(rango);
    }
    
    /**
     * Añade un listener a un TextField para permitir solo números enteros.
     */
    private void validarInputNumericoEntero(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(oldValue);
            }
        });
    }
}