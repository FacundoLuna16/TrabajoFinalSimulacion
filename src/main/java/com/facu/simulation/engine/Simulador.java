package com.facu.simulation.engine;

import com.facu.simulation.events.Evento;
import com.facu.simulation.events.LlegadaBarco;
import com.facu.simulation.events.FinDescarga;
import com.facu.simulation.model.*;
import com.facu.simulation.generator.GeneradorAleatorio;
import com.facu.simulation.dto.FilaVectorDTO;
import com.facu.simulation.dto.ResultadosSimulacionDTO;
import lombok.Data;

import java.util.*;

/**
 * Motor central de la simulación de eventos discretos.
 * Gestiona el tiempo, la lista de eventos futuros (FEL) y coordina
 * todos los elementos del sistema portuario.
 */
@Data
public class Simulador {

    private PriorityQueue<Evento> fel;
    private ConfiguracionSimulacion configuracion;
    private double reloj = 0.0; // Reloj del sistema, inicia en 0.0

    // Objetos del sistema
    private List<Muelle> muelles; // Lista de muelles disponibles en el puerto
    private List<Grua> gruas; // Lista de grúas disponibles en el puerto
    private Queue<Barco> bahia; // Cola de espera para barcos que no pueden ser atendidos inmediatamente

    // Estadísticas y contadores
    private double acumuladorTiempoEsperaBahia;
    private int contadorBarcosAtendidos;
    private int contadorBarcosGenerados;
    private List<FilaVector> vectoresEstado;
    private FilaVector ultimaFilaVector;
    private FilaVector actualFilaVector; // en esta voy armando la fila actual.

    //solo usados para inicializar.
    private double ultimoRndLlegada = -1; // Último RND de llegada generado
    // Generadores de números aleatorios
    private GeneradorAleatorio generador;

    /**
     * Constructor del simulador.
     * Inicializa todos los componentes y programa el primer evento.
     */
    public Simulador(ConfiguracionSimulacion configuracion) {
        this.configuracion = configuracion;
        this.generador = new GeneradorAleatorio(12345); // Usar semilla fija para reproducibilidad

        // Inicializar estructuras de datos
        this.fel = new PriorityQueue<>(); //
        this.muelles = new ArrayList<>();
        this.gruas = new ArrayList<>();
        this.bahia = new LinkedList<>();
        this.vectoresEstado = new ArrayList<>();

        // Inicializar contadores y acumuladores
        this.acumuladorTiempoEsperaBahia = 0.0;
        this.contadorBarcosAtendidos = 0;
        this.contadorBarcosGenerados = 0;

        // Crear los recursos del puerto
        inicializarRecursos();

        // Programar el primer evento
        programarPrimeraLlegada();
    }

    /**
     * Crea las instancias de muelles y grúas según la configuración.
     */
    private void inicializarRecursos() {
        // Crear muelles
        for (int i = 1; i <= configuracion.getCantidadMuelles(); i++) {
            Muelle muelle = new Muelle(i);
            muelles.add(muelle);
        }

        // Crear grúas
        for (int i = 1; i <= configuracion.getCantidadGruas(); i++) {
            Grua grua = new Grua(i);
            gruas.add(grua);
        }
    }

    /**
     * Programa la primera llegada de barco para iniciar la simulación.
     */
    private void programarPrimeraLlegada() {
        // Generar número aleatorio y tiempo
        double RndLlegada = generador.generarNumeroAleatorio();
        this.ultimoRndLlegada = RndLlegada;
        // convertir en tiempo exponencial negativo
        double tiempoLlegada = generador.convertirAExponencial(RndLlegada, configuracion.getMediaLlegadas());


        Barco primerBarco = new Barco(1, tiempoLlegada, 0.0);
        this.contadorBarcosGenerados ++;

        Evento primeraLlegada = new LlegadaBarco(tiempoLlegada, primerBarco);
        fel.add(primeraLlegada);
    }

    /**
     * Bucle principal de la simulación.
     * Ejecuta eventos en orden cronológico hasta que se cumple la condición de parada.
     *
     * @return ResultadosSimulacionDTO con todos los datos para la interfaz gráfica
     */
    public ResultadosSimulacionDTO run() {
        int numeroFila = 0;
        double tiempoMaximo = configuracion.getDiasSimulacion();
        
        // SIEMPRE generar la fila de inicialización para evitar NullPointerException
        FilaVector filaInicializacion = generarFilaInicializacion(numeroFila);

        ultimaFilaVector = filaInicializacion; // Asegurar que ultimaFilaVector nunca sea null


        
        // Solo agregar a la lista si debe mostrarse
        if (debeMostrarFila(numeroFila)) {
            vectoresEstado.add(filaInicializacion);
        }
        
        numeroFila++;

        // Bucle principal
        while (true) {
            // Si la FEL está vacía o el próximo evento supera el tiempo máximo, terminamos
            if (fel.isEmpty()) break;

            // Obtener el evento más próximo
            Evento eventoActual = fel.peek();

            // Si el tiempo del evento supera el tiempo máximo, terminamos
            if (eventoActual.getTiempo() > tiempoMaximo) break;

            // Avanzar el reloj al tiempo del evento actual
            fel.poll();
            reloj = eventoActual.getTiempo();

            //Inicializar la fila actual
            this.actualFilaVector = new FilaVector(numeroFila, reloj, eventoActual.getClass().getSimpleName());

            // Procesar el evento actual
            eventoActual.procesar(this);

            //Filtrar y generar fila de estado
            if (debeMostrarFila(numeroFila)) {
                vectoresEstado.add(this.actualFilaVector);
            }
            numeroFila++;
            // Actualizar la última fila vector
            ultimaFilaVector = actualFilaVector;
        }

        // Procesamiento del último evento
        if (!fel.isEmpty()) {
            Evento eventoFinal = fel.poll();
            reloj = eventoFinal.getTiempo();
            eventoFinal.procesar(this);

            // La última iteración SIEMPRE se muestra, independientemente del rango configurado
            vectoresEstado.add(this.actualFilaVector);
        }
        
        // NUEVO: Usar el convertidor para retornar datos listos para la UI
        return ConvertidorDatosUI.convertirAResultadosDTO(vectoresEstado);
    }

    /**
     * Determina si una fila debe ser mostrada (y guardada) según la configuración.
     */
    private boolean debeMostrarFila(int numeroFila) {
        if (configuracion.isMostrarPorDia()) {
            return reloj >= configuracion.getMostrarDesde() && reloj <= configuracion.getMostrarHasta();
        } else {
            return numeroFila >= configuracion.getMostrarFilaDesde() && numeroFila <= configuracion.getMostrarFilaHasta();
        }
    }


    /**
     * Genera una fila vector que representa el estado inicial del sistema (INICIALIZACION).
     */
    private FilaVector generarFilaInicializacion(int numeroFila) {
        FilaVector fila = new FilaVector(numeroFila, reloj, "INICIALIZACION");

        //Se utilisa solo una vez esta funcion por eso usamos this.ultimoRndLlegada
        //PARTICULAR.

        // Llegada_Barco
        if (this.fel.peek() != null) {
            Evento primerEvento = this.fel.peek();
            fila.setProximaLlegada(primerEvento.getTiempo());
        }
        fila.setRndLlegada(this.ultimoRndLlegada);

        //Bahia COLA
        fila.setCantidadBarcosBahia(bahia.size()); // 0
        // Muelle 1
        fila.setMuelle1Estado(EstadoMuelle.LIBRE);
        // Muelle 2
        fila.setMuelle2Estado(EstadoMuelle.LIBRE);
        // Grúa 1
        fila.setGrua1Estado(EstadoGrua.LIBRE);
        // Grúa 2
        fila.setGrua2Estado(EstadoGrua.LIBRE);
        // Estadísticas
        fila.setMaxTiempoPermanencia(0.0); // 0.0
        //DEJAMOS EN NULL O EMPTY MIN PARA PREGUNTAR SI ES NULO PONEMOS EL PRIMER VALOR Y LISTO
        fila.setAcumuladorTiempoEsperaBahia(0);
        fila.setContadorBarcosAtendidos(0);

        // Recursos del puerto
        fila.setMuelle1AcTiempoOcupado(0.0);
        fila.setMuelle2AcTiempoOcupado(0.0);

        // Grúas
        fila.setGrua1AcTiempoOcupado(0.0);
        fila.setGrua2AcTiempoOcupado(0.0);
        // Variables de control
        fila.setCantBarcosEnSistema(0); // No hay barcos en el sistema al inicio
        fila.setCantMaxBarcosEnSistema(0);
        fila.setBarcosEnSistema(new ArrayList<>()); // Lista vacía al inicio

        return fila;
    }


    // ==================== MÉTODOS DE LÓGICA DE NEGOCIO ====================

    /**
     * Procesa la llegada de un barco al sistema.
     * Este método será llamado por el evento LlegadaBarco.
     */
    public void procesarLlegadaBarco(Barco barco) {

        // 1. PRIMERO: Programar la próxima llegada (para no olvidarlo)
        programarProximaLlegada(); //REVISADO

        // 2. Buscar muelle libre
        Muelle muelleLibre = buscarMuelleLibre();

        barco.setTiempoLlegadaSistema(reloj);

        cargarDatosPreviosFinLlegada();

        // Copiar métricas de tiempo de permanencia desde ultimaFilaVector
        if (ultimaFilaVector != null) {
            actualFilaVector.setMinTiempoPermanencia(ultimaFilaVector.getMinTiempoPermanencia());
            actualFilaVector.setMaxTiempoPermanencia(ultimaFilaVector.getMaxTiempoPermanencia());
            actualFilaVector.setAcumuladorTiempoEsperaBahia(ultimaFilaVector.getAcumuladorTiempoEsperaBahia());
            actualFilaVector.setContadorBarcosQueEsperanEnBahia(ultimaFilaVector.getContadorBarcosQueEsperanEnBahia());
            
            // Recalcular media con los datos actuales
            if (actualFilaVector.getContadorBarcosQueEsperanEnBahia() > 0) {
                double media = actualFilaVector.getAcumuladorTiempoEsperaBahia() / actualFilaVector.getContadorBarcosQueEsperanEnBahia();
                actualFilaVector.setMediaTiempoPermanencia(media);
            } else {
                actualFilaVector.setMediaTiempoPermanencia(0.0);
            }
        }

        // 3. Decidir el camino del barco
        if (muelleLibre != null) {
            // HAY MUELLE LIBRE

            // NUEVA LÓGICA: Generar RND específico según qué muelle se asigne
            barco.setEstado(EstadoBarco.SIENDO_DESCARGADO);
            double rndDescarga = generador.generarNumeroAleatorio();
            double tiempoDescargaBase = generador.convertirAUniforme(rndDescarga,
                    configuracion.getTiempoDescargaMin(), configuracion.getTiempoDescargaMax());
            barco.setTiempoDescargaRestante(tiempoDescargaBase);

            // Ocupar el muelle
            muelleLibre.ocuparMuelle(barco, reloj);

            // DETERMINAR QUÉ MUELLE SE ASIGNÓ Y GUARDAR RND CORRESPONDIENTE
            if (muelleLibre.getId() == 1) {
                // Se asignó al Muelle 1
                this.actualFilaVector.setMuelle1Estado(EstadoMuelle.OCUPADO);
                this.actualFilaVector.setMuelle1InicioOcupado(reloj);
                this.actualFilaVector.setRndDescargaMuelle1(rndDescarga);
                this.actualFilaVector.setFinDescarga1(tiempoDescargaBase + this.reloj);
                this.actualFilaVector.setTiempoRestanteMuelle1(tiempoDescargaBase);
            } else if (muelleLibre.getId() == 2) {
                // Se asignó al Muelle 2
                this.actualFilaVector.setMuelle2Estado(EstadoMuelle.OCUPADO);
                this.actualFilaVector.setMuelle2InicioOcupado(reloj);
                this.actualFilaVector.setRndDescargaMuelle2(rndDescarga);
                this.actualFilaVector.setFinDescarga2(tiempoDescargaBase + this.reloj);
                this.actualFilaVector.setTiempoRestanteMuelle2(tiempoDescargaBase);
            }

            // Llamar a la lógica de reasignación y recálculo
            reasignarGruasYRecalcularTiempos();

        } else {
            // NO HAY MUELLE LIBRE
            // Agregar el barco a la bahía
            barco.setEstado(EstadoBarco.EN_BAHIA);
            barco.setHoraLlegadaBahia(reloj);
            this.bahia.add(barco);
        }

        reasignarGruasYRecalcularTiempos();
        // 4. ACTUALIZAR ESTADO ACTUAL DEL SISTEMA EN LA FILA VECTOR
        actualizarEstadoSistemaEnFilaVector();
    }

    private void cargarDatosPreviosFinLlegada() {
        if (ultimaFilaVector != null) {
            double diferencia = reloj - this.ultimaFilaVector.getTiempo();
            if (this.ultimaFilaVector.getTiempoRestanteMuelle1() > 0) {
                double tiempoRestante = this.ultimaFilaVector.getTiempoRestanteMuelle1() - diferencia;
                this.actualFilaVector.setTiempoRestanteMuelle1(tiempoRestante);
                this.actualFilaVector.setFinDescarga1(this.reloj + tiempoRestante);
            }
            if (this.ultimaFilaVector.getTiempoRestanteMuelle2() > 0) {
                double tiempoRestante = this.ultimaFilaVector.getTiempoRestanteMuelle2() - diferencia;
                this.actualFilaVector.setTiempoRestanteMuelle2(tiempoRestante);
                this.actualFilaVector.setFinDescarga2(this.reloj + tiempoRestante);
            }

        }
    }

    /**
     * Procesa el fin de descarga de un barco.
     * Este método será llamado por el evento FinDescarga.
     */
    public void procesarFinDescarga(Barco barcoTerminado) {
        // 1. Liberar recursos: eliminar barco del muelle
        Muelle muelleOcupado = null;
        for (Muelle muelle : muelles) {
            if (muelle.getBarcoAtendido() != null &&
                    muelle.getBarcoAtendido().getId() == barcoTerminado.getId()) {
                muelleOcupado = muelle;
                break;
            }
        }

        if (muelleOcupado != null) {
            muelleOcupado.liberarMuelle(this.reloj);
        }

        contadorBarcosAtendidos++;

        this.actualFilaVector.setProximaLlegada(this.ultimaFilaVector.getProximaLlegada());
        // 2. Atender la cola (Bahía)
        if (!bahia.isEmpty() && muelleOcupado != null) {
            //
            Barco barcoEsperando = bahia.poll();
            // 3. Actualizar estadísticas
            double tiempoEnBahia = reloj - barcoEsperando.getHoraLlegadaBahia();

            // Se actualizan los acumuladores en la fila actual, leyendo desde la anterior.
            actualFilaVector.setAcumuladorTiempoEsperaBahia(ultimaFilaVector.getAcumuladorTiempoEsperaBahia() + tiempoEnBahia);

            actualFilaVector.setContadorBarcosQueEsperanEnBahia(ultimaFilaVector.getContadorBarcosQueEsperanEnBahia() + 1);
            // Actualizar min/max leyendo el histórico desde la última fila
            double minHistorico = ultimaFilaVector.getMinTiempoPermanencia();
            if (minHistorico == 0.0 || tiempoEnBahia < minHistorico) {
                actualFilaVector.setMinTiempoPermanencia(tiempoEnBahia);
            } else {
                actualFilaVector.setMinTiempoPermanencia(minHistorico);
            }

            if (tiempoEnBahia > ultimaFilaVector.getMaxTiempoPermanencia()) {
                actualFilaVector.setMaxTiempoPermanencia(tiempoEnBahia);
            } else {
                actualFilaVector.setMaxTiempoPermanencia(ultimaFilaVector.getMaxTiempoPermanencia());
            }

            // Hay barcos esperando y hay un muelle libre

            barcoEsperando.setEstado(EstadoBarco.SIENDO_DESCARGADO);

            // Generar RND específico según qué muelle se libera
            double rndDescarga = generador.generarNumeroAleatorio();
            double tiempoDescargaBase = generador.convertirAUniforme(rndDescarga,
                    configuracion.getTiempoDescargaMin(), configuracion.getTiempoDescargaMax());

            barcoEsperando.setTiempoDescargaRestante(tiempoDescargaBase);

            // Ocupar el muelle
            muelleOcupado.ocuparMuelle(barcoEsperando, reloj);

            // DETERMINAR QUÉ MUELLE SE LIBERÓ Y GUARDAR RND CORRESPONDIENTE
            if (muelleOcupado.getId() == 1) {
                this.actualFilaVector.setMuelle1Estado(EstadoMuelle.OCUPADO);
                this.actualFilaVector.setMuelle1InicioOcupado(reloj);
                this.actualFilaVector.setRndDescargaMuelle1(rndDescarga);
                this.actualFilaVector.setFinDescarga1(tiempoDescargaBase + this.reloj);
                this.actualFilaVector.setTiempoRestanteMuelle1(tiempoDescargaBase);
            } else if (muelleOcupado.getId() == 2) {
                this.actualFilaVector.setMuelle2Estado(EstadoMuelle.OCUPADO);
                this.actualFilaVector.setMuelle2InicioOcupado(reloj);
                this.actualFilaVector.setRndDescargaMuelle2(rndDescarga);
                this.actualFilaVector.setFinDescarga2(tiempoDescargaBase + this.reloj);
                this.actualFilaVector.setTiempoRestanteMuelle2(tiempoDescargaBase);
            }
        }

        // 4. Llamar a la lógica de reasignación
        reasignarGruasYRecalcularTiempos();

        // 5. Actualizar estado del sistema
        actualizarEstadoSistemaEnFilaVector();
    }

    private void asignarGruasABarco(Barco barco, int cantidadGruas) {
        Muelle muelleDelBarco = buscarMuellePorBarco(barco);
        if (muelleDelBarco == null) return;

        int gruasAsignadas = 0;
        for (Grua grua : gruas) {
            if (grua.estaLibre() && gruasAsignadas < cantidadGruas) {
                grua.setEstado(EstadoGrua.OCUPADA);
                grua.setBarcoAsignado(barco);
                grua.setTiempoInicioOcupado(reloj); // ¡ESTO ES LO QUE FALTABA!
                muelleDelBarco.asignarGrua();
                gruasAsignadas++;
                if (grua.getId() == 1) {
                    this.actualFilaVector.setGrua1Estado(EstadoGrua.OCUPADA);
                    this.actualFilaVector.setGrua1InicioOcupado(reloj);
                } else if (grua.getId() == 2) {
                    this.actualFilaVector.setGrua2Estado(EstadoGrua.OCUPADA);
                    this.actualFilaVector.setGrua2InicioOcupado(reloj);
                }
            }
        }
    }
    private void actualizarEstadisticasBarcos() {
        // El contador de barcos atendidos se actualiza aquí para todos los casos
        actualFilaVector.setContadorBarcosAtendidos(ultimaFilaVector.getContadorBarcosAtendidos());
        if (actualFilaVector.getEvento().equals("FinDescarga")) {
            actualFilaVector.setContadorBarcosAtendidos(actualFilaVector.getContadorBarcosAtendidos() + 1);
        }

        // Se copian las estadísticas de la fila anterior por si no hay cambios en este evento
        if (!actualFilaVector.getEvento().equals("FinDescarga") || bahia.isEmpty()) {
            actualFilaVector.setAcumuladorTiempoEsperaBahia(ultimaFilaVector.getAcumuladorTiempoEsperaBahia());
            actualFilaVector.setContadorBarcosQueEsperanEnBahia(ultimaFilaVector.getContadorBarcosQueEsperanEnBahia());
            actualFilaVector.setMinTiempoPermanencia(ultimaFilaVector.getMinTiempoPermanencia());
            actualFilaVector.setMaxTiempoPermanencia(ultimaFilaVector.getMaxTiempoPermanencia());
        }

        // Calcular media de tiempo de permanencia en bahía
        if (actualFilaVector.getContadorBarcosQueEsperanEnBahia() > 0) {
            double media = actualFilaVector.getAcumuladorTiempoEsperaBahia() / actualFilaVector.getContadorBarcosQueEsperanEnBahia();
            actualFilaVector.setMediaTiempoPermanencia(media);
        } else {
            actualFilaVector.setMediaTiempoPermanencia(0.0);
        }
    }

    // Función auxiliar que faltaba
    private Muelle buscarMuellePorBarco(Barco barco) {
        for (Muelle muelle : muelles) {
            if (muelle.getBarcoAtendido() != null && muelle.getBarcoAtendido().getId() == barco.getId()) {
                return muelle;
            }
        }
        return null;
    }
    // ==================== EL CORAZÓN DE LA LÓGICA ====================

    /**
     * Reasigna las grúas y recalcula los tiempos de finalización de todos los barcos en servicio.
     * Esta es la versión corregida y final.
     */
    private void reasignarGruasYRecalcularTiempos() {
        // 1. ACTUALIZAR TRABAJO REALIZADO DESDE EL ÚLTIMO EVENTO
        double tiempoTranscurrido = reloj - this.ultimaFilaVector.getTiempo();
        if (tiempoTranscurrido > 0) {
            // Usamos el estado guardado en la última fila para saber cómo estaba el sistema
            if (ultimaFilaVector.getMuelle1Estado() == EstadoMuelle.OCUPADO) {
                Muelle muelle1 = this.muelles.stream()
                        .filter(m -> m.getId() == 1)
                        .findFirst()
                        .orElse(null);
                if (muelle1.getBarcoAtendido() != null) {
                    int gruasPrevias = ultimaFilaVector.getMuelle1GruasAsignadas();
                    double trabajoRealizado = tiempoTranscurrido * gruasPrevias;
                    muelle1.getBarcoAtendido().reducirTiempoDescarga(trabajoRealizado);
                }
            }
            if (ultimaFilaVector.getMuelle2Estado() == EstadoMuelle.OCUPADO) {
                Muelle muelle2 = this.muelles.stream()
                        .filter(m -> m.getId() == 2)
                        .findFirst()
                        .orElse(null);
                if (muelle2.getBarcoAtendido() != null) {
                    int gruasPrevias = ultimaFilaVector.getMuelle2GruasAsignadas();
                    double trabajoRealizado = tiempoTranscurrido * gruasPrevias;
                    muelle2.getBarcoAtendido().reducirTiempoDescarga(trabajoRealizado);
                }
            }
        }

        // 2. LIMPIAR ESTADO ANTERIOR: Quitar eventos FinDescarga y liberar grúas
        fel.removeIf(evento -> evento instanceof FinDescarga);
        List<Barco> barcosEnServicio = new ArrayList<>();
        for (Grua grua : gruas) { grua.liberar(reloj); }
        for (Muelle muelle: muelles) { 
            muelle.setGruasAsignadas(0); 
            if (muelle.estaOcupado()){
                Barco barco = muelle.getBarcoAtendido();
                barcosEnServicio.add(barco);
            }
        } // Resetear contador
        
        // 3. REASIGNAR GRÚAS Y PROGRAMAR NUEVOS EVENTOS
        

        if (barcosEnServicio.size() == 1) {
            Barco barco = barcosEnServicio.get(0);
            asignarGruasABarco(barco, 2); // Asignar 2 grúas

            // El tiempo restante se divide por la nueva velocidad (2 grúas)
            double tiempoFinalRecalculado = barco.getTiempoDescargaRestante() / 2.0;
            double tiempoFinDescarga = reloj + tiempoFinalRecalculado;

            fel.add(new FinDescarga(tiempoFinDescarga, barco));
            actualizarVectorConTiemposDeDescarga(barco, tiempoFinDescarga, tiempoFinalRecalculado);

        } else if (barcosEnServicio.size() == 2) {
            for (Barco barco : barcosEnServicio) {
                asignarGruasABarco(barco, 1); // Asignar 1 grúa

                // El tiempo restante es el base (velocidad de 1 grúa)
                double tiempoFinalRecalculado = barco.getTiempoDescargaRestante();
                double tiempoFinDescarga = reloj + tiempoFinalRecalculado;

                fel.add(new FinDescarga(tiempoFinDescarga, barco));
                actualizarVectorConTiemposDeDescarga(barco, tiempoFinDescarga, tiempoFinalRecalculado);
            }
        }

        // Guardar el número de grúas asignadas en la fila actual para el próximo cálculo
        for(Muelle muelle : muelles) {
            if (muelle.getId() == 1) actualFilaVector.setMuelle1GruasAsignadas(muelle.getGruasAsignadas());
            if (muelle.getId() == 2) actualFilaVector.setMuelle2GruasAsignadas(muelle.getGruasAsignadas());
        }
    }

    // Método auxiliar para no repetir código
    private void actualizarVectorConTiemposDeDescarga(Barco barco, double finDescarga, double tiempoRestante) {
        for (Muelle muelle : muelles) {
            if (muelle.getBarcoAtendido() == barco) {
                if (muelle.getId() == 1) {
                    actualFilaVector.setFinDescarga1(finDescarga);
                    actualFilaVector.setTiempoRestanteMuelle1(tiempoRestante);
                } else if (muelle.getId() == 2) {
                    actualFilaVector.setFinDescarga2(finDescarga);
                    actualFilaVector.setTiempoRestanteMuelle2(tiempoRestante);
                }
                break;
            }
        }
    }



    /**
     * Actualiza todos los campos del FilaVector actual con el estado actual del sistema.
     */
    private void actualizarEstadoSistemaEnFilaVector() {
        // 1. Copiar datos de la última fila para mantener continuidad
        if (this.ultimaFilaVector != null) {
            copiarDatosDeUltimaFila();
        }

        // 2. Actualizar estado de la bahía
        this.actualFilaVector.setCantidadBarcosBahia(this.bahia.size());

        // 3. Actualizar estado de muelles
        for (Muelle muelle : muelles) {
            if (muelle.getId() == 1) {
                this.actualFilaVector.setMuelle1Estado(muelle.getEstado());
                if (muelle.estaOcupado()) {
                    this.actualFilaVector.setMuelle1InicioOcupado(muelle.getTiempoInicioOcupado());
                }
            } else if (muelle.getId() == 2) {
                this.actualFilaVector.setMuelle2Estado(muelle.getEstado());
                if (muelle.estaOcupado()) {
                    this.actualFilaVector.setMuelle2InicioOcupado(muelle.getTiempoInicioOcupado());
                }
            }
        }

        // 4. Actualizar estado de grúas
        for (Grua grua : gruas) {
            if (grua.getId() == 1) {
                this.actualFilaVector.setGrua1Estado(grua.getEstado());
                if (grua.estaOcupada()) {
                    this.actualFilaVector.setGrua1InicioOcupado(grua.getTiempoInicioOcupado());
                }
            } else if (grua.getId() == 2) {
                this.actualFilaVector.setGrua2Estado(grua.getEstado());
                if (grua.estaOcupada()) {
                    this.actualFilaVector.setGrua2InicioOcupado(grua.getTiempoInicioOcupado());
                }
            }
        }

        // 5. Actualizar acumuladores de tiempo ocupado
        actualizarAcumuladoresTiempo();

        // 6. Actualizar estadísticas de barcos
        actualizarEstadisticasBarcos();

        // 7. Actualizar porcentajes de utilización
        actualizarPorcentajesUtilizacion();

        // 8. Actualizar barcos en sistema
        actualizarBarcosEnSistema();
    }

    /**
     * Copia datos relevantes de la última fila para mantener continuidad.
     */
    private void copiarDatosDeUltimaFila() {
        if (this.ultimaFilaVector == null) return;

        // Copiar acumuladores que deben mantenerse
        this.actualFilaVector.setAcumuladorTiempoEsperaBahia(this.ultimaFilaVector.getAcumuladorTiempoEsperaBahia());
        this.actualFilaVector.setContadorBarcosAtendidos(this.ultimaFilaVector.getContadorBarcosAtendidos());
        this.actualFilaVector.setMaxTiempoPermanencia(this.ultimaFilaVector.getMaxTiempoPermanencia());
        this.actualFilaVector.setMinTiempoPermanencia(this.ultimaFilaVector.getMinTiempoPermanencia());
        this.actualFilaVector.setCantMaxBarcosEnSistema(this.ultimaFilaVector.getCantMaxBarcosEnSistema());

        // Copiar acumuladores de tiempo ocupado
        this.actualFilaVector.setMuelle1AcTiempoOcupado(this.ultimaFilaVector.getMuelle1AcTiempoOcupado());
        this.actualFilaVector.setMuelle2AcTiempoOcupado(this.ultimaFilaVector.getMuelle2AcTiempoOcupado());
        this.actualFilaVector.setGrua1AcTiempoOcupado(this.ultimaFilaVector.getGrua1AcTiempoOcupado());
        this.actualFilaVector.setGrua2AcTiempoOcupado(this.ultimaFilaVector.getGrua2AcTiempoOcupado());
    }

    // ==================== MÉTODOS AUXILIARES ====================

    /**
     * Busca y retorna el primer muelle libre disponible.
     */
    private Muelle buscarMuelleLibre() {
        return muelles.stream()
                .filter(muelle -> muelle.estaLibre())
                .findFirst()
                .orElse(null);
    }

    /**
     * Programa la próxima llegada de barco.
     */
    private void programarProximaLlegada() {
        // Generar número aleatorio y tiempo
        this.ultimoRndLlegada = generador.generarNumeroAleatorio();
        double tiempoInterLlegada = generador.convertirAExponencial(ultimoRndLlegada, configuracion.getMediaLlegadas());
        double tiempoProximaLlegada = this.reloj + tiempoInterLlegada;
        //setear el tiempo de llegada en la fila actual
        this.actualFilaVector.setRndLlegada(this.ultimoRndLlegada);
        this.actualFilaVector.setProximaLlegada(tiempoProximaLlegada);

        //INICIALIZAMOS EL CONTADOR DE BARCOS PORQUE EL OBJETO QUE VA A APARECER EN LA PROXIMA ITERACION YA FUE CREADO
        this.contadorBarcosGenerados++;
        Barco nuevoBarco = new Barco(this.contadorBarcosGenerados, tiempoProximaLlegada);

        Evento proximaLlegada = new LlegadaBarco(tiempoProximaLlegada, nuevoBarco);
        fel.add(proximaLlegada);
    }

    // ==================== MÉTODOS DE ACCESO PARA RESULTADOS ====================

    /**
     * Actualiza los acumuladores de tiempo ocupado de muelles y grúas.
     */
    private void actualizarAcumuladoresTiempo() {
        double tiempoTranscurrido = this.reloj - this.ultimaFilaVector.getTiempo();

        // Actualizar acumuladores de muelles
        for (Muelle muelle : muelles) {
            if (muelle.estaOcupado()) {
                if (muelle.getId() == 1) {
                    double tiempoOcupado = this.actualFilaVector.getMuelle1AcTiempoOcupado();
                    this.actualFilaVector.setMuelle1AcTiempoOcupado(tiempoOcupado + tiempoTranscurrido);
                } else if (muelle.getId() == 2) {
                    double tiempoOcupado2 = this.actualFilaVector.getMuelle2AcTiempoOcupado();
                    this.actualFilaVector.setMuelle2AcTiempoOcupado(tiempoOcupado2 + tiempoTranscurrido);
                }
            }
        }

        // Actualizar acumuladores de grúas
        for (Grua grua : gruas) {
            if (grua.estaOcupada()) {
                if (grua.getId() == 1) {
                    double tiempoOcupado = this.actualFilaVector.getGrua1AcTiempoOcupado();
                    this.actualFilaVector.setGrua1AcTiempoOcupado(tiempoOcupado + tiempoTranscurrido);
                } else if (grua.getId() == 2) {
                    double tiempoOcupado = this.actualFilaVector.getGrua2AcTiempoOcupado();
                    this.actualFilaVector.setGrua2AcTiempoOcupado(tiempoOcupado + tiempoTranscurrido);
                }
            }
        }
    }



    /**
     * Actualiza los porcentajes de utilización de recursos.
     */
    private void actualizarPorcentajesUtilizacion() {
        if (this.reloj > 0) {
            // Utilización de muelles
            double utilizacionM1 = (this.actualFilaVector.getMuelle1AcTiempoOcupado() / this.reloj) * 100;
            double utilizacionM2 = (this.actualFilaVector.getMuelle2AcTiempoOcupado() / this.reloj) * 100;
            this.actualFilaVector.setMuelle1Utilizacion(utilizacionM1);
            this.actualFilaVector.setMuelle2Utilizacion(utilizacionM2);

            // Utilización de grúas
            double utilizacionG1 = (this.actualFilaVector.getGrua1AcTiempoOcupado() / this.reloj) * 100;
            double utilizacionG2 = (this.actualFilaVector.getGrua2AcTiempoOcupado() / this.reloj) * 100;
            this.actualFilaVector.setGrua1Utilizacion(utilizacionG1);
            this.actualFilaVector.setGrua2Utilizacion(utilizacionG2);
        }
    }

    /**
     * Actualiza la información de barcos en el sistema.
     */
    private void actualizarBarcosEnSistema() {
        List<Barco> barcosEnSistema = new ArrayList<>();
        
        // Agregar barcos de la bahía
        barcosEnSistema.addAll(this.bahia);
        
        // Agregar barcos en muelles
        for (Muelle muelle : muelles) {
            if (muelle.estaOcupado() && muelle.getBarcoAtendido() != null) {
                barcosEnSistema.add(muelle.getBarcoAtendido());
            }
        }
        
        this.actualFilaVector.setBarcosEnSistema(barcosEnSistema);
        this.actualFilaVector.setCantBarcosEnSistema(barcosEnSistema.size());
        
        // Actualizar máximo si es necesario
        int cantidadActual = barcosEnSistema.size();
        if (cantidadActual > this.actualFilaVector.getCantMaxBarcosEnSistema()) {
            this.actualFilaVector.setCantMaxBarcosEnSistema(cantidadActual);
        }
    }

    /**
     * Retorna los vectores de estado generados durante la simulación.
     */
    public List<FilaVector> getVectoresEstado() {
        return new ArrayList<>(vectoresEstado);
    }
}


