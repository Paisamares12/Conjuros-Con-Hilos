package udistrital.avanzada.taller.control;

import java.io.IOException;
import javax.swing.SwingWorker;
import udistrital.avanzada.taller.modelo.*;
import udistrital.avanzada.taller.modelo.persistencia.CargadorPropiedades;

/**
 * Controlador principal de la lógica del programa <b>ConjurosConHilos</b>.
 *
 * <p>
 * Coordina la comunicación entre la vista y el modelo, garantizando que la interfaz
 * gráfica no interactúe directamente con las clases de dominio. Administra la carga
 * de datos (magos y hechizos), la inicialización del torneo y la ejecución de duelos
 * (individuales o encadenados en torneo).
 * </p>
 *
 * 
 * Cumple el patrón MVC y los principios SOLID (especialmente SRP y DIP):
 * <ul>
 *   <li><b>SRP</b>: concentra la orquestación de casos de uso (cargar, iniciar duelos, exponer estado).</li>
 *   <li><b>DIP</b>: la vista depende de este controlador y no del modelo concreto.</li>
 * </ul>
 * 
 *
 * <p>
 * <b>Concurrencia:</b> los duelos se ejecutan en hilos de trabajo mediante
 * {@link SwingWorker} y dentro de {@link CampoDeDuelo}, que usa sincronización
 * con <i>wait/notify</i> para alternar turnos.
 * </p>
 *
 * <p>
 * Creada por Paula Martínez y modificada por Juan Ariza y Sebastián Bravo.
 * </p>
 *
 * @author Paula Martínez
 * @version 6.0
 * @since 2025-10-29
 */
public class ControlLogica {

    /** Servicio de lectura de archivos de propiedades (.properties). */
    private final CargadorPropiedades cargador;

    /** Controlador de la capa de interfaz (vista). */
    private final ControlInterfaz cInterfaz;

    /** Repositorio en memoria de hechizos disponibles. */
    private LibroHechizos libro;

    /** Repositorio en memoria de magos participantes. */
    private ListadoMagos listado;

    /** Coordinador del flujo de enfrentamientos en modo torneo. */
    private GestorTorneo gestorTorneo;

    /**
     * Constructor principal. Inicializa la capa lógica, repositorios y vincula la interfaz.
     *
     * <p>Se crean contenedores vacíos para libro y listado, y se instancia la vista
     * asociando este controlador.</p>
     */
    public ControlLogica() {
        this.cargador = new CargadorPropiedades();
        this.libro = new LibroHechizos();
        this.listado = new ListadoMagos();
        this.cInterfaz = new ControlInterfaz(this);
    }

    // ============================================================
    // ===============   CARGA DE DATOS   =========================
    // ============================================================

    /**
     * Carga el listado de magos desde un archivo de propiedades y, si los hechizos ya
     * están cargados, inicializa el torneo.
     *
     * @param rutaArchivo ruta absoluta del archivo de magos; si es {@code null},
     *                    el usuario seleccionará el archivo mediante un {@code JFileChooser}
     * @return {@code true} si la carga fue exitosa; {@code false} en caso de error
     */
    public boolean cargarMagos(String rutaArchivo) {
        try {
            this.listado = cargador.cargarMagos(rutaArchivo);
            if (datosListos()) inicializarTorneo();
            return true;
        } catch (IOException e) {
            System.err.println("Error al cargar magos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Carga el libro de hechizos desde un archivo de propiedades y, si los magos ya
     * están cargados, inicializa el torneo.
     *
     * @param rutaArchivo ruta absoluta del archivo de hechizos; si es {@code null},
     *                    el usuario seleccionará el archivo mediante un {@code JFileChooser}
     * @return {@code true} si la carga fue exitosa; {@code false} en caso de error
     */
    public boolean cargarHechizos(String rutaArchivo) {
        try {
            this.libro = cargador.cargarHechizos(rutaArchivo);
            if (datosListos()) inicializarTorneo();
            return true;
        } catch (IOException e) {
            System.err.println("Error al cargar hechizos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inicializa el gestor de torneo con los repositorios cargados.
     *
     * <p>Precondición: {@link #listado} y {@link #libro} no deben ser {@code null} y
     * deben contener datos válidos.</p>
     */
    private void inicializarTorneo() {
        if (listado != null && libro != null)
            gestorTorneo = new GestorTorneo(listado, libro);
    }

    // ============================================================
    // ===============   EJECUCIÓN DE DUELOS   ====================
    // ============================================================

    /**
     * Ejecuta un duelo simple (fuera del esquema de torneo) y notifica el resultado
     * a la vista.
     *
     * <p>El duelo es bloqueante en este hilo, pero su lógica interna usa hilos para
     * alternar turnos dentro de {@link CampoDeDuelo}.</p>
     *
     * @param m1 primer mago participante
     * @param m2 segundo mago participante
     * @throws IllegalArgumentException si alguno de los magos es {@code null}
     */
    public void ejecutarDueloSimple(Mago m1, Mago m2) {
        if (m1 == null || m2 == null) throw new IllegalArgumentException("Magos inválidos");
        CampoDeDuelo duelo = new CampoDeDuelo(m1, m2, libro);
        ResultadoDuelo resultado = duelo.iniciar();
        cInterfaz.mostrarResultado(resultado);
    }

    /**
     * Inicia el siguiente duelo del torneo en un hilo de trabajo, notificando
     * los eventos a través del observador recibido.
     *
     * <p>
     * El método no bloquea el hilo de la EDT (Event Dispatch Thread); utiliza un
     * {@link SwingWorker} para delegar la ejecución y dejar la UI receptiva.
     * </p>
     *
     * @param observador observador de eventos del duelo (inicio, lanzamientos, aturdimiento, fin)
     * @throws IllegalStateException si el torneo no ha sido inicializado o si no hay duelos disponibles
     */
    public void ejecutarSiguienteDueloTorneoConObservador(CampoDeDuelo.ObservadorDuelo observador) {
        if (gestorTorneo == null)
            throw new IllegalStateException("No hay torneo inicializado");
        if (!gestorTorneo.hayDueloDisponible())
            throw new IllegalStateException("No hay más duelos disponibles");

        SwingWorker<ResultadoDuelo, Void> worker = new SwingWorker<>() {
            @Override
            protected ResultadoDuelo doInBackground() {
                return gestorTorneo.ejecutarSiguienteDueloConObservador(observador);
            }
            @Override
            protected void done() {
                // El observador actualiza la UI; no se requiere lógica adicional aquí.
            }
        };
        worker.execute();
    }

    // ============================================================
    // ===============   MÉTODOS DE ESTADO   ======================
    // ============================================================

    /**
     * Indica si existen datos suficientes para poder iniciar un torneo/duelo:
     * al menos 1 mago cargado y al menos 1 hechizo cargado.
     *
     * @return {@code true} si hay magos y hechizos cargados; {@code false} en caso contrario
     */
    public boolean datosListos() {
        return listado != null && !listado.getMagos().isEmpty()
                && libro != null && !libro.getHechizos().isEmpty();
    }

    /**
     * Verifica si es posible iniciar o continuar un duelo de torneo con los datos
     * y estado actual del gestor de torneo.
     *
     * @return {@code true} si existe un torneo inicializado y hay duelos disponibles;
     *         {@code false} en caso contrario
     */
    public boolean puedeIniciarDuelo() {
        return gestorTorneo != null && gestorTorneo.hayDueloDisponible();
    }

    /**
     * Reinicia el torneo con los repositorios actualmente cargados.
     *
     * <p>No afecta a los datos de magos ni hechizos; solo recrea el flujo del torneo.</p>
     */
    public void reiniciarTorneo() {
        if (listado != null && libro != null)
            gestorTorneo = new GestorTorneo(listado, libro);
    }

    // ============================================================
    // ===============   GETTERS DE INFORMACIÓN ===================
    // ============================================================

    /**
     * Devuelve el campeón actual según las estadísticas del torneo.
     *
     * @return el {@link Mago} que ostenta el título actualmente, o {@code null} si no hay torneo
     */
    public Mago getCampeonActual() {
        if (gestorTorneo == null) return null;
        return gestorTorneo.obtenerEstadisticas().getCampeonActual();
    }

    /**
     * Devuelve la cantidad de duelos realizados hasta el momento según las estadísticas del torneo.
     *
     * @return número entero de duelos realizados; 0 si no hay torneo
     */
    public int getDuelosRealizados() {
        if (gestorTorneo == null) return 0;
        return gestorTorneo.obtenerEstadisticas().getDuelosRealizados();
    }

    /**
     * Devuelve el gestor de torneo en uso (si ha sido inicializado).
     *
     * @return instancia de {@link GestorTorneo}, o {@code null} si aún no se ha creado
     */
    public GestorTorneo getGestorTorneo() {
        return gestorTorneo;
    }
}
