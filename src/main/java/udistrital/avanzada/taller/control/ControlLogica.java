package udistrital.avanzada.taller.control;

import java.io.IOException;
import udistrital.avanzada.taller.modelo.*;
import udistrital.avanzada.taller.modelo.persistencia.CargadorPropiedades;

/**
 * Controlador principal de la lógica del programa <b>ConjurosConHilos</b>.
 * <p>
 * Coordina la comunicación entre la interfaz de usuario y el modelo, cargando
 * los datos iniciales (magos y hechizos) y gestionando la ejecución de los
 * duelos mágicos. Aplica los principios del patrón MVC y mantiene el flujo
 * general del simulador.
 * </p>
 *
 * <p>
 * Esta clase no implementa directamente la lógica de combate ni la interfaz;
 * delega dichas responsabilidades a {@link CampoDeDuelo} y
 * {@link ControlInterfaz}, respectivamente.
 * </p>
 *
 * <p>
 * Originalmente creada por Paula Martínez.<br>
 * Modificada por Juan Sebastián Bravo Rojas
 * </p>
 * 
 * @author Paula Martínez
 * @version 2.0
 * @since 2025-10-26
 */
public class ControlLogica {

    /** Controlador de la interfaz gráfica (Vista–Controlador). */
    private ControlInterfaz cInterfaz;

    /** Gestor de carga de archivos de propiedades. */
    private CargadorPropiedades cargador;

    /** Libro global de hechizos cargado desde archivo. */
    private LibroHechizos libro;

    /** Listado global de magos participantes. */
    private ListadoMagos listado;

    /**
     * Constructor que inicializa la capa lógica del sistema.
     * <p>
     * Carga los datos de magos y hechizos, y establece la comunicación con la
     * interfaz de usuario.
     * </p>
     */
    public ControlLogica() {
        this.cInterfaz = new ControlInterfaz(this);
        this.cargador = new CargadorPropiedades();

        try {
            // Carga dinámica mediante JFileChooser (si la ruta es null)
            this.libro = cargador.cargarHechizos(null);
            this.listado = cargador.cargarMagos(null);
        } catch (IOException e) {
            System.err.println("Error al cargar archivos de propiedades: " + e.getMessage());
            this.libro = new LibroHechizos();
            this.listado = new ListadoMagos();
        }
    }

    /**
     * Inicia un duelo entre dos magos dados, utilizando el libro de hechizos
     * previamente cargado.
     * <p>
     * Esta operación crea una instancia de {@link CampoDeDuelo}, ejecuta la
     * simulación y comunica el resultado final a la interfaz.
     * </p>
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

        // Comunicación con la interfaz
        cInterfaz.mostrarResultado(resultado);
    }

    /**
     * Devuelve el listado de magos cargado desde el archivo de propiedades.
     *
     * @return listado de magos
     */
    public ListadoMagos getListadoMagos() {
        return listado;
    }

    /**
     * Devuelve el conjunto de hechizos cargado desde el archivo de propiedades.
     *
     * @return libro de hechizos
     */
    public LibroHechizos getLibroHechizos() {
        return libro;
    }
}
