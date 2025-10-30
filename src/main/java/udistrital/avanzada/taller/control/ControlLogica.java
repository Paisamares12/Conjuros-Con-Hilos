package udistrital.avanzada.taller.control;

import java.io.IOException;
import javax.swing.SwingWorker;
import udistrital.avanzada.taller.modelo.*;
import udistrital.avanzada.taller.modelo.persistencia.CargadorPropiedades;

/**
 * Controlador principal de la lógica del programa <b>ConjurosConHilos</b>.
 * <p>
 * Coordina la comunicación entre la vista y el modelo, asegurando que la
 * interfaz gráfica nunca interactúe directamente con las clases de dominio.
 * <br>
 * Gestiona la carga de datos, la ejecución de los duelos (individuales o en
 * torneo) y el control del flujo general del simulador.
 * </p>
 *
 * <p>
 * Cumple con el patrón MVC y los principios SOLID, especialmente SRP y DIP.
 * </p>
 *
 * Creada por Paula Martínez y modificada por Juan Ariza y Sebastián Bravo.
 *
 * @author Paula
 * @version 6.0
 * @since 2025-10-29
 */
public class ControlLogica {

    private final CargadorPropiedades cargador;
    private final ControlInterfaz cInterfaz;

    private LibroHechizos libro;
    private ListadoMagos listado;
    private GestorTorneo gestorTorneo;

    /**
     * Constructor principal. Inicializa la capa lógica y vincula la interfaz.
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

    private void inicializarTorneo() {
        if (listado != null && libro != null)
            gestorTorneo = new GestorTorneo(listado, libro);
    }

    // ============================================================
    // ===============   EJECUCIÓN DE DUELOS   ====================
    // ============================================================

    /**
     * Ejecuta un duelo simple (sin torneo) y notifica el resultado.
     */
    public void ejecutarDueloSimple(Mago m1, Mago m2) {
        if (m1 == null || m2 == null) throw new IllegalArgumentException("Magos inválidos");
        CampoDeDuelo duelo = new CampoDeDuelo(m1, m2, libro);
        ResultadoDuelo resultado = duelo.iniciar();
        cInterfaz.mostrarResultado(resultado);
    }

    /**
     * Inicia el siguiente duelo del torneo en un hilo separado, notificando
     * progresos a la vista a través de un observador.
     *
     * @param observador instancia que recibe los eventos del duelo
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
                // El observador ya maneja la actualización de la UI
            }
        };
        worker.execute();
    }

    // ============================================================
    // ===============   MÉTODOS DE ESTADO   ======================
    // ============================================================

    public boolean datosListos() {
        return listado != null && !listado.getMagos().isEmpty()
                && libro != null && !libro.getHechizos().isEmpty();
    }

    public boolean puedeIniciarDuelo() {
        return gestorTorneo != null && gestorTorneo.hayDueloDisponible();
    }

    public void reiniciarTorneo() {
        if (listado != null && libro != null)
            gestorTorneo = new GestorTorneo(listado, libro);
    }

    // ============================================================
    // ===============   GETTERS DE INFORMACIÓN ===================
    // ============================================================

    public Mago getCampeonActual() {
        if (gestorTorneo == null) return null;
        return gestorTorneo.obtenerEstadisticas().getCampeonActual();
    }

    public int getDuelosRealizados() {
        if (gestorTorneo == null) return 0;
        return gestorTorneo.obtenerEstadisticas().getDuelosRealizados();
    }

    public GestorTorneo getGestorTorneo() {
        return gestorTorneo;
    }
}
