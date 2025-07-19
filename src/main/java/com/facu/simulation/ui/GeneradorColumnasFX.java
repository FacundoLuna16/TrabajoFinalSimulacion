package com.facu.simulation.ui;

import com.facu.simulation.dto.BarcoSlotDTO;
import com.facu.simulation.dto.FilaVectorDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Clase de utilidad para generar las columnas jerárquicas en una TableView de JavaFX.
 * Mantiene la misma lógica de qué columnas mostrar.
 */
public class GeneradorColumnasFX {

    private final DecimalFormat formatoTiempo = new DecimalFormat("0.00");
    private final DecimalFormat formatoRnd = new DecimalFormat("0.0000");
    private final DecimalFormat formatoPorcentaje = new DecimalFormat("0.00'%'");

    /**
     * Crea todas las columnas base y jerárquicas en la tabla.
     */
    public void crearColumnasBase(TableView<FilaVectorDTO> tabla) {
        tabla.getColumns().clear(); // Limpiar columnas existentes

        // --- Grupo Control ---
        TableColumn<FilaVectorDTO, String> colControl = new TableColumn<>("Control");
        colControl.getColumns().addAll(
                crearColumna("Fila", 60, f -> String.valueOf(f.getNumeroFila()), "celda-control"),
                crearColumna("Evento", 120, FilaVectorDTO::getEvento, "celda-control"),
                crearColumna("Reloj", 80, f -> formatear(f.getTiempo(), formatoTiempo), "celda-control")
        );
        colControl.getStyleClass().add("grupo-control");

        // --- Grupo Llegada ---
        TableColumn<FilaVectorDTO, String> colLlegada = new TableColumn<>("Llegada Barco");
        colLlegada.getColumns().addAll(
                crearColumna("RND", 80, f -> formatear(f.getRndLlegada(), formatoRnd), "celda-llegada"),
                crearColumna("Próx. Llegada", 100, f -> formatear(f.getProximaLlegada(), formatoTiempo), "celda-llegada")
        );
        colLlegada.getStyleClass().add("grupo-llegada");

        // --- Grupo Descarga ---
        TableColumn<FilaVectorDTO, String> colMuelle1Desc = new TableColumn<>("Descarga Muelle 1");
        colMuelle1Desc.getColumns().addAll(
                crearColumna("RND", 80, f -> formatear(f.getRndDescargaMuelle1(), formatoRnd), "celda-descarga-m1"),
                crearColumna("T. Restante", 100, f -> formatear(f.getTiempoRestanteMuelle1(), formatoTiempo), "celda-descarga-m1"),
                crearColumna("Fin Descarga", 100, f -> formatear(f.getFinDescarga1(), formatoTiempo), "celda-descarga-m1")
        );
        colMuelle1Desc.getStyleClass().add("subgrupo-descarga-m1");

        TableColumn<FilaVectorDTO, String> colMuelle2Desc = new TableColumn<>("Descarga Muelle 2");
        colMuelle2Desc.getColumns().addAll(
                crearColumna("RND", 80, f -> formatear(f.getRndDescargaMuelle2(), formatoRnd), "celda-descarga-m2"),
                crearColumna("T. Restante", 100, f -> formatear(f.getTiempoRestanteMuelle2(), formatoTiempo), "celda-descarga-m2"),
                crearColumna("Fin Descarga", 100, f -> formatear(f.getFinDescarga2(), formatoTiempo), "celda-descarga-m2")
        );
        colMuelle2Desc.getStyleClass().add("subgrupo-descarga-m2");

        // --- Grupo Bahía y Estados ---
        TableColumn<FilaVectorDTO, String> colBahia = new TableColumn<>("Bahía");
        colBahia.getColumns().add(crearColumna("Cola", 60, f -> String.valueOf(f.getCantidadBarcosBahia()), "celda-bahia"));
        colBahia.getStyleClass().add("grupo-bahia");

        TableColumn<FilaVectorDTO, String> colMuellesEst = new TableColumn<>("Estados Muelles");
        colMuellesEst.getColumns().addAll(
                crearColumna("M1 Estado", 100, FilaVectorDTO::getMuelle1Estado, "celda-estados-muelles"),
                crearColumna("M1 Inicio Ocup.", 100, f -> formatear(f.getMuelle1InicioOcupado(), formatoTiempo), "celda-estados-muelles"),
                crearColumna("M2 Estado", 100, FilaVectorDTO::getMuelle2Estado, "celda-estados-muelles"),
                crearColumna("M2 Inicio Ocup.", 100, f -> formatear(f.getMuelle2InicioOcupado(), formatoTiempo), "celda-estados-muelles")
        );
        colMuellesEst.getStyleClass().add("subgrupo-estados-muelles");

        TableColumn<FilaVectorDTO, String> colGruasEst = new TableColumn<>("Estados Grúas");
        colGruasEst.getColumns().addAll(
                crearColumna("G1 Estado", 100, FilaVectorDTO::getGrua1Estado, "celda-estados-gruas"),
                crearColumna("G1 Inicio Ocup.", 100, f -> formatear(f.getGrua1InicioOcupado(), formatoTiempo), "celda-estados-gruas"),
                crearColumna("G2 Estado", 100, FilaVectorDTO::getGrua2Estado, "celda-estados-gruas"),
                crearColumna("G2 Inicio Ocup.", 100, f -> formatear(f.getGrua2InicioOcupado(), formatoTiempo), "celda-estados-gruas")
        );
        colGruasEst.getStyleClass().add("subgrupo-estados-gruas");

        // --- Grupo Estadísticas ---
        TableColumn<FilaVectorDTO, String> colTPerm = new TableColumn<>("T. Permanencia");
        colTPerm.getColumns().addAll(
                crearColumna("Máx", 80, f -> formatear(f.getMaxTiempoPermanencia(), formatoTiempo), "celda-stats-tperm"),
                crearColumna("Mín", 80, f -> formatear(f.getMinTiempoPermanencia(), formatoTiempo), "celda-stats-tperm"),
                crearColumna("Acum.", 80, f -> formatear(f.getAcumuladorTiempoEsperaBahia(), formatoTiempo), "celda-stats-tperm"),
                crearColumna("Cant. Barcos", 100, f -> String.valueOf(f.getContadorBarcosQueEsperaronEnBahia()), "celda-stats-tperm"),
                crearColumna("Media", 80, f -> formatear(f.getMediaTiempoPermanencia(), formatoTiempo), "celda-stats-tperm")
        );
        colTPerm.getStyleClass().add("subgrupo-stats-tperm");

        TableColumn<FilaVectorDTO, String> colUtil = new TableColumn<>("Utilización");
        colUtil.getColumns().addAll(
                crearColumna("M1 Ac. T.O.", 100, f -> formatear(f.getMuelle1AcTiempoOcupado(), formatoTiempo), "celda-stats-util"),
                crearColumna("M1 %", 80, f -> formatear(f.getMuelle1Utilizacion(), formatoPorcentaje), "celda-stats-util"),
                crearColumna("M2 Ac. T.O.", 100, f -> formatear(f.getMuelle2AcTiempoOcupado(), formatoTiempo), "celda-stats-util"),
                crearColumna("M2 %", 80, f -> formatear(f.getMuelle2Utilizacion(), formatoPorcentaje), "celda-stats-util"),
                crearColumna("G1 Ac. T.O.", 100, f -> formatear(f.getGrua1AcTiempoOcupado(), formatoTiempo), "celda-stats-util"),
                crearColumna("G1 %", 80, f -> formatear(f.getGrua1Utilizacion(), formatoPorcentaje), "celda-stats-util"),
                crearColumna("G2 Ac. T.O.", 100, f -> formatear(f.getGrua2AcTiempoOcupado(), formatoTiempo), "celda-stats-util"),
                crearColumna("G2 %", 80, f -> formatear(f.getGrua2Utilizacion(), formatoPorcentaje), "celda-stats-util")
        );
        colUtil.getStyleClass().add("subgrupo-stats-util");

        // --- Columna Cantidad Barcos en Sistema ---
        TableColumn<FilaVectorDTO, String> colCantBarcos = new TableColumn<>("Barcos en Sistema");
        colCantBarcos.getColumns().add(crearColumna("Total", 80, f -> String.valueOf(f.getCantBarcosEnSistema()), "celda-barcos"));
        colCantBarcos.getStyleClass().add("grupo-barcos");

        tabla.getColumns().addAll(colControl, colLlegada, colMuelle1Desc, colMuelle2Desc, colBahia, colMuellesEst, colGruasEst, colTPerm, colUtil, colCantBarcos);
    }

    private int ultimoMaxBarcosEnSistema = -1;

    /**
     * Actualiza las columnas dinámicas de los barcos solo si es necesario.
     */
    public void actualizarColumnasDeBarcos(TableView<FilaVectorDTO> tabla, int maxBarcosEnSistema) {
        // Solo actualizar si cambió el número máximo de barcos
        if (ultimoMaxBarcosEnSistema == maxBarcosEnSistema) {
            return;
        }
        
        ultimoMaxBarcosEnSistema = maxBarcosEnSistema;
        
        // Eliminar columnas de barcos anteriores
        tabla.getColumns().removeIf(col -> col.getText().startsWith("B"));

        if (maxBarcosEnSistema > 0) {
            for (int i = 1; i <= maxBarcosEnSistema; i++) {
                final int slotIndex = i;
                TableColumn<FilaVectorDTO, String> colBarcoSlot = new TableColumn<>("B" + slotIndex);
                colBarcoSlot.getColumns().addAll(
                        crearColumna("ID", 60, f -> obtenerInfoBarco(f, slotIndex, "id"), "celda-barcos"),
                        crearColumna("Estado", 80, f -> obtenerInfoBarco(f, slotIndex, "estado"), "celda-barcos"),
                        crearColumna("T. Ingreso", 100, f -> obtenerInfoBarco(f, slotIndex, "ingreso"), "celda-barcos")
                );
                colBarcoSlot.getStyleClass().add("grupo-barcos");
                tabla.getColumns().add(colBarcoSlot);
            }
        }
    }

    /**
     * Método de utilidad para crear una columna de tabla con optimizaciones.
     */
    private <T> TableColumn<T, String> crearColumna(String titulo, double ancho, java.util.function.Function<T, String> extractor, String cellStyleClass) {
        TableColumn<T, String> columna = new TableColumn<>(titulo);
        columna.setPrefWidth(ancho);
        columna.setCellValueFactory(cellData -> new SimpleStringProperty(extractor.apply(cellData.getValue())));
        columna.setCellFactory(tc -> new OptimizedTableCell<>(cellStyleClass));
        columna.setSortable(false);
        return columna;
    }
    
    /**
     * Celda optimizada que minimiza actualizaciones de estilo.
     */
    private static class OptimizedTableCell<T> extends TableCell<T, String> {
        private final String cellStyleClass;
        private boolean styleClassAdded = false;
        
        public OptimizedTableCell(String cellStyleClass) {
            this.cellStyleClass = cellStyleClass;
        }
        
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                if (styleClassAdded) {
                    getStyleClass().remove(cellStyleClass);
                    styleClassAdded = false;
                }
            } else {
                setText(item);
                if (!styleClassAdded) {
                    getStyleClass().add(cellStyleClass);
                    styleClassAdded = true;
                }
            }
        }
    }

    /**
     * Formatea un valor double, devolviendo "" si es -1.
     */
    private String formatear(double valor, DecimalFormat formato) {
        return (valor == -1.0 || valor == 0.0) ? "" : formato.format(valor);
    }

    /**
     * Obtiene información específica de un barco en un slot.
     */
    private String obtenerInfoBarco(FilaVectorDTO fila, int slot, String tipoInfo) {
        List<BarcoSlotDTO> barcos = fila.getBarcosEnSistema();
        if (barcos == null) return "";

        return barcos.stream()
                .filter(b -> b.getSlotAsignado() == slot)
                .map(b -> {
                    switch (tipoInfo) {
                        case "id": return String.valueOf(b.getId());
                        case "estado":
                            return (fila.getTiempo() < b.getTiempoInicioDescarga()) ? "EB" : "SD";
                        case "ingreso": return formatear(b.getTiempoIngreso(), formatoTiempo);
                        default: return "";
                    }
                })
                .findFirst()
                .orElse("");
    }
}