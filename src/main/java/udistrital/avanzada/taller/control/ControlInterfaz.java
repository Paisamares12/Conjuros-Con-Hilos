package udistrital.avanzada.taller.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import udistrital.avanzada.taller.modelo.ListadoMagos;
import udistrital.avanzada.taller.modelo.LibroHechizos;
import udistrital.avanzada.taller.modelo.Mago;
import udistrital.avanzada.taller.modelo.ResultadoDuelo;
import udistrital.avanzada.taller.vista.VentanaPrincipal;

/**
 * Controlador de la capa de interfaz del proyecto <b>ConjurosConHilos</b>.
 * <p>
 * Se encarga de recibir los eventos generados por la vista, comunicarlos a la
 * capa lógica {@link ControlLogica} y actualizar la interfaz según los
 * resultados del sistema.
 * </p>
 *
 * <p>
 * Aplica el patrón MVC, actuando como puente entre la vista y el modelo sin
 * contener reglas de negocio.
 * </p>
 *
 * <p>
 * Originalmente creada por Paula Martínez.<br>
 * Modificada por Juan Sebastián Bravo Rojas
 * </p>
 * 
 * @author Paula
 * @version 2.0
 * @since 2025-10-26
 */
public class ControlInterfaz implements ActionListener {

    /** Controlador de la capa lógica del sistema. */
    private final ControlLogica cLogica;

    /** Ventana principal de la aplicación. */
    private VentanaPrincipal vPrincipal;

    /**
     * Constructor que inicializa la interfaz gráfica principal.
     *
     * @param cLogica controlador lógico asociado
     */
    public ControlInterfaz(ControlLogica cLogica) {
        this.cLogica = cLogica;
        iniciarPrograma();
    }

    /**
     * Inicializa la interfaz principal y conecta los eventos de la UI con el
     * controlador.
     */
    private void iniciarPrograma() {
        this.vPrincipal = new VentanaPrincipal(this);
        this.vPrincipal.setVisible(true);
        this.vPrincipal.setLocationRelativeTo(null);
        conectarEventos();
    }

    /**
     * Conecta los botones de la interfaz con sus listeners.
     */
    private void conectarEventos() {
        this.vPrincipal.getPanelMain().getPanelInicio().getBotonJugar().addActionListener(this);
        this.vPrincipal.getPanelMain().getPanelInicio().getBotonSalir().addActionListener(this);
        this.vPrincipal.getPanelMain().getPanelCargar().getBotonSalir().addActionListener(this);
        this.vPrincipal.getPanelMain().getPanelCargar().getBotonJugar().addActionListener(this);
    }

    /**
     * Maneja los eventos generados por los botones de la aplicación.
     *
     * @param e evento de acción recibido
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // ---- BOTONES DE SALIDA ----
        if (e.getSource() == this.vPrincipal.getPanelMain().getPanelInicio().getBotonSalir()
                || e.getSource() == this.vPrincipal.getPanelMain().getPanelCargar().getBotonSalir()) {
            this.vPrincipal.dispose();
            System.exit(0);
        }

        // ---- BOTÓN "JUGAR" EN PANEL INICIO ----
        if (e.getSource() == this.vPrincipal.getPanelMain().getPanelInicio().getBotonJugar()) {
            this.vPrincipal.getPanelMain().mostrarPanelCargar();
            return;
        }

        // ---- BOTÓN "JUGAR" EN PANEL CARGAR ----
        if (e.getSource() == this.vPrincipal.getPanelMain().getPanelCargar().getBotonJugar()) {
            ejecutarDuelo();
        }
    }

    /**
     * Ejecuta un duelo entre dos magos seleccionados desde la interfaz.
     * <p>
     * Recupera los magos y el libro de hechizos desde la capa lógica, ejecuta
     * el enfrentamiento y muestra el resultado al usuario.
     * </p>
     */
    private void ejecutarDuelo() {
        ListadoMagos listado = cLogica.getListadoMagos();
        LibroHechizos libro = cLogica.getLibroHechizos();

        if (listado == null || listado.getMagos() == null || listado.getMagos().size() < 2) {
            JOptionPane.showMessageDialog(vPrincipal, "No hay suficientes magos cargados para iniciar un duelo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Por simplicidad: seleccionar aleatoriamente dos magos distintos
        Mago mago1 = listado.getMagos().get(0);
        Mago mago2 = listado.getMagos().get(1);

        JOptionPane.showMessageDialog(vPrincipal,
                "Comienza el duelo entre " + mago1.getNombre() + " y " + mago2.getNombre() + "!",
                "Inicio del Duelo", JOptionPane.INFORMATION_MESSAGE);

        cLogica.iniciarDuelo(mago1, mago2);
    }

    /**
     * Muestra en pantalla el resultado final del duelo.
     *
     * @param resultado objeto con los datos del combate
     */
    public void mostrarResultado(ResultadoDuelo resultado) {
        if (resultado == null || resultado.getGanador() == null) {
            JOptionPane.showMessageDialog(vPrincipal, "El duelo no generó un resultado válido.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String mensaje = "Ganador: " + resultado.getGanador().getNombre()
                + "\nCasa: " + resultado.getGanador().getCasa()
                + "\n\nPerdedor: " + resultado.getPerdedor().getNombre()
                + "\nCasa: " + resultado.getPerdedor().getCasa()
                + "\n\nPuntos: " + resultado.getPuntosGanador() + " - " + resultado.getPuntosPerdedor()
                + "\nHechizos lanzados: " + resultado.getHechizosLanzadosGanador();

        JOptionPane.showMessageDialog(vPrincipal, mensaje, "Resultado del Duelo", JOptionPane.INFORMATION_MESSAGE);
    }
}
