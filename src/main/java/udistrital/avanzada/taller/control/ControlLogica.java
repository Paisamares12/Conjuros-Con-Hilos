package udistrital.avanzada.taller.control;

import java.io.IOException;
import udistrital.avanzada.taller.modelo.*;
import udistrital.avanzada.taller.modelo.persistencia.CargadorPropiedades;

/**
 * Controlador principal de la lógica del programa <b>ConjurosConHilos</b>.
 * <p>
 * Coordina la comunicación entre la interfaz de usuario y el modelo, cargando
 * los datos iniciales (magos y hechizos) y gestionando la ejecución de los
 * duelos mágicos y torneos.
 *
 * Creada por Paula Martínez Modificada por Juan Ariza
 * </p>
 *
 * @author Paula Martínez
 * @version 5.0
 * @since 2025-10-29
 */

//TODO: revisar que cumpla con el MVC y los SOLID *no sé si pueda tener modelo*
public class ControlLogica {

    private ControlInterfaz cInterfaz;
    private CargadorPropiedades cargador;
    private LibroHechizos libro;
    private ListadoMagos listado;
    private GestorTorneo gestorTorneo;

    /**
     * Constructor que inicializa la capa lógica del sistema.
     */
    public ControlLogica() {
        this.cInterfaz = new ControlInterfaz(this);
        this.cargador = new CargadorPropiedades();
        this.libro = new LibroHechizos();
        this.listado = new ListadoMagos();
    }

    /**
     * Carga los magos desde un archivo de propiedades.
     *
     * @param rutaArchivo ruta del archivo de magos (null para usar
     * JFileChooser)
     * @return true si la carga fue exitosa
     */
    public boolean cargarMagos(String rutaArchivo) {
        try {
            this.listado = cargador.cargarMagos(rutaArchivo);

            // Si hay magos y hechizos cargados, inicializar el torneo
            if (listado != null && listado.getMagos() != null && !listado.getMagos().isEmpty()
                    && libro != null && libro.getHechizos() != null && !libro.getHechizos().isEmpty()) {
                inicializarTorneo();
            }

            return true;
        } catch (IOException e) {
            System.err.println("Error al cargar magos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Carga los hechizos desde un archivo de propiedades.
     *
     * @param rutaArchivo ruta del archivo de hechizos (null para usar
     * JFileChooser)
     * @return true si la carga fue exitosa
     */
    public boolean cargarHechizos(String rutaArchivo) {
        try {
            this.libro = cargador.cargarHechizos(rutaArchivo);

            // Si hay magos y hechizos cargados, inicializar el torneo
            if (listado != null && listado.getMagos() != null && !listado.getMagos().isEmpty()
                    && libro != null && libro.getHechizos() != null && !libro.getHechizos().isEmpty()) {
                inicializarTorneo();
            }

            return true;
        } catch (IOException e) {
            System.err.println("Error al cargar hechizos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inicializa el gestor de torneo con los magos y hechizos cargados.
     */
    private void inicializarTorneo() {
        if (listado != null && libro != null) {
            gestorTorneo = new GestorTorneo(listado, libro);
        }
    }

    /**
     * Verifica si hay datos suficientes cargados para iniciar un duelo.
     *
     * @return true si hay al menos 2 magos y hechizos cargados
     */
    public boolean puedeIniciarDuelo() {
        return gestorTorneo != null && gestorTorneo.hayDueloDisponible();
    }

    /**
     * Inicia un duelo entre dos magos específicos (modo individual).
     *
     * @param mago1 primer mago participante
     * @param mago2 segundo mago participante
     */
    public void iniciarDuelo(Mago mago1, Mago mago2) {
        if (mago1 == null || mago2 == null) {
            System.err.println("Error: magos no válidos para el duelo.");
            return;
        }

        CampoDeDuelo duelo = new CampoDeDuelo(mago1, mago2, libro);
        ResultadoDuelo resultado = duelo.iniciar();

        cInterfaz.mostrarResultado(resultado);
    }

    /**
     * Inicia un duelo con observador para actualización de UI en tiempo real.
     *
     * @param mago1 primer mago participante
     * @param mago2 segundo mago participante
     * @param observador observador del duelo
     * @return resultado del duelo
     */
    public ResultadoDuelo iniciarDueloConObservador(Mago mago1, Mago mago2,
            CampoDeDuelo.ObservadorDuelo observador) {
        if (mago1 == null || mago2 == null) {
            throw new IllegalArgumentException("Los magos no pueden ser nulos");
        }

        CampoDeDuelo duelo = new CampoDeDuelo(mago1, mago2, libro);
        duelo.setObservador(observador);
        return duelo.iniciar();
    }

    /**
     * Inicia el siguiente duelo del torneo.
     *
     * @return resultado del duelo, o null si no hay duelos disponibles
     */
    public ResultadoDuelo iniciarSiguienteDueloTorneo() {
        if (gestorTorneo == null) {
            System.err.println("Error: no hay torneo inicializado.");
            return null;
        }

        if (!gestorTorneo.hayDueloDisponible()) {
            System.err.println("No hay más duelos disponibles en el torneo.");
            return null;
        }

        return gestorTorneo.ejecutarSiguienteDuelo();
    }

    /**
     * Inicia el siguiente duelo del torneo con observador.
     *
     * @param observador observador del duelo
     * @return resultado del duelo, o null si no hay duelos disponibles
     */
    public ResultadoDuelo iniciarSiguienteDueloTorneoConObservador(
            CampoDeDuelo.ObservadorDuelo observador) {

        if (gestorTorneo == null) {
            throw new IllegalStateException("No hay torneo inicializado");
        }

        if (!gestorTorneo.hayDueloDisponible()) {
            throw new IllegalStateException("No hay más duelos disponibles");
        }

        return gestorTorneo.ejecutarSiguienteDueloConObservador(observador);
    }

    /**
     * Obtiene los magos del siguiente duelo sin ejecutarlo.
     *
     * @return array con los dos magos que participarán
     */
    public Mago[] obtenerSiguienteDuelo() {
        if (gestorTorneo == null || !gestorTorneo.hayDueloDisponible()) {
            return null;
        }
        return gestorTorneo.obtenerSiguienteDuelo();
    }

    /**
     * Reinicia el torneo con los magos actuales.
     */
    public void reiniciarTorneo() {
        if (listado != null && libro != null) {
            gestorTorneo = new GestorTorneo(listado, libro);
        }
    }

    // ========== GETTERS ==========
    /**
     * Obtiene el listado actual de magos registrados en el sistema.
     *
     * <p>
     * Este listado contiene todos los participantes disponibles para los duelos
     * o torneos. Es cargado desde un archivo de propiedades mediante el
     * {@link CargadorPropiedades}.
     * </p>
     *
     * @return instancia de {@link ListadoMagos} con los magos actualmente
     * cargados
     */
    public ListadoMagos getListadoMagos() {
        return listado;
    }

    /**
     * Devuelve el libro de hechizos actualmente cargado en el sistema.
     *
     * <p>
     * Este libro contiene todos los hechizos disponibles que los magos pueden
     * usar durante los duelos mágicos. Es cargado desde un archivo de
     * propiedades mediante el {@link CargadorPropiedades}.
     * </p>
     *
     * @return objeto {@link LibroHechizos} con la lista de hechizos disponibles
     */
    public LibroHechizos getLibroHechizos() {
        return libro;
    }

    /**
     * Obtiene el gestor de torneo actual.
     *
     * <p>
     * El {@link GestorTorneo} administra el flujo de duelos, determina los
     * enfrentamientos, lleva el historial de resultados y controla el avance de
     * las rondas del torneo.
     * </p>
     *
     * @return instancia activa de {@link GestorTorneo}, o {@code null} si el
     * torneo no se ha inicializado
     */
    public GestorTorneo getGestorTorneo() {
        return gestorTorneo;
    }

    /**
     * Devuelve el controlador encargado de gestionar la interfaz gráfica.
     *
     * <p>
     * El {@link ControlInterfaz} coordina la interacción con el usuario, los
     * paneles gráficos y la actualización visual de los duelos y torneos.
     * </p>
     *
     * @return instancia de {@link ControlInterfaz} utilizada por esta capa
     * lógica
     */
    public ControlInterfaz getControlInterfaz() {
        return cInterfaz;
    }

    /**
     * Verifica si los datos necesarios están cargados.
     *
     * @return true si hay magos y hechizos cargados
     */
    public boolean datosListos() {
        return listado != null && listado.getMagos() != null && !listado.getMagos().isEmpty()
                && libro != null && libro.getHechizos() != null && !libro.getHechizos().isEmpty();
    }
}
